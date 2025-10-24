package com.ecommerce.store.service.impl.model_impl;

import com.ecommerce.store.dto.request.model_request.FullBookRequest;
import com.ecommerce.store.dto.response.model_response.FullBookResponse;
import com.ecommerce.store.entity.*;
import com.ecommerce.store.entity.key.BookAuthorId;
import com.ecommerce.store.entity.key.ProductAssetId;
import com.ecommerce.store.enums.entity_enums.AuthorEnums.Nationality;
import com.ecommerce.store.enums.entity_enums.BookAuthorEnums.AuthorRole;
import com.ecommerce.store.enums.entity_enums.ProductAssetEnums.ProductAssetType;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.mapper.model_map.FullBookMapper;
import com.ecommerce.store.repository.*;
import com.ecommerce.store.service.model_service.FullBookService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FullBookServiceImpl implements FullBookService {

    ProductRepository productRepository;
    AssetRepository assetRepository;
    AuthorRepository authorRepository;
    ProductAssetRepository productAssetRepository;
    BookAuthorRepository bookAuthorRepository;

    FullBookMapper fullBookMapper;

    @Override
    public FullBookResponse createFullBook(FullBookRequest request) {
        // Check trùng product SKU
        if (productRepository.existsBySku(request.sku())) {
            throw new AppException(ErrorCode.PRODUCT_SKU_EXISTED);
        }

        // Check trùng product slug
        if (productRepository.existsBySlug(request.slug())) {
            throw new AppException(ErrorCode.PRODUCT_SLUG_EXISTED);
        }

        Product product = fullBookMapper.toProduct(request);
        product.setCreatedAt(LocalDateTime.now());
        Product savedProduct = productRepository.save(product);

        Asset savedAsset = null;
        Author savedAuthor = null;

        try {
            if (request.url() != null && !request.url().isBlank()) {
                savedAsset = createAndLinkAsset(request, savedProduct);
            } else {
                log.info("Skipping asset creation - no URL provided");
            }

            if (request.authorName() != null && !request.authorName().isBlank()) {
                savedAuthor = createAndLinkAuthor(request, savedProduct, savedAsset);
            } else {
                log.info("Skipping author creation - no authorName provided");
            }

            FullBookResponse response = fullBookMapper.toFullResponse(savedProduct, savedAsset, savedAuthor);
            return response;

        } catch (Exception e) {
            log.error("Failed to create full book: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.CREATE_BOOK_FAILED);
        }
    }

    private Asset createAndLinkAsset(FullBookRequest request, Product product) {
        // Check trùng URL asset
        if (assetRepository.existsByUrl(request.url())) {
            throw new AppException(ErrorCode.URL_EXISTED);
        }

        Asset asset = fullBookMapper.toAsset(request);
        asset.setCreatedAt(LocalDateTime.now());
        Asset savedAsset = assetRepository.save(asset);

        ProductAssetId productAssetId = ProductAssetId.builder()
                .productId(product.getProductId())
                .assetId(savedAsset.getAssetId())
                .build();

        // Check trùng product-asset relationship
        if (productAssetRepository.existsById(productAssetId)) {
            throw new AppException(ErrorCode.PRODUCT_ASSET_EXISTED);
        }

        ProductAsset productAsset = ProductAsset.builder()
                .productAssetId(productAssetId)
                .product(product)
                .asset(savedAsset)
                .type(ProductAssetType.MAIN)
                .ordinal(0)
                .build();

        productAssetRepository.save(productAsset);
        log.info("Successfully created and linked asset for product: {}", product.getProductId());

        return savedAsset;
    }

    private Author createAndLinkAuthor(FullBookRequest request, Product product, Asset asset) {
        if (authorRepository.existsByAuthorName(request.authorName())) {
            throw new AppException(ErrorCode.AUTHOR_EXISTED);
        }

        Author author = fullBookMapper.toAuthor(request);
        author.setCreatedAt(LocalDateTime.now());

        if (asset != null) {
            author.setAsset(asset);
        }

        if (author.getNationality() == null) {
            author.setNationality(Nationality.OTHER);
        }

        Author savedAuthor = authorRepository.save(author);

        BookAuthorId bookAuthorId = BookAuthorId.builder()
                .productId(product.getProductId())
                .authorId(savedAuthor.getAuthorId())
                .build();

        // Check trùng book-author relationship
        if (bookAuthorRepository.existsById(bookAuthorId)) {
            throw new AppException(ErrorCode.BOOK_AUTHOR_EXISTED);
        }

        BookAuthor bookAuthor = BookAuthor.builder()
                .bookAuthorId(bookAuthorId)
                .product(product)
                .author(savedAuthor)
                .authorRole(AuthorRole.CO_AUTHOR)
                .build();

        bookAuthorRepository.save(bookAuthor);
        log.info("Successfully created and linked author for product: {}", product.getProductId());

        return savedAuthor;
    }
}