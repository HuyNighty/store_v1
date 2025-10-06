package com.ecommerce.store.service.impl.model_impl;

import com.ecommerce.store.dto.request.model_request.AuthorRequest;
import com.ecommerce.store.dto.request.model_request.AuthorUpdateRequest;
import com.ecommerce.store.dto.response.model_response.AuthorResponse;
import com.ecommerce.store.entity.Asset;
import com.ecommerce.store.entity.Author;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.mapper.model_map.AuthorMapper;
import com.ecommerce.store.repository.AssetRepository;
import com.ecommerce.store.repository.AuthorRepository;
import com.ecommerce.store.service.model_service.AuthorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorServiceImpl implements AuthorService {

    AuthorRepository authorRepository;
    AssetRepository assetRepository;

    AuthorMapper authorMapper;

    @Override
    public AuthorResponse create(AuthorRequest request) {
        if (authorRepository.existsByAuthorName(request.authorName())) {
            throw new AppException(ErrorCode.AUTHOR_EXISTED);
        }

        Asset asset = assetRepository.findById(request.assetId())
                .orElseThrow(() -> new AppException(ErrorCode.ASSET_NOT_FOUND));

        Author author = authorMapper.toEntity(request);
        author.setAsset(asset);

        authorRepository.save(author);
        return authorMapper.toResponse(author);
    }

    @Override
    public AuthorResponse update(Integer authorId, AuthorUpdateRequest request) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));

        if (request.assetId() != null) {
            Asset asset = assetRepository.findById(request.assetId())
                    .orElseThrow(() -> new AppException(ErrorCode.ASSET_NOT_FOUND));
            author.setAsset(asset);
        }

        if (request.deathDate() != null && request.deathDate().toString().trim().isEmpty()) {
            author.setDeathDate(null);
        } else {
            author.setDeathDate(request.deathDate());
        }

        authorRepository.save(author);
        return authorMapper.toResponse(author);
    }

    @Override
    public void delete(Integer authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));

        author.setAsset(null);
        authorRepository.save(author);
        authorRepository.deleteById(author.getAuthorId());
    }

    @Override
    public AuthorResponse getById(Integer authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));
        return authorMapper.toResponse(author);
    }

    @Override
    public List<AuthorResponse> getAll() {
        return authorRepository
                .findAll()
                .stream().map(authorMapper::toResponse)
                .toList();    }

    @Override
    public List<AuthorResponse> searchByName(String keyword) {
        List<Author> authors = authorRepository.findByAuthorNameContainingIgnoreCase(keyword)
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));
        return authors
                .stream()
                .map(authorMapper::toResponse)
                .toList();    }
}
