package com.ecommerce.store.service.impl.model_impl;

import com.ecommerce.store.dto.request.model_request.ProductRequest;
import com.ecommerce.store.dto.response.model_response.ProductResponse;
import com.ecommerce.store.entity.Product;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.mapper.model_map.ProductMapper;
import com.ecommerce.store.repository.ProductRepository;
import com.ecommerce.store.service.model_service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        if (productRepository.existsBySku(request.sku())) {
            throw new AppException(ErrorCode.PRODUCT_SKU_EXISTED);
        }

        if (request.slug() != null && productRepository.existsBySlug(request.slug())) {
            throw new AppException(ErrorCode.PRODUCT_SLUG_EXISTED);
        }

        Product product = productMapper.toProduct(request);
        productRepository.save(product);

        return productMapper.toProductResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Integer productId, ProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (request.slug() != null && !request.slug().isBlank()) {
            boolean isExisted = productRepository.existsBySlug(request.slug());
            if (isExisted && !product.getSlug().equalsIgnoreCase(request.slug())) {
                throw new AppException(ErrorCode.PRODUCT_SLUG_EXISTED);
            }
            product.setSlug(request.slug());
        }

        if (request.sku() != null && !request.sku().isBlank()) {
            boolean isExisted = productRepository.existsBySku(request.sku());
            if (isExisted && !product.getSku().equalsIgnoreCase(request.sku())) {
                throw new AppException(ErrorCode.PRODUCT_SKU_EXISTED);
            }
            product.setSku(request.sku());
        }

        productMapper.updateProductFromRequest(request, product);
        product.setUpdatedAt(LocalDateTime.now());

        productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    @Override
    public void deleteProduct(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        product.setDeletedAt(LocalDateTime.now());
        product.setIsActive(false);

        productRepository.save(product);
    }

    @Override
    public ProductResponse getProductById(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getDeletedAt() != null && !Boolean.TRUE.equals(product.getIsActive())) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        return productMapper.toProductResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
         return productRepository.findAll().stream()
                 .map(productMapper::toProductResponse)
                 .toList();
    }

    @Override
    public List<ProductResponse> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        return productRepository.findByProductNameContainingIgnoreCase(keyword.trim())
                .stream()
                .filter(p -> Boolean.TRUE.equals(p.getIsActive()))
                .map(productMapper::toProductResponse)
                .toList();
    }

    public List<ProductResponse> getProductsByCategory(Integer categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream()
                .map(productMapper::toProductResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    public List<ProductResponse> filterProducts(
            Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, Double minRating) {
        List<Product> products = productRepository.findWithFiltersAndRating(categoryId, minPrice, maxPrice, minRating);
        return products.stream()
                .map(productMapper::toProductResponse)
                .toList();
    }
}
