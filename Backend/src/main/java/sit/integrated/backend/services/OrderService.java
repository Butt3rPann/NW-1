package sit.integrated.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import sit.integrated.backend.entities.Order;
import sit.integrated.backend.repositories.OrderRepository;
import sit.integrated.backend.repositories.UserRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    public Page<Order> getOrdersByBuyer(Integer buyerId, String sortField, String sortDirection, Integer page, Integer size) {
        if (!userRepository.existsById(buyerId)) {
            throw new ResourceNotFoundException("User not found with id " + buyerId);
        }
        Sort sort = (sortField == null ? Sort.by("orderDate", "id") : Sort.by(Sort.Direction.fromString(sortDirection), sortField).and(Sort.by("id")));
        return orderRepository.findOrdersByUserId(buyerId, PageRequest.of(page, size, sort));
    }
}
