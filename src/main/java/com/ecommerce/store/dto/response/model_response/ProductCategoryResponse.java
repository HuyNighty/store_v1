package com.ecommerce.store.dto.response.model_response;

public record ProductCategoryResponse(
        Integer productId,
        String productName,
        Integer categoryId,
        String categoryName
) {}
