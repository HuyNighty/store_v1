package com.ecommerce.store.service.model_service;

import com.ecommerce.store.dto.request.model_request.ProductRequest;
import com.ecommerce.store.dto.response.model_response.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(Integer productId, ProductRequest request);
    void deleteProduct(Integer productId);
    ProductResponse getProductById(Integer productId);
    List<ProductResponse> getAllProducts();
    List<ProductResponse> searchProducts(String keyword);
    List<ProductResponse> getProductsByCategory(Integer categoryId);
    List<ProductResponse> getProductsByCategoryName(String categoryName);
    List<ProductResponse> filterProducts(Integer categoryId, java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice, Double minRating);
}
