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
        Product product = getProduct(request.productId());

        CartItem cartItem = cartItemRepository
                .findByCartCartIdAndProductProductId(cart.getCartId(), product.getProductId())
                .orElseGet(() -> createNewCartItem(cart, product, request.quantity()));

        cartItem.setQuantity(cartItem.getQuantity() + request.quantity());
        cartItemRepository.save(cartItem);
        return cartItemMapper.toResponse(cartItem);
    }

    @Override
    @Transactional
    public CartItemResponse updateItemQuantityForUser(Jwt jwt, Integer productId, Integer newQuantity) {
        Cart cart = getCartOfCurrentUser(jwt);
        CartItem cartItem = getCartItem(cart.getCartId(), productId);
        cartItem.setQuantity(newQuantity);
        cartItemRepository.save(cartItem);
        return cartItemMapper.toResponse(cartItem);
    }

    @Override
    @Transactional
    public void removeItemFromCartForUser(Jwt jwt, Integer productId) {
        Cart cart = getCartOfCurrentUser(jwt);
        CartItem cartItem = getCartItem(cart.getCartId(), productId);
        cartItemRepository.delete(cartItem);
    }

    @Override
    public List<CartItemResponse> getItemsForUser(Jwt jwt) {
        Cart cart = getCartOfCurrentUser(jwt);
        return cartItemMapper.toResponseList(cartItemRepository.findByCartCartId(cart.getCartId()));
    }


    @Override
    @Transactional
    public void removeItemFromCart(Integer cartId, Integer productId) {
        CartItem cartItem = getCartItem(cartId, productId);
        cartItemRepository.delete(cartItem);
    }

    @Override
    public List<CartItemResponse> getItemsByCartId(Integer cartId) {
        return cartItemMapper.toResponseList(cartItemRepository.findByCartCartId(cartId));
    }


    private Cart getCartOfCurrentUser(Jwt jwt) {
        String userId = jwt.getClaimAsString("id");

        log.info("Current userId from JWT: {}", userId);

        return cartRepository.findByUserUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
    }

    private Product getProduct(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private CartItem createNewCartItem(Cart cart, Product product, Integer quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setUnitPrice(product.getPrice());
        cartItem.setQuantity(quantity);
        return cartItem;
    }

    private CartItem getCartItem(Integer cartId, Integer productId) {
        return cartItemRepository.findByCartCartIdAndProductProductId(cartId, productId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));
    }
}
