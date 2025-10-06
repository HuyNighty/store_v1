package com.ecomerce.store.service.impl.model_impl;

import com.ecomerce.store.dto.request.model_request.AuthorRequest;
import com.ecomerce.store.dto.request.model_request.AuthorUpdateRequest;
import com.ecomerce.store.dto.response.model_response.AuthorResponse;
import com.ecomerce.store.entity.Asset;
import com.ecomerce.store.entity.Author;
import com.ecomerce.store.enums.error.ErrorCode;
import com.ecomerce.store.exception.AppException;
import com.ecomerce.store.mapper.model_map.AuthorMapper;
import com.ecomerce.store.repository.AssetRepository;
import com.ecomerce.store.repository.AuthorRepository;
import com.ecomerce.store.service.model_service.AuthorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
