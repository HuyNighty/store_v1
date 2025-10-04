package com.ecomerce.store.dto.request.model_request;

import com.ecomerce.store.enums.entity_enums.OrderEnums.StatusOrder;

public record UpdateStatusOrderRequest(
        StatusOrder newStatusOrder
) {}
