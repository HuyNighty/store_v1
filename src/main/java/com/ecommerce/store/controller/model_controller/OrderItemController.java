package com.ecommerce.store.controller.model_controller;

import com.ecommerce.store.dto.request.model_request.OrderItemRequest;
import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.dto.response.model_response.OrderItemResponse;
import com.ecommerce.store.service.model_service.OrderItemService;
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
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderItemController {

    OrderItemService orderItemService;

    @PostMapping("/orders/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<OrderItemResponse> addItemToOrder(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Integer orderId,
            @RequestBody @Valid OrderItemRequest request) {
        return ApiResponse
                .<OrderItemResponse>builder()
                .result(orderItemService.addItemToOrder(jwt, orderId, request))
                .build();
    }

    @PutMapping("/{orderItemId}/quantity")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<OrderItemResponse> updateItemQuantity(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Integer orderItemId,
            @RequestParam Integer quantity) {
        return ApiResponse
                .<OrderItemResponse>builder()
                .result(orderItemService.updateItem(jwt, orderItemId, quantity))
                .build();
    }

    @DeleteMapping("/{orderItemId}")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<Void> removeItem(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Integer orderItemId) {
        orderItemService.removeItem(jwt, orderItemId);
        return ApiResponse
                .<Void>builder()
                .code(200)
                .message("Item removed successfully")
                .build();
    }

    @GetMapping("/orders/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<List<OrderItemResponse>> getOrderItems(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Integer orderId) {
        return ApiResponse
                .<List<OrderItemResponse>>builder()
                .result(orderItemService.getItemsByOrder(jwt, orderId))
                .build();
    }

    @GetMapping("/admin/orders/{orderId}/items")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<OrderItemResponse>> getOrderItemsAdmin(@PathVariable Integer orderId) {
        return ApiResponse
                .<List<OrderItemResponse>>builder()
                .result(orderItemService.getItemsByOrderAdmin(orderId))
                .build();
    }
}