package com.ecommerce.store.dto.request.model_request;

import com.ecommerce.store.enums.entity_enums.OrderEnums.StatusOrder;

public record UpdateStatusOrderRequest(
        StatusOrder newStatusOrder
) {}
