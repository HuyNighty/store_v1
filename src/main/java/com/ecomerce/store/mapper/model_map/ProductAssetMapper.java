package com.ecomerce.store.mapper.model_map;

import com.ecomerce.store.dto.request.model_request.ProductAssetRequest;
import com.ecomerce.store.dto.response.model_response.ProductAssetResponse;
import com.ecomerce.store.entity.ProductAsset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductAssetMapper {

    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "asset.assetId", target = "assetId")
    @Mapping(source = "asset.fileName", target = "fileName")
    @Mapping(source = "asset.url", target = "url")
    ProductAssetResponse toResponse(ProductAsset productAsset);

    ProductAsset toEntity(ProductAssetRequest productAssetRequest);
}
