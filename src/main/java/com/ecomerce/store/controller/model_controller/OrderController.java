package com.ecomerce.store.controller.model_controller;

import com.ecomerce.store.dto.request.model_request.OrderRequest;
import com.ecomerce.store.dto.request.model_request.UpdateStatusOrderRequest;
import com.ecomerce.store.dto.response.ApiResponse;
import com.ecomerce.store.dto.response.model_response.OrderResponse;
import com.ecomerce.store.service.model_service.OrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService orderService;

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<OrderResponse> checkout(@RequestBody @Valid OrderRequest request,
                                               @AuthenticationPrincipal Jwt jwt) {
        return ApiResponse
                .<OrderResponse>builder()
                .result(orderService.createOrderFromCart(jwt, request))
                .build();
    }


    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<List<OrderResponse>> getMyOrders(@AuthenticationPrincipal Jwt jwt) {
        return ApiResponse
                .<List<OrderResponse>>builder()
                .result(orderService.getMyOrders(jwt))
                .build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me/{orderId}")
    public ApiResponse<OrderResponse> getOrderById(@PathVariable Integer orderId,
                                      @AuthenticationPrincipal Jwt jwt) {
        return ApiResponse
                .<OrderResponse>builder()
                .result(orderService.getOrderById(jwt, orderId))
                .build();
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/me/cancel/orders/{orderId}")
    public ApiResponse<Void> cancelOrder(@AuthenticationPrincipal Jwt jwt,
                                     @PathVariable Integer orderId) {
        orderService.cancelOrder(jwt, orderId);
        return ApiResponse
                .<Void>builder()
                .code(200)
                .message("Order has been cancelled")
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<OrderResponse>> getAllOrders() {
        return ApiResponse
                .<List<OrderResponse>>builder()
                .result(orderService.getAllOrders())
                .build();
    }

    @PatchMapping("/admin/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<OrderResponse> updateStatus(@PathVariable Integer orderId,
                                                   @RequestBody UpdateStatusOrderRequest request) {
        return ApiResponse
                .<OrderResponse>builder()
                .result(orderService.updateStatus(orderId, request))
                .build();
    }

    @GetMapping("/admin/users/{userId}/orders/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<OrderResponse> getOrderByUser(@PathVariable String userId,
                                                     @PathVariable Integer orderId) {
        return ApiResponse
                .<OrderResponse>builder()
                .result(orderService.getOrderRoleAdmin(orderId, userId))
                .build();

    }

    @GetMapping("/admin/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<OrderResponse>> getOrdersByUser(@PathVariable String userId) {
        return ApiResponse
                .<List<OrderResponse>>builder()
                .result(orderService.getOrdersByUserId(userId))
                .build();
    }
}
