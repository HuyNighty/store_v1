package com.ecommerce.store.dto.response.model_response;

import lombok.Builder;

@Builder
public record CategoryResponse(
        Integer categoryId,
        Integer parentId,
        String categoryName,
        String slug,
        Boolean isActive,
        String description
) {
}
