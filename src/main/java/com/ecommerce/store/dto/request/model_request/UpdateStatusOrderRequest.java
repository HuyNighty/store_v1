package com.ecommerce.store.dto.request.model_request;

import com.ecommerce.store.enums.entity_enums.OrderEnums.StatusOrder;

import jakarta.validation.constraints.*;

public record UpdateStatusOrderRequest(

        @NotNull(message = "newStatusOrder is required")
        StatusOrder newStatusOrder
) {}
