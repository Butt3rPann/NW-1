package sit.integrated.backend.controllers.v2;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.integrated.backend.dtos.*;
import sit.integrated.backend.entities.AuthUserDetail;
import sit.integrated.backend.services.CartItemService;
import sit.integrated.backend.services.OrderService;
import sit.integrated.backend.services.UserService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v2")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartItemService cartItemService;

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
    public ResponseEntity<BuyerResponseDto> updateUserProfile(@PathVariable Integer id, @Valid  @RequestBody UserProfileDto userProfileDto) {
        AuthUserDetail userDetail = (AuthUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tokenUserId = userDetail.getId();
        if (!Objects.equals(id, tokenUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Request user id not matched");
        }

        BuyerResponseDto updatedUser = userService.updateUserProfileById(id, userProfileDto);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/users/{id}/orders")
    public ResponseEntity<PageDto<OrderResponseDto>> getAllUserOrders(@PathVariable Integer id,
                                                                      @RequestParam Integer page,
                                                                      @RequestParam(required = false, defaultValue = "10") Integer size) {
        AuthUserDetail userDetail = (AuthUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tokenUserId = userDetail.getId();
        if (!Objects.equals(id, tokenUserId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request user id not matched");
        }
        PageDto<OrderResponseDto> dtos = orderService.getOrdersByBuyer(id, page, size);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/users/{id}/carts")
    public ResponseEntity<List<CartSellerWithItemsDto>> getCartItems(@PathVariable Integer id) {
        return ResponseEntity.ok(cartItemService.getAllCartItem(id));
    }
}
