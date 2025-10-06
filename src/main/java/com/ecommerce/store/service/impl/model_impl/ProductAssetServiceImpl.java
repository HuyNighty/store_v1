package com.ecommerce.store.service.impl.model_impl;

import com.ecommerce.store.dto.request.model_request.ProductAssetRequest;
import com.ecommerce.store.dto.response.model_response.ProductAssetResponse;
import com.ecommerce.store.entity.Asset;
import com.ecommerce.store.entity.Product;
import com.ecommerce.store.entity.ProductAsset;
import com.ecommerce.store.entity.key.ProductAssetId;
import com.ecommerce.store.enums.entity_enums.ProductAssetEnums.ProductAssetType;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.mapper.model_map.ProductAssetMapper;
import com.ecommerce.store.repository.AssetRepository;
import com.ecommerce.store.repository.ProductAssetRepository;
import com.ecommerce.store.repository.ProductRepository;
import com.ecommerce.store.service.model_service.ProductAssetService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ProductAssetServiceImpl implements ProductAssetService {

    ProductRepository productRepository;
    AssetRepository assetRepository;
    ProductAssetRepository productAssetRepository;

    ProductAssetMapper productAssetMapper;

    @Override
    public ProductAssetResponse addAssetToProduct(ProductAssetRequest request) {
        Product product = productRepository.findById(request.productId())
                .filter(Product::getIsActive)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Asset asset = assetRepository.findById(request.assetId())
                .orElseThrow(() -> new AppException(ErrorCode.ASSET_NOT_FOUND));

        ProductAssetId productAssetId = new ProductAssetId(product.getProductId(), asset.getAssetId());

        boolean exists = productAssetRepository.existsById(productAssetId);
        if (exists) {
            throw new AppException(ErrorCode.PRODUCT_ASSET_EXISTED);
        }

        ProductAsset productAsset = ProductAsset
                .builder()
                .productAssetId(productAssetId)
                .product(product)
                .asset(asset)
                .ordinal(request.ordinal() != 0 ? request.ordinal() : 0)
                .type(request.type() != null ? request.type() : ProductAssetType.MAIN)
                .build();

        productAssetRepository.save(productAsset);

        return productAssetMapper.toResponse(productAsset);
    }

    @Override
    public List<ProductAssetResponse> getAssetsByProductAssetType(ProductAssetType productAssetType) {
        List<ProductAsset> assets = productAssetRepository.findByType(productAssetType);
        return assets
                .stream()
                .map(productAssetMapper::toResponse)
                .toList();
    }

    @Override
    public void removeAssetFromProduct(ProductAssetRequest request) {
        ProductAssetId id = new ProductAssetId(request.productId(), request.assetId());
        if (!productAssetRepository.existsById(id)) {
            throw new AppException(ErrorCode.PRODUCT_ASSET_NOT_FOUND);
        }
        productAssetRepository.deleteById(id);
    }

    @Override
    public List<ProductAssetResponse> getAll() {
        List<ProductAsset> productAssets = productAssetRepository.findAll();
        return productAssets
                .stream()
                .map(productAssetMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductAssetResponse> getAssetsByProduct(Integer productId) {
        List<ProductAsset> assets = productAssetRepository.findByProductProductId(productId);
        return assets
                .stream()
                .map(productAssetMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductAssetResponse> getAssetsByOrdinal(Integer ordinal) {
        List<ProductAsset> assets = productAssetRepository.findByOrdinal(ordinal);
        return assets
                .stream()
                .map(productAssetMapper::toResponse)
                .toList();
    }
}
