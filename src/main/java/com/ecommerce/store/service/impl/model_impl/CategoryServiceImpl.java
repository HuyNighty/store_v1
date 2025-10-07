package com.ecommerce.store.service.impl.model_impl;

import com.ecommerce.store.dto.request.model_request.CategoryRequest;
import com.ecommerce.store.dto.response.model_response.CategoryResponse;
import com.ecommerce.store.entity.Category;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.mapper.model_map.CategoryMapper;
import com.ecommerce.store.repository.CategoryRepository;
import com.ecommerce.store.service.model_service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    @Override
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByCategoryNameIgnoreCase(request.categoryName()))
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);

        Category entity = categoryMapper.toEntity(request);

        if (entity.getIsActive() == null) {
            entity.setIsActive(true);
        }
        categoryRepository.save(entity);
        return categoryMapper.toResponse(entity);
    }

    @Override
    public CategoryResponse update(Integer id, CategoryRequest request) {
        Category entity = categoryRepository.findById(id)
                .filter(c -> c.getDeletedAt() == null)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        if (request.categoryName() != null &&
                categoryRepository.existsByCategoryNameIgnoreCaseAndCategoryIdNot(request.categoryName(), id)) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        categoryMapper.updateCategoryFromRequest(request, entity);
        entity.setUpdatedAt(LocalDateTime.now());
        categoryRepository.save(entity);
        return categoryMapper.toResponse(entity);
    }

    @Override
    public CategoryResponse getById(Integer id) {
        Category entity = categoryRepository.findById(id)
                .filter(c -> c.getDeletedAt() == null)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return categoryMapper.toResponse(entity);
    }

    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    public void softDelete(Integer id) {
        Category category = categoryRepository.findById(id)
                .filter(c -> c.getDeletedAt() == null)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        category.setDeletedAt(LocalDateTime.now());
        category.setIsActive(false);
        categoryRepository.save(category);
    }
}
