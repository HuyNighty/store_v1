package com.ecommerce.store.service.impl.model_impl;

import com.ecommerce.store.dto.request.model_request.ProductAttributeValueRequest;
import com.ecommerce.store.dto.response.model_response.ProductAttributeValueResponse;
import com.ecommerce.store.entity.Attribute;
import com.ecommerce.store.entity.Product;
import com.ecommerce.store.entity.ProductAttributeValue;
import com.ecommerce.store.entity.key.ProductAttributeValueId;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.mapper.model_map.ProductAttributeValueMapper;
import com.ecommerce.store.repository.AttributeRepository;
import com.ecommerce.store.repository.ProductAttributeRepository;
import com.ecommerce.store.repository.ProductRepository;
import com.ecommerce.store.service.model_service.ProductAttributeValueService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductAttributeValueServiceImpl implements ProductAttributeValueService {

    ProductAttributeRepository productAttributeValueRepository;
    ProductRepository productRepository;
    AttributeRepository attributeRepository;
    ProductAttributeValueMapper productAttributeValueMapper;

    @Override
    public ProductAttributeValueResponse create(ProductAttributeValueRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Attribute attribute = attributeRepository.findById(request.attributeId())
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));

        int count = 0;
        if (request.valueText() != null) count++;
        if (request.valueNumber() != null) count++;
        if (request.valueDate() != null) count++;

        if (count != 1) {
            throw new AppException(ErrorCode.INVALID_ATTRIBUTE_VALUE);
        }

        switch (attribute.getDataType()) {
            case TEXT -> {
                if (request.valueText() == null)
                    throw new AppException(ErrorCode.INVALID_ATTRIBUTE_TYPE);
            }
            case NUMBER -> {
                if (request.valueNumber() == null)
                    throw new AppException(ErrorCode.INVALID_ATTRIBUTE_TYPE);
            }
            case DATE -> {
                if (request.valueDate() == null)
                    throw new AppException(ErrorCode.INVALID_ATTRIBUTE_TYPE);
            }
        }
        ProductAttributeValueId id = new ProductAttributeValueId(product.getProductId(), attribute.getAttributeId());

        ProductAttributeValue entity = ProductAttributeValue.builder()
                .productAttributeValueId(id)
                .product(product)
                .attribute(attribute)
                .valueText(request.valueText())
                .valueNumber(request.valueNumber())
                .valueDate(request.valueDate())
                .build();

        productAttributeValueRepository.save(entity);
        return productAttributeValueMapper.toResponse(entity);
    }

    @Override
    public ProductAttributeValueResponse getById(Integer productId, Integer attributeId) {
        ProductAttributeValueId id = new ProductAttributeValueId(productId, attributeId);
        ProductAttributeValue entity = productAttributeValueRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_ATTRIBUTE_VALUE_NOT_FOUND));
        return productAttributeValueMapper.toResponse(entity);
    }

    @Override
    public List<ProductAttributeValueResponse> getAll() {
        return productAttributeValueRepository.findAll()
                .stream()
                .map(productAttributeValueMapper::toResponse)
                .toList();
    }

    @Override
    public ProductAttributeValueResponse update(ProductAttributeValueId id, ProductAttributeValueRequest request) {
        ProductAttributeValue entity = productAttributeValueRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_ATTRIBUTE_VALUE_NOT_FOUND));

        productAttributeValueMapper.updateFromRequest(request, entity);
        entity.setUpdatedAt(LocalDateTime.now());
        productAttributeValueRepository.save(entity);
        return productAttributeValueMapper.toResponse(entity);
    }

    @Override
    public void delete(Integer productId, Integer attributeId) {
        ProductAttributeValueId id = new ProductAttributeValueId(productId, attributeId);
        ProductAttributeValue entity = productAttributeValueRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_ATTRIBUTE_VALUE_NOT_FOUND));
        productAttributeValueRepository.delete(entity);
    }
}
