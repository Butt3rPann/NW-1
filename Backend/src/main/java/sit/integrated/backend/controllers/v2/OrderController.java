package sit.integrated.backend.controllers.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.integrated.backend.dtos.OrderRequestDto;
import sit.integrated.backend.dtos.OrderResponseDto;
import sit.integrated.backend.entities.AuthUserDetail;
import sit.integrated.backend.services.OrderService;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/v2")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<List<OrderResponseDto>> placeOrders (@RequestBody List<OrderRequestDto> orderRequests) {
        List<OrderResponseDto> responses = orderService.placeOrders(orderRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }
}
