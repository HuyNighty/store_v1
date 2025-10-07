package com.ecommerce.store.dto.request.model_request;

import com.ecommerce.store.enums.entity_enums.OrderEnums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record OrderRequest(
        @NotNull(message = "Shipping address is required")
        String shippingAddress,

        @Size(max = 2000)
        String note,

        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod
) {}
