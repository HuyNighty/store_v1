package com.ecomerce.store.service.model_service;

import com.ecomerce.store.dto.request.model_request.ProductRequest;
import com.ecomerce.store.dto.response.model_response.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(Integer productId, ProductRequest request);
    void deleteProduct(Integer productId);
    ProductResponse getProductById(Integer productId);
    List<ProductResponse> getAllProducts();
    List<ProductResponse> searchProducts(String keyword);
}
