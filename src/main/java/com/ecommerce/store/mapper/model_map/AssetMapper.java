package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.request.model_request.AssetRequest;
import com.ecommerce.store.dto.response.model_response.AssetResponse;
import com.ecommerce.store.entity.Asset;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AssetMapper {

    @Mapping(source = "type", target = "type")
    AssetResponse toAssetResponse(Asset asset);

    Asset toEntity(AssetRequest assetRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAssetFromRequest(AssetRequest request, @MappingTarget Asset asset);

}
