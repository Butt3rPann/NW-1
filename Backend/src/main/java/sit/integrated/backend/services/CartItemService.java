package sit.integrated.backend.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sit.integrated.backend.dtos.CartItemDto;
import sit.integrated.backend.dtos.CartRequestDto;
import sit.integrated.backend.dtos.CartSellerWithItemsDto;
import sit.integrated.backend.dtos.SellerDto;
import sit.integrated.backend.entities.CartItem;
import sit.integrated.backend.entities.User;
import sit.integrated.backend.repositories.CartItemRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<CartSellerWithItemsDto> getAllCartItem(Integer userId) {
        List<CartItem> carts =  cartItemRepository.getCartItemsByUserId(userId);
        Map<User, List<CartItem>> itemsGroupBySeller = carts.stream().collect(Collectors.groupingBy(item -> item.getSaleItem().getUser()));
        return itemsGroupBySeller.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(User::getId)))
                .map(entry -> {
                    User seller = entry.getKey();
                    List<CartItem> items = entry.getValue();
                    CartSellerWithItemsDto dto = new CartSellerWithItemsDto();
                    dto.setSeller(modelMapper.map(seller, SellerDto.class));
                    dto.setCartItems(items.stream()
                            .sorted(Comparator.comparing(CartItem::getId))
                            .map(item -> modelMapper.map(item, CartItemDto.class))
                            .collect(Collectors.toList()));
                    return dto;
                }).collect(Collectors.toList());
    }

    public CartItem addToCart(CartRequestDto request) {
        CartItem cartItem = modelMapper.map(request, CartItem.class);
        Integer cartId = cartItemRepository.findIdByUserAndSaleItem(request.getUserId(), request.getSaleItemId());
        if (cartId != null) {
            cartItem.setId(cartId);
            int quantity = cartItemRepository.getQuantityById(cartId) + request.getQuantity();
            if (quantity > request.getMaxQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Exceeded the maximum quantity allowed for this product.");
            }
            cartItem.setQuantity(quantity);
        } else {
            cartItem.setId(null);
        }
        cartItemRepository.save(cartItem);
        return cartItem;
    }

    @Transactional
    public CartItem updateCartItemQuantity(Integer cartItemId, int newQuantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        if (newQuantity > cartItem.getMaxQuantity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Exceeded the maximum quantity allowed for this product.");
        }
        int rowsUpdated = cartItemRepository.updateQuantity(cartItemId, newQuantity);
        if (rowsUpdated == 0) {
            throw new ResourceNotFoundException("Cart item with id " + cartItemId + " not found.");
        }
        cartItem.setQuantity(newQuantity);
        return cartItem;
    }

    public CartItem getCartItemById(Integer cartItemId) {
        return cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
    }

    @Transactional
    public void deleteCartItem(Integer cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new ResourceNotFoundException("Cart item not found");
        }
        cartItemRepository.deleteById(cartItemId);
    }
}
