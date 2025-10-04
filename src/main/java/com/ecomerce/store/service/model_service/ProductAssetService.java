package com.ecomerce.store.service.model_service;

import com.ecomerce.store.dto.request.model_request.ProductAssetRequest;
import com.ecomerce.store.dto.response.model_response.ProductAssetResponse;
import com.ecomerce.store.enums.entity_enums.ProductAssetEnums.ProductAssetType;

import java.util.List;

public interface ProductAssetService {

    ProductAssetResponse addAssetToProduct(ProductAssetRequest request);

    void removeAssetFromProduct(ProductAssetRequest request);

    List<ProductAssetResponse> getAssetsByProduct(Integer productId);

    List<ProductAssetResponse> getAssetsByProductAssetType(ProductAssetType productAssetType);

    List<ProductAssetResponse> getAssetsByOrdinal(Integer ordinal);

    List<ProductAssetResponse> getAll();
}
