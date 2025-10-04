package sit.integrated.backend.services;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.integrated.backend.dtos.*;
import sit.integrated.backend.entities.*;
import sit.integrated.backend.repositories.*;
import sit.integrated.backend.entities.AuthUserDetail;
import sit.integrated.backend.utils.ListMapper;
import sit.integrated.backend.utils.OrderStatus;
import java.util.*;

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
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private CartItemRepository cartItemRepository;

    public PageDto<OrderResponseDto> getOrdersByBuyer(Integer buyerId, Integer page, Integer size) {
        if (!userRepository.existsById(buyerId)) {
            throw new ResourceNotFoundException("User not found with id " + buyerId);
        }
        Sort sort =  Sort.by("orderDate").descending().and(Sort.by("id"));
        Page<Order> orders = orderRepository.findOrdersByUserId(buyerId, PageRequest.of(page, size, sort));
        PageDto<OrderResponseDto> dtos = listMapper.toPageDto(orders, OrderResponseDto.class, modelMapper);
        for (int i = 0; i < orders.getContent().size(); i++) {
            Order order = orders.getContent().get(i);
            OrderResponseDto dto = dtos.getContent().get(i);

            User seller = order.getOrderItems()
                    .stream()
                    .findFirst()
                    .map(item -> item.getSaleItem().getUser())
                    .orElseThrow(() -> new ResourceNotFoundException("Seller not found for this order"));
            dto.setBuyerId(order.getUser().getId());
            dto.setSeller(modelMapper.map(seller, SellerDto.class));
            dto.getOrderItems().forEach(item -> item.setNo(order.getId()));
        }
        return dtos;
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
                int rowsDeleted = cartItemRepository.deleteByUserAndSaleItem(request.getBuyerId(), item.getSaleItemId());
                if (rowsDeleted == 0) {
                    throw new ResourceNotFoundException("Cart item with sale item id " + item.getSaleItemId() + " not found.");
                }
            }
            OrderResponseDto response = modelMapper.map(order, OrderResponseDto.class);
            response.setBuyerId(order.getUser().getId());
            User seller = userRepository.findById(request.getSellerId()).orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
            response.setSeller(modelMapper.map(seller, SellerDto.class));
            response.getOrderItems().forEach(item -> item.setNo(order.getId()));
            responses.add(response);
        }
        return responses;
    }

    public Order getOrderById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for id " + id));

        AuthUserDetail userDetail = (AuthUserDetail) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Integer tokenUserId = userDetail.getId();
        if (!Objects.equals(order.getUser().getId(), tokenUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Request user id not matched to order");
        }
        return order;
    }

    public OrderDto getOrderResponseById(Integer id) {
        Order order = getOrderById(id);

        User seller = order.getOrderItems()
                .stream()
                .findFirst()
                .map(item -> item.getSaleItem().getUser())
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found for this order"));

        OrderDto dto = modelMapper.map(order, OrderDto.class);
        SellerOrderDto sellerDto = modelMapper.map(seller, SellerOrderDto.class);

        dto.setSeller(sellerDto);
        dto.setBuyerId(order.getUser().getId());
        dto.getOrderItems().forEach(item -> item.setNo(order.getId()));

        return dto;
    }
}
