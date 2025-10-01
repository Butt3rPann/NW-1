package sit.integrated.backend.controllers.v2;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.integrated.backend.dtos.*;
import sit.integrated.backend.entities.AuthUserDetail;
import sit.integrated.backend.entities.Order;
import sit.integrated.backend.services.OrderService;
import sit.integrated.backend.services.UserService;
import sit.integrated.backend.utils.ListMapper;
import java.util.Objects;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/v2")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    @GetMapping("/users/{id}")
    public ResponseEntity<BuyerResponseDto> getUserProfile(@PathVariable Integer id) {
        Integer tokenUserId = ((AuthUserDetail) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()).getId();
        if (!Objects.equals(id, tokenUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Request user id not matched");
        }

        return ResponseEntity.ok(userService.getUserProfileById(id)) ;

    }

    @PutMapping("/users/{id}")
    public ResponseEntity<BuyerResponseDto> updateUserProfile(@PathVariable Integer id, @RequestBody UserProfileDto userProfileDto) {
        AuthUserDetail userDetail = (AuthUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tokenUserId = userDetail.getId();
        if (!Objects.equals(id, tokenUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Request user id not matched");
        }

        BuyerResponseDto updatedUser = userService.updateUserProfileById(id, userProfileDto);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/users/{id}/orders")
    public ResponseEntity<PageDto<OrderDto>> getAllUserOrders(@PathVariable Integer id,
                                                              @RequestParam(required = false) String sortField,
                                                              @RequestParam(required = false, defaultValue = "asc") String sortDirection,
                                                              @RequestParam Integer page,
                                                              @RequestParam(required = false, defaultValue = "10") Integer size) {
        AuthUserDetail userDetail = (AuthUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tokenUserId = userDetail.getId();
        if (!Objects.equals(id, tokenUserId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request user id not matched");
        }
        Page<Order> orders = orderService.getOrdersByBuyer(id, sortField, sortDirection, page, size);
        PageDto<OrderDto> dtos = listMapper.toPageDto(orders, OrderDto.class, modelMapper);
        dtos.getContent().forEach(order -> order.getOrderItems().forEach(item -> item.setNo(order.getId())));
        return ResponseEntity.ok(dtos);
    }
}
