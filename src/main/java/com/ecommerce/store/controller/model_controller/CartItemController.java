package com.ecommerce.store.controller.model_controller;

import com.ecommerce.store.dto.request.model_request.CartItemRequest;
import com.ecommerce.store.dto.request.model_request.UpdateCartItemQuantityRequest;
import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.dto.response.model_response.CartItemResponse;
import com.ecommerce.store.service.model_service.CartItemService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartItemController {

    CartItemService cartItemService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/me/items")
    public ApiResponse<CartItemResponse> addItemToCartForUser(
            @RequestBody @Valid CartItemRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        try {
            CartItemResponse result = cartItemService.addItemToCartForUser(jwt, request);
            return ApiResponse.<CartItemResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Item added to cart successfully")
                    .result(result)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<CartItemResponse>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to add item to cart: " + e.getMessage())
                    .build();
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/me/items/{productId}")
    public ApiResponse<CartItemResponse> updateItemQuantityForUser(
            @PathVariable Integer productId,
            @RequestBody @Valid UpdateCartItemQuantityRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        try {
            CartItemResponse result = cartItemService.updateItemQuantityForUser(jwt, productId, request.quantity());
            return ApiResponse.<CartItemResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Cart item quantity updated successfully")
                    .result(result)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<CartItemResponse>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to update cart item: " + e.getMessage())
                    .build();
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/me/items/{productId}")
    public ApiResponse<Void> removeItemFromCartForUser(
            @PathVariable Integer productId,
            @AuthenticationPrincipal Jwt jwt) {

        try {
            cartItemService.removeItemFromCartForUser(jwt, productId);
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("Item removed from cart successfully")
                    .build();
        } catch (Exception e) {
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to remove item from cart: " + e.getMessage())
                    .build();
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me/items")
    public ApiResponse<List<CartItemResponse>> getItemsForUser(@AuthenticationPrincipal Jwt jwt) {

        try {
            List<CartItemResponse> result = cartItemService.getItemsForUser(jwt);
            return ApiResponse.<List<CartItemResponse>>builder()
                    .code(HttpStatus.OK.value())
                    .message("Cart items retrieved successfully")
                    .result(result)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<List<CartItemResponse>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to retrieve cart items: " + e.getMessage())
                    .build();
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/me/clear")
    public ApiResponse<Void> clearCartForUser(@AuthenticationPrincipal Jwt jwt) {

        try {
            cartItemService.clearCartForUser(jwt);
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("Cart cleared successfully")
                    .build();
        } catch (Exception e) {
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to clear cart: " + e.getMessage())
                    .build();
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me/items/count")
    public ApiResponse<Integer> getCartItemCountForUser(@AuthenticationPrincipal Jwt jwt) {

        try {
            List<CartItemResponse> cartItems = cartItemService.getItemsForUser(jwt);
            int totalCount = cartItems.stream()
                    .mapToInt(CartItemResponse::quantity)
                    .sum();
            return ApiResponse.<Integer>builder()
                    .code(HttpStatus.OK.value())
                    .message("Cart item count retrieved successfully")
                    .result(totalCount)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<Integer>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to retrieve cart item count: " + e.getMessage())
                    .build();
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{cartId}/items")
    public ApiResponse<List<CartItemResponse>> getItemsByCartId(@PathVariable Integer cartId) {

        try {
            List<CartItemResponse> result = cartItemService.getItemsByCartId(cartId);
            return ApiResponse.<List<CartItemResponse>>builder()
                    .code(HttpStatus.OK.value())
                    .message("Cart items retrieved successfully")
                    .result(result)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<List<CartItemResponse>>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to retrieve cart items: " + e.getMessage())
                    .build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{cartId}/items/{productId}")
    public ApiResponse<Void> removeItemFromCart(
            @PathVariable Integer cartId,
            @PathVariable Integer productId) {

        try {
            cartItemService.removeItemFromCart(cartId, productId);
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("Item removed from cart successfully")
                    .build();
        } catch (Exception e) {
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to remove item from cart: " + e.getMessage())
                    .build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{cartId}/clear")
    public ApiResponse<Void> clearCartByAdmin(@PathVariable Integer cartId) {

        try {
            List<CartItemResponse> items = cartItemService.getItemsByCartId(cartId);
            for (CartItemResponse item : items) {
                cartItemService.removeItemFromCart(cartId, item.productId());
            }
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("Cart cleared successfully")
                    .build();
        } catch (Exception e) {
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to clear cart: " + e.getMessage())
                    .build();
        }
    }
}