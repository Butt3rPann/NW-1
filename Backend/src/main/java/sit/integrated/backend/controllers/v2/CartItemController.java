package sit.integrated.backend.controllers.v2;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.integrated.backend.dtos.*;
import sit.integrated.backend.services.CartItemService;
import sit.integrated.backend.services.UserService;

@RestController
@RequestMapping("/v2")
public class CartItemController {
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;

    @PostMapping("/carts")
    public ResponseEntity<CartResponseDto> addToCart(@Valid @RequestBody CartRequestDto cartRequestDto) {
        CartResponseDto response = modelMapper.map(cartItemService.addToCart(cartRequestDto), CartResponseDto.class);
        response.setSeller(modelMapper.map(userService.getUserBySaleItemId(cartRequestDto.getSaleItemId()), UserDto.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/carts/{id}")
    public ResponseEntity<CartResponseDto> updateCartItemQuantity(@PathVariable Integer id,
                                                                  @Valid @RequestBody UpdateCartItemQuantityDto updateCartItemQuantityDto) {
        CartResponseDto response = modelMapper.map(cartItemService.updateCartItemQuantity(id, updateCartItemQuantityDto.getQuantity()), CartResponseDto.class);
        response.setSeller(modelMapper.map(userService.getUserBySaleItemId(id), UserDto.class));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/carts/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Integer id) {
        cartItemService.deleteCartItem(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
