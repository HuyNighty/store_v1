package com.ecommerce.store.service.impl.model_impl;

import com.ecommerce.store.dto.request.model_request.ProductCategoryRequest;
import com.ecommerce.store.dto.response.model_response.ProductCategoryResponse;
import com.ecommerce.store.entity.*;
import com.ecommerce.store.entity.key.ProductCategoryId;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.mapper.model_map.ProductCategoryMapper;
import com.ecommerce.store.repository.*;
import com.ecommerce.store.service.model_service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProductCategoryServiceImpl implements ProductCategoryService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductCategoryRepository productCategoryRepository;
    ProductCategoryMapper productCategoryMapper;

    @Override
    public ProductCategoryResponse assignProductToCategory(ProductCategoryRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

       ProductCategoryId productCategoryId = new ProductCategoryId(product.getProductId(),  category.getCategoryId());

       ProductCategory entity = ProductCategory
               .builder()
               .productCategoryId(productCategoryId)
               .category(category)
               .product(product)
               .build();

        productCategoryRepository.save(entity);
        return productCategoryMapper.toResponse(entity);
    }

    @Override
    public void removeProductFromCategory(Integer productId, Integer categoryId) {
        ProductCategoryId id = new ProductCategoryId(productId, categoryId);
        if (!productCategoryRepository.existsById(id)) {
            throw new AppException(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND);
        }
        productCategoryRepository.deleteById(id);
    }

    @Override
    public List<ProductCategoryResponse> getAll() {
        return productCategoryRepository.findAll()
                .stream()
                .map(productCategoryMapper::toResponse)
                .toList();
    }
}
