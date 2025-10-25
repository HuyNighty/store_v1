package com.ecommerce.store.service.impl.model_impl;

import com.ecommerce.store.dto.request.model_request.CartItemRequest;
import com.ecommerce.store.dto.response.model_response.CartItemResponse;
import com.ecommerce.store.entity.Cart;
import com.ecommerce.store.entity.CartItem;
import com.ecommerce.store.entity.Product;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.mapper.model_map.CartItemMapper;
import com.ecommerce.store.repository.CartItemRepository;
import com.ecommerce.store.repository.CartRepository;
import com.ecommerce.store.repository.ProductRepository;
import com.ecommerce.store.service.model_service.CartItemService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartItemServiceImpl implements CartItemService {

    CartItemRepository cartItemRepository;
    CartRepository cartRepository;
    ProductRepository productRepository;
    CartItemMapper cartItemMapper;

    @Override
    @Transactional
    public CartItemResponse addItemToCartForUser(Jwt jwt, CartItemRequest request) {

        Cart cart = getCartOfCurrentUser(jwt);
        Product product = getActiveProduct(request.productId());

        validateStockQuantity(product, request.quantity());

        CartItem cartItem = cartItemRepository
                .findByCartCartIdAndProductProductId(cart.getCartId(), product.getProductId())
                .map(existingItem -> {
                    int newQuantity = existingItem.getQuantity() + request.quantity();
                    validateStockQuantity(product, newQuantity);

                    existingItem.setQuantity(newQuantity);
                    updateUnitPrice(existingItem, product);
                    existingItem.setUpdatedAt(LocalDateTime.now());
                    return existingItem;
                })
                .orElseGet(() -> {
                    CartItem newItem = createNewCartItem(cart, product, request.quantity());
                    log.info("Created new cart item");
                    return newItem;
                });

        CartItem savedItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toResponse(savedItem);
    }

    @Override
    @Transactional
    public CartItemResponse updateItemQuantityForUser(Jwt jwt, Integer productId, Integer newQuantity) {

        if (newQuantity < 0) {
            throw new AppException(ErrorCode.INVALID_QUANTITY);
        }

        Cart cart = getCartOfCurrentUser(jwt);
        Product product = getActiveProduct(productId);

        if (newQuantity == 0) {
            removeItemFromCartForUser(jwt, productId);
            throw new AppException(ErrorCode.CART_ITEM_REMOVED);
        }

        validateStockQuantity(product, newQuantity);

        CartItem cartItem = getCartItem(cart.getCartId(), productId);
        cartItem.setQuantity(newQuantity);
        updateUnitPrice(cartItem, product);
        cartItem.setUpdatedAt(LocalDateTime.now());

        CartItem updatedItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toResponse(updatedItem);
    }

    @Override
    @Transactional
    public void removeItemFromCartForUser(Jwt jwt, Integer productId) {
        Cart cart = getCartOfCurrentUser(jwt);
        CartItem cartItem = getCartItem(cart.getCartId(), productId);
        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional
    public void removeItemFromCart(Integer cartId, Integer productId) {
        CartItem cartItem = getCartItem(cartId, productId);
        cartItemRepository.delete(cartItem);
    }

    @Override
    public List<CartItemResponse> getItemsForUser(Jwt jwt) {

        Cart cart = getCartOfCurrentUser(jwt);
        List<CartItem> cartItems = cartItemRepository.findByCartCartId(cart.getCartId());

        return cartItemMapper.toResponseList(cartItems);
    }

    @Override
    public List<CartItemResponse> getItemsByCartId(Integer cartId) {

        List<CartItem> cartItems = cartItemRepository.findByCartCartId(cartId);

        return cartItemMapper.toResponseList(cartItems);
    }

    @Override
    @Transactional
    public void clearCartForUser(Jwt jwt) {

        Cart cart = getCartOfCurrentUser(jwt);
        cartItemRepository.deleteByCartCartId(cart.getCartId());
    }


    private Cart getCartOfCurrentUser(Jwt jwt) {
        String userId = jwt.getClaimAsString("id");

        return cartRepository.findByUserUserId(userId)
                .orElseThrow(() -> {
                    return new AppException(ErrorCode.CART_NOT_FOUND);
                });
    }

    private Product getActiveProduct(Integer productId) {
        return productRepository.findActiveProductWithDetails(productId)
                .orElseThrow(() -> {
                    return new AppException(ErrorCode.PRODUCT_NOT_FOUND);
                });
    }

    private CartItem getCartItem(Integer cartId, Integer productId) {

        return cartItemRepository.findByCartCartIdAndProductProductId(cartId, productId)
                .orElseThrow(() -> {
                    return new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
                });
    }

    private CartItem createNewCartItem(Cart cart, Product product, Integer quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        updateUnitPrice(cartItem, product);
        return cartItem;
    }

    private void updateUnitPrice(CartItem cartItem, Product product) {
        BigDecimal currentPrice = product.getSalePrice() != null ? product.getSalePrice() : product.getPrice();
        cartItem.setUnitPrice(currentPrice);
    }

    private void validateStockQuantity(Product product, Integer requestedQuantity) {
        if (product.getStockQuantity() < requestedQuantity) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }
    }
}