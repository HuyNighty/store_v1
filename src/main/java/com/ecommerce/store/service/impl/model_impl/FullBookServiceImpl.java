package com.ecommerce.store.service.impl.model_impl;

import com.ecommerce.store.dto.request.model_request.FullBookRequest;
import com.ecommerce.store.dto.request.model_request.FullBookUpdateRequest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    CategoryRepository categoryRepository;
    ProductCategoryRepository productCategoryRepository;

    FullBookMapper fullBookMapper;

    @Override
    public FullBookResponse createFullBook(FullBookRequest request) {
        if (productRepository.existsBySku(request.sku())) {
            throw new AppException(ErrorCode.PRODUCT_SKU_EXISTED);
        }

        if (productRepository.existsBySlug(request.slug())) {
            throw new AppException(ErrorCode.PRODUCT_SLUG_EXISTED);
        }

        List<Category> categories = validateAndGetCategories(request.categoryIds());

        Product product = fullBookMapper.toProduct(request);
        product.setCreatedAt(LocalDateTime.now());
        Product savedProduct = productRepository.save(product);

        linkCategoriesToProduct(savedProduct, categories);

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

            return fullBookMapper.toFullResponse(savedProduct, categories, savedAsset, savedAuthor);

        } catch (Exception e) {
            log.error("Error creating full book", e);
            throw new AppException(ErrorCode.CREATE_BOOK_FAILED);
        }
    }

    @Override
    public FullBookResponse updateFullBook(Integer productId, FullBookUpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (request.sku() != null && !request.sku().isBlank() && !request.sku().equals(product.getSku())) {
            if (productRepository.existsBySku(request.sku())) {
                throw new AppException(ErrorCode.PRODUCT_SKU_EXISTED);
            }
            product.setSku(request.sku());
        }
        if (request.slug() != null && !request.slug().isBlank() && !request.slug().equals(product.getSlug())) {
            if (productRepository.existsBySlug(request.slug())) {
                throw new AppException(ErrorCode.PRODUCT_SLUG_EXISTED);
            }
            product.setSlug(request.slug());
        }

        if (request.productName() != null) product.setProductName(request.productName());
        if (request.price() != null) product.setPrice(request.price());
        if (request.salePrice() != null) product.setSalePrice(request.salePrice());
        if (request.stockQuantity() != null) product.setStockQuantity(request.stockQuantity());
        if (request.weightG() != null) product.setWeightG(request.weightG());
        if (request.isActive() != null) product.setIsActive(request.isActive());
        if (request.featured() != null) product.setFeatured(request.featured());

        List<Category> categoriesForResponse;
        if (request.categoryIds() != null) {
            List<Category> categories = validateAndGetCategories(request.categoryIds());
            List<ProductCategory> existingPCs = productCategoryRepository.findByProduct_ProductId(product.getProductId());
            if (existingPCs != null && !existingPCs.isEmpty()) {
                productCategoryRepository.deleteAll(existingPCs);
            }
            linkCategoriesToProduct(product, categories);
            categoriesForResponse = categories;
        } else {
            List<ProductCategory> existingPCs = productCategoryRepository.findByProduct_ProductId(product.getProductId());
            categoriesForResponse = existingPCs == null ? List.of() :
                    existingPCs.stream().map(ProductCategory::getCategory).collect(Collectors.toList());
        }

        Asset linkedAsset = null;
        Author linkedAuthor = null;

        if (request.url() != null && !request.url().isBlank()) {
            ProductAsset existingPA = productAssetRepository.findFirstByProduct_ProductIdAndType(product.getProductId(), ProductAssetType.MAIN);
            if (existingPA != null && existingPA.getAsset() != null) {
                Asset a = existingPA.getAsset();
                if (!request.url().equals(a.getUrl()) && assetRepository.existsByUrl(request.url())) {
                    throw new AppException(ErrorCode.URL_EXISTED);
                }
                a.setUrl(request.url());
                if (request.fileName() != null) a.setFileName(request.fileName());
                if (request.mimeType() != null) a.setMimeType(request.mimeType());
                if (request.width() != null) a.setWidth(request.width());
                if (request.height() != null) a.setHeight(request.height());
                if (request.sizeBytes() != null) a.setSizeBytes(request.sizeBytes());
                a.setUpdatedAt(LocalDateTime.now());
                linkedAsset = assetRepository.save(a);
            } else {
                if (assetRepository.existsByUrl(request.url())) {
                    throw new AppException(ErrorCode.URL_EXISTED);
                }
                linkedAsset = createAndLinkAsset(request, product);
            }
        } else {
            ProductAsset existingPA = productAssetRepository.findFirstByProduct_ProductIdAndType(product.getProductId(), ProductAssetType.MAIN);
            if (existingPA != null) linkedAsset = existingPA.getAsset();
        }

        if (request.authorId() != null || (request.authorName() != null && !request.authorName().isBlank())) {
            List<BookAuthor> existingBAs = bookAuthorRepository.findByProduct_ProductId(product.getProductId());
            if (existingBAs != null && !existingBAs.isEmpty()) {
                bookAuthorRepository.deleteAll(existingBAs);
            }

            if (request.authorId() != null) {
                linkedAuthor = authorRepository.findById(request.authorId())
                        .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));
                BookAuthorId baId = BookAuthorId.builder()
                        .productId(product.getProductId())
                        .authorId(linkedAuthor.getAuthorId())
                        .build();
                if (!bookAuthorRepository.existsById(baId)) {
                    BookAuthor ba = BookAuthor.builder()
                            .bookAuthorId(baId)
                            .product(product)
                            .author(linkedAuthor)
                            .authorRole(AuthorRole.CO_AUTHOR)
                            .build();
                    bookAuthorRepository.save(ba);
                }
            } else {
                Author found = authorRepository.findByAuthorName(request.authorName()).orElse(null);
                if (found != null) {
                    linkedAuthor = found;
                } else {
                    Author newAuthor = fullBookMapper.toAuthor(request);
                    newAuthor.setCreatedAt(LocalDateTime.now());
                    if (linkedAsset != null) newAuthor.setAsset(linkedAsset);
                    if (newAuthor.getNationality() == null) newAuthor.setNationality(Nationality.OTHER);
                    linkedAuthor = authorRepository.save(newAuthor);
                }

                BookAuthorId baId = BookAuthorId.builder()
                        .productId(product.getProductId())
                        .authorId(linkedAuthor.getAuthorId())
                        .build();
                if (!bookAuthorRepository.existsById(baId)) {
                    BookAuthor ba = BookAuthor.builder()
                            .bookAuthorId(baId)
                            .product(product)
                            .author(linkedAuthor)
                            .authorRole(AuthorRole.CO_AUTHOR)
                            .build();
                    bookAuthorRepository.save(ba);
                }
            }
        } else {
            List<BookAuthor> existingBAs = bookAuthorRepository.findByProduct_ProductId(product.getProductId());
            if (existingBAs != null && !existingBAs.isEmpty()) {
                linkedAuthor = existingBAs.get(0).getAuthor();
            }
        }

        product.setUpdatedAt(LocalDateTime.now());
        Product updatedProduct = productRepository.save(product);

        return fullBookMapper.toFullResponse(updatedProduct, categoriesForResponse, linkedAsset, linkedAuthor);
    }

    private List<Category> validateAndGetCategories(List<Integer> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_REQUIRED);
        }

        List<Category> categories = categoryRepository.findAllById(categoryIds);

        if (categories.size() != categoryIds.size()) {
            List<Integer> foundIds = categories.stream()
                    .map(Category::getCategoryId)
                    .toList();

            List<Integer> missingIds = categoryIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        List<Category> inactiveCategories = categories.stream()
                .filter(category -> !category.getIsActive())
                .toList();

        if (!inactiveCategories.isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_INACTIVE);
        }

        return categories;
    }

    private void linkCategoriesToProduct(Product product, List<Category> categories) {
        List<ProductCategory> productCategories = new ArrayList<>();

        for (Category category : categories) {
            ProductCategory productCategory = ProductCategory.create(product, category);
            productCategories.add(productCategory);
        }

        productCategoryRepository.saveAll(productCategories);
    }

    private Asset createAndLinkAsset(FullBookRequest request, Product product) {
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

        return savedAsset;
    }

    private Asset createAndLinkAsset(FullBookUpdateRequest request, Product product) {
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

        return savedAuthor;
    }

    private Author createAndLinkAuthor(FullBookUpdateRequest request, Product product, Asset asset) {
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

        return savedAuthor;
    }
}
