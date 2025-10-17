package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.request.model_request.ProductRequest;
import com.ecommerce.store.dto.response.model_response.ProductResponse;
import com.ecommerce.store.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {ProductAssetMapper.class, BookAuthorMapper.class}
)
public interface ProductMapper {

    @Mapping(target = "productId", ignore = true)
    Product toProduct(ProductRequest request);
    @AfterMapping
    default void setRelations(@MappingTarget Product product) {
        if (product.getProductAssets() != null) {
            product.getProductAssets().forEach(asset -> asset.setProduct(product));
        }

        if (product.getBookAuthors() != null) {
            product.getBookAuthors().forEach(pa -> pa.setProduct(product));
        }

    }
    ProductResponse toProductResponse(Product product);
    void updateProductFromRequest(ProductRequest request, @MappingTarget Product product);
}
