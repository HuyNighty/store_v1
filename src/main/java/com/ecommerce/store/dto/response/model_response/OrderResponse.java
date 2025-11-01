package com.ecommerce.store.dto.response.model_response;

import com.ecommerce.store.enums.entity_enums.OrderEnums.PaymentMethod;
import com.ecommerce.store.enums.entity_enums.OrderEnums.StatusOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Integer orderId,
        String orderNumber,
        StatusOrder statusOrder,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        String note,
        String shippingAddress,
        BigDecimal shippingCost,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime canceledAt,
        LocalDateTime completedAt,
        LocalDateTime deliveredAt,
        String userId,
        String customerId,
        String fullName,
        List<OrderItemResponse> orderItems
) {
}
