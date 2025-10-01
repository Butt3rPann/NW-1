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
import sit.integrated.backend.utils.OrderStatus;

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

    public void validateOrderRequest(List<OrderRequestDto> orderRequests) {
        if (orderRequests == null) {
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
    public List<OrderResponseDto> placeOrders(List<OrderRequestDto> orderRequests) {
        validateOrderRequest(orderRequests);
        List<OrderResponseDto> responses = new ArrayList<>();
        for (OrderRequestDto request : orderRequests) {
            if (request.getSellerId().equals(request.getBuyerId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seller cannot buy their own products");
            }
            User buyer = userRepository.findById(request.getBuyerId()).orElseThrow(() -> new ResourceNotFoundException("Buyer not found"));
            Order order = modelMapper.map(request, Order.class);
            order.setOrderStatus(OrderStatus.COMPLETED);
            order.setUser(buyer);
            orderRepository.save(order);
            for (OrderItemDto item : request.getOrderItems()) {
                SaleItem saleItem = saleItemRepository.findById(item.getSaleItemId()).orElseThrow(() -> new ResourceNotFoundException("Sale item not found"));
                if (saleItem.getQuantity() < item.getQuantity()) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Not enough stock for item " + saleItem.getId());
                }
                saleItem.setQuantity(saleItem.getQuantity() - item.getQuantity());
                saleItemRepository.save(saleItem);
                OrderItem orderItem = modelMapper.map(item, OrderItem.class);
                orderItem.setId(null);
                orderItem.setOrder(order);
                orderItem.setSaleItem(saleItem);
                orderItemRepository.save(orderItem);
            }
            OrderResponseDto response = modelMapper.map(order, OrderResponseDto.class);
            response.setBuyerId(order.getUser().getId());
            User seller = userRepository.findById(request.getSellerId()).orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
            response.setSeller(modelMapper.map(seller, SellerOrderDto.class));
            response.getOrderItems().forEach(item -> item.setNo(order.getId()));
            responses.add(response);
        }
        return responses;
    }
}
