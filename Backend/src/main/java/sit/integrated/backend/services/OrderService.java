package sit.integrated.backend.services;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.integrated.backend.dtos.OrderItemDto;
import sit.integrated.backend.dtos.OrderRequestDto;
import sit.integrated.backend.dtos.OrderResponseDto;
import sit.integrated.backend.dtos.SellerOrderDto;
import sit.integrated.backend.entities.*;
import sit.integrated.backend.repositories.OrderItemRepository;
import sit.integrated.backend.repositories.OrderRepository;
import sit.integrated.backend.repositories.SaleItemRepository;
import sit.integrated.backend.repositories.UserRepository;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private SaleItemRepository saleItemRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Page<Order> getOrdersByBuyer(Integer buyerId, String sortField, String sortDirection, Integer page, Integer size) {
        if (!userRepository.existsById(buyerId)) {
            throw new ResourceNotFoundException("User not found with id " + buyerId);
        }
        Sort sort = (sortField == null ? Sort.by("orderDate", "id") : Sort.by(Sort.Direction.fromString(sortDirection), sortField).and(Sort.by("id")));
        return orderRepository.findOrdersByUserId(buyerId, PageRequest.of(page, size, sort));
    }

    public void validateOrderRequest(Integer userId, List<OrderRequestDto> orderRequests) {
        if (userId == null || orderRequests == null) {
            throw new IllegalArgumentException("Missing required fields for Order");
        }
        for (OrderRequestDto request : orderRequests) {
            if (request.getSellerId() == null ||
                    request.getShippingAddress() == null ||
                    request.getOrderItems() == null) {
                throw new IllegalArgumentException("Missing required fields for OrderRequest");
            }
            for (OrderItemDto itemDto : request.getOrderItems()) {
                if (itemDto.getSaleItemId() == null || itemDto.getQuantity() == null) {
                    throw new IllegalArgumentException("Missing required fields for OrderItem");
                }
            }
        }
    }

    @Transactional
    public List<OrderResponseDto> placeOrders(Integer userId, List<OrderRequestDto> orderRequests) {
        validateOrderRequest(userId, orderRequests);
        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserId not found"));
        List<OrderResponseDto> responses = new ArrayList<>();
        for (OrderRequestDto request : orderRequests) {
            User seller = userRepository.findById(request.getSellerId())
                    .orElseThrow(() -> new ResourceNotFoundException("SellerId not found"));
            Order order = modelMapper.map(request, Order.class);
            order.setUser(buyer);
            order.setOrderDate(Instant.now());
            orderRepository.save(order);
            Set<OrderItem> savedItems = new HashSet<>();
            for (OrderItemDto itemDto : request.getOrderItems()) {
                SaleItem saleItem = saleItemRepository.findById(itemDto.getSaleItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("Sale item not found"));
                if (request.getSellerId().equals(request.getBuyerId())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sellers cannot buy their own products");
                }
                if (saleItem.getQuantity() < itemDto.getQuantity()) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Not enough stock for item " + saleItem.getId());
                }
                saleItem.setQuantity(saleItem.getQuantity() - itemDto.getQuantity());
                saleItemRepository.save(saleItem);
                OrderItem orderItem = modelMapper.map(itemDto, OrderItem.class);
                orderItem.setOrder(order);
                orderItem.setSaleItem(saleItem);
                savedItems.add(orderItemRepository.save(orderItem));
            }
            order.setOrderItems(savedItems);
            OrderResponseDto response = modelMapper.map(order, OrderResponseDto.class);
            response.setBuyerId(order.getUser().getId());
            SellerOrderDto sellerDto = modelMapper.map(seller, SellerOrderDto.class);
            response.setSeller(sellerDto);
            response.setOrderDate(order.getOrderDate());
            response.setOrderItems(
                    savedItems.stream().map(item -> {
                        OrderItemDto dto = modelMapper.map(item, OrderItemDto.class);
                        dto.setNo(item.getOrder().getId());
                        dto.setSaleItemId(item.getSaleItem().getId());
                        return dto;
                    }).collect(Collectors.toSet())
            );
            responses.add(response);
        }
        return responses;
    }
}
