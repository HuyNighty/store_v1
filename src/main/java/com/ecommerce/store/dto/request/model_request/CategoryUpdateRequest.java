package com.ecommerce.store.dto.request.model_request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CategoryUpdateRequest(

        Integer parentId,
        String categoryName,
        String slug,
        Boolean isActive,
        String description
) {}
