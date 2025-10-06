package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.request.model_request.ProductRequest;
import com.ecommerce.store.dto.response.model_response.ProductResponse;
import com.ecommerce.store.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductMapper {

    Product toProduct(ProductRequest request);
    ProductResponse toProductResponse(Product product);
    void updateProductFromRequest(ProductRequest request, @MappingTarget Product product);

}
