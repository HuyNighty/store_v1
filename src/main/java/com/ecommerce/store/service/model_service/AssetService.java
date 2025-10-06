package com.ecommerce.store.service.model_service;

import com.ecommerce.store.dto.request.model_request.AssetRequest;
import com.ecommerce.store.dto.response.model_response.AssetResponse;
import com.ecommerce.store.enums.entity_enums.AssetEnums.AssetType;

import java.util.List;

public interface AssetService {

    AssetResponse createAsset(AssetRequest request);

    AssetResponse updateAsset(Integer assetId, AssetRequest request);

    AssetResponse findById(Integer assetId);

    AssetResponse findByType(AssetType assetType);

    List<AssetResponse> findAll();

    void softDelete(Integer assetId);
}
