package com.ecomerce.store.service.impl.model_impl;

import com.ecomerce.store.dto.request.model_request.AssetRequest;
import com.ecomerce.store.dto.response.model_response.AssetResponse;
import com.ecomerce.store.entity.Asset;
import com.ecomerce.store.enums.entity_enums.AssetEnums.AssetType;
import com.ecomerce.store.enums.error.ErrorCode;
import com.ecomerce.store.exception.AppException;
import com.ecomerce.store.mapper.model_map.AssetMapper;
import com.ecomerce.store.repository.AssetRepository;
import com.ecomerce.store.service.model_service.AssetService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AssetServiceImpl implements AssetService {

    AssetRepository assetRepository;

    AssetMapper assetMapper;

    @Override
    public AssetResponse createAsset(AssetRequest request) {
        if (assetRepository.existsByUrl(request.url())) {
            throw new AppException(ErrorCode.URL_EXISTED);
        }

        Asset asset = assetMapper.toEntity(request);
        asset.setCreatedAt(LocalDateTime.now());
        assetRepository.save(asset);

        return assetMapper.toAssetResponse(asset);
    }

    @Override
    public AssetResponse updateAsset(Integer assetId, AssetRequest request) {
        Asset asset = getActiveAsset(assetId);

        if (request.url() != null && !request.url().isEmpty()) {
            boolean isExisted = assetRepository.existsByUrl(request.url());
            if (isExisted && !asset.getUrl().equalsIgnoreCase(request.url())) {
                throw new AppException(ErrorCode.URL_EXISTED);
            }
            asset.setUrl(request.url());
        }

        assetMapper.updateAssetFromRequest(request, asset);

        asset.setUpdatedAt(LocalDateTime.now());
        assetRepository.save(asset);

        return assetMapper.toAssetResponse(asset);
    }

    @Override
    public AssetResponse findById(Integer assetId) {
        Asset asset = getActiveAsset(assetId);
        return assetMapper.toAssetResponse(asset);
    }

    @Override
    public AssetResponse findByType(AssetType assetType) {
        Asset asset = assetRepository.findByType(assetType)
                .orElseThrow(() -> new AppException(ErrorCode.ASSET_NOT_FOUND));

        if (asset.getDeletedAt() != null) {
            throw new AppException(ErrorCode.ASSET_DELETED);
        }
        return assetMapper.toAssetResponse(asset);
    }

    @Override
    public List<AssetResponse> findAll() {
        List<Asset> assets = assetRepository.findAll();
        return assets
                .stream()
                .filter(asset -> asset.getDeletedAt() == null)
                .map(assetMapper::toAssetResponse)
                .toList();
    }

    @Override
    public void softDelete(Integer assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSET_NOT_FOUND));

        asset.setDeletedAt(LocalDateTime.now());
        assetRepository.save(asset);
    }

    private Asset getActiveAsset(Integer assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSET_NOT_FOUND));

        if (asset.getDeletedAt() != null) {
            throw new AppException(ErrorCode.ASSET_DELETED);
        }

        return asset;
    }
}
