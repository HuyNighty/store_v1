package com.ecommerce.store.dto.request.model_request;

import com.ecommerce.store.enums.entity_enums.OrderEnums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record OrderRequest(
        @NotNull
        String shippingAddress,

        @Size(max = 2000)
        String note,

        @NotNull
        PaymentMethod paymentMethod
) {}
