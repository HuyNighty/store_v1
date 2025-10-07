package com.ecommerce.store.dto.request.model_request;

public record CategoryRequest(
        Integer parentId,
        String categoryName,
        String slug,
        Boolean isActive,
        String description
) {
}
