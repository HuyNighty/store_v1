package com.ecommerce.store.dto.request.model_request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CategoryRequest(

        @PositiveOrZero(message = "Parent ID must be >= 0")
        Integer parentId,

        @NotBlank(message = "Category name is required")
        String categoryName,

        @NotBlank(message = "Slug is required")
        @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, numbers, and dashes")
        String slug,

        Boolean isActive,

        @Size
        String description
) {}
