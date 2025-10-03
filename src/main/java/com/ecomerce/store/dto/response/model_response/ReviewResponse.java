package com.ecomerce.store.dto.response.model_response;

import java.time.LocalDateTime;

public record ReviewResponse(
        Integer reviewId,
        Integer productId,
        String productName,
        String userId,
        String userName,
        Float rating,
        String comment,
        Boolean isApproved,
        LocalDateTime approvedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
