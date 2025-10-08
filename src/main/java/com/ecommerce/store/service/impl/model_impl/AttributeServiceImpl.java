package com.ecommerce.store.service.impl.model_impl;

import com.ecommerce.store.dto.request.model_request.AttributeRequest;
import com.ecommerce.store.dto.response.model_response.AttributeResponse;
import com.ecommerce.store.entity.Attribute;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.mapper.model_map.AttributeMapper;
import com.ecommerce.store.repository.AttributeRepository;
import com.ecommerce.store.service.model_service.AttributeService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Transactional
public class AttributeServiceImpl implements AttributeService {

    AttributeRepository attributeRepository;
    AttributeMapper attributeMapper;

    @Override
    public AttributeResponse create(AttributeRequest request) {
        if (attributeRepository.existsByAttributeNameIgnoreCase(request.attributeName()))
            throw new AppException(ErrorCode.DUPLICATE_ATTRIBUTE_NAME);

        Attribute entity = attributeMapper.toEntity(request);
        attributeRepository.save(entity);
        return attributeMapper.toResponse(entity);
    }

    @Override
    public AttributeResponse update(Integer id, AttributeRequest request) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));

        attributeMapper.updateAttributeFromRequest(request, attribute);
        attribute.setUpdatedAt(LocalDateTime.now());

        attributeRepository.save(attribute);
        return attributeMapper.toResponse(attribute);
    }

    @Override
    public void delete(Integer id) {
        Attribute entity = attributeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));
        attributeRepository.delete(entity);
    }

    @Override
    public AttributeResponse getById(Integer id) {
        Attribute entity = attributeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));
        return attributeMapper.toResponse(entity);
    }

    @Override
    public List<AttributeResponse> getAll() {
        return attributeRepository.findAll()
                .stream()
                .map(attributeMapper::toResponse)
                .toList();
    }

    @Override
    public List<AttributeResponse> getByNameContaining(String keyword) {
        return attributeRepository.findByAttributeNameContainingIgnoreCase(keyword)
                .stream()
                .map(attributeMapper::toResponse)
                .toList();
    }
}

