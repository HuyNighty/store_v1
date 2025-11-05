package com.ecommerce.store.dto.response.model_response;

import java.time.LocalDateTime;

public record ReviewResponse(
        Integer reviewId,
        Integer productId,
        String productName,
        String userId,
        String userName,
        String profileImage,
        String email,
        Float rating,
        String comment,
        Boolean isApproved,
        LocalDateTime approvedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
