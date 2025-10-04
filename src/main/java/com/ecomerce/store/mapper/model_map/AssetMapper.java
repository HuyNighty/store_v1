package com.ecomerce.store.mapper.model_map;

import com.ecomerce.store.dto.request.model_request.AssetRequest;
import com.ecomerce.store.dto.request.model_request.ProductRequest;
import com.ecomerce.store.dto.response.model_response.AssetResponse;
import com.ecomerce.store.entity.Asset;
import com.ecomerce.store.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AssetMapper {

    @Mapping(source = "type", target = "type")
    AssetResponse toAssetResponse(Asset asset);

    Asset toEntity(AssetRequest assetRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAssetFromRequest(AssetRequest request, @MappingTarget Asset asset);

}
