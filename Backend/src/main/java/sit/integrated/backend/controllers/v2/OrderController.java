package sit.integrated.backend.controllers.v2;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.integrated.backend.dtos.OrderRequestDto;
import sit.integrated.backend.dtos.OrderResponseDto;
import sit.integrated.backend.services.OrderService;
import sit.integrated.backend.services.UserService;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/v2")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;

    @PostMapping("/orders")
    public ResponseEntity<List<OrderResponseDto>> placeOrders (@RequestBody List<OrderRequestDto> orderRequests) {
        List<OrderResponseDto> responses = orderService.placeOrders(orderRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getOrderResponseById(id));
    }
}
