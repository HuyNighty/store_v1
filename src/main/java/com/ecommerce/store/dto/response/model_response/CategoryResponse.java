package com.ecommerce.store.dto.response.model_response;

public record CategoryResponse(
        Integer categoryId,
        Integer parentId,
        String categoryName,
        String slug,
        Boolean isActive,
        String description
) {
}
