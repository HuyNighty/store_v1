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

        return ApiResponse
                .<CartItemResponse>builder()
                .result(cartItemService.addItemToCartForUser(jwt, request))
                .build();
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/me/items/{productId}")
    public ApiResponse<CartItemResponse> updateItemQuantityForUser(
            @PathVariable Integer productId,
            @RequestBody @Valid UpdateCartItemQuantityRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        return ApiResponse
                .<CartItemResponse>builder()
                .result(cartItemService.updateItemQuantityForUser(jwt, productId, request.quantity()))
                .build();
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/me/items/{productId}")
    public ApiResponse<Void> removeItemFromCartForUser(
            @PathVariable Integer productId,
            @AuthenticationPrincipal Jwt jwt) {

        cartItemService.removeItemFromCartForUser(jwt, productId);
        return ApiResponse
                .<Void>builder()
                .code(200)
                .message("Removed item from cart successfully")
                .build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me/items")
    public ApiResponse<List<CartItemResponse>> getItemsForUser(@AuthenticationPrincipal Jwt jwt) {

        return ApiResponse
                .<List<CartItemResponse>>builder()
                .result(cartItemService.getItemsForUser(jwt))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{cartId}/items")
    public ApiResponse<List<CartItemResponse>> getItemsByCartId(@PathVariable Integer cartId) {
        return ApiResponse
                .<List<CartItemResponse>>builder()
                .result(cartItemService.getItemsByCartId(cartId))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{cartId}/items/{productId}")
    public ApiResponse<Void> removeItemFromCart(
            @PathVariable Integer cartId,
            @PathVariable Integer productId) {

        cartItemService.removeItemFromCart(cartId, productId);
        return ApiResponse
                .<Void>builder()
                .code(200)
                .message("Removed item from cart successfully")
                .build();
    }
}