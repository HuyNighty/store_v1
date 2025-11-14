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
        // Check duplicate SKU
        if (productRepository.existsBySku(request.sku())) {
            throw new AppException(ErrorCode.PRODUCT_SKU_EXISTED);
        }

        // Check duplicate slug
        if (productRepository.existsBySlug(request.slug())) {
            throw new AppException(ErrorCode.PRODUCT_SLUG_EXISTED);
        }

        // Validate categories (must provide)
        List<Category> categories = validateAndGetCategories(request.categoryIds());

        Product product = fullBookMapper.toProduct(request);
        product.setCreatedAt(LocalDateTime.now());
        Product savedProduct = productRepository.save(product);

        // Link categories
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
        // 1. Load existing product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        // 2. SKU/Slug check if provided & changed
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

        // 3. Update basic fields only if provided (partial update)
        if (request.productName() != null) product.setProductName(request.productName());
        if (request.price() != null) product.setPrice(request.price());
        if (request.salePrice() != null) product.setSalePrice(request.salePrice());
        if (request.stockQuantity() != null) product.setStockQuantity(request.stockQuantity());
        if (request.weightG() != null) product.setWeightG(request.weightG());
        if (request.isActive() != null) product.setIsActive(request.isActive());
        if (request.featured() != null) product.setFeatured(request.featured());

        // 4. Categories: only change when categoryIds provided (otherwise keep existing)
        List<Category> categoriesForResponse;
        if (request.categoryIds() != null) {
            // validate provided ids (may throw)
            List<Category> categories = validateAndGetCategories(request.categoryIds());
            // remove existing relations
            List<ProductCategory> existingPCs = productCategoryRepository.findByProduct_ProductId(product.getProductId());
            if (existingPCs != null && !existingPCs.isEmpty()) {
                productCategoryRepository.deleteAll(existingPCs);
            }
            // link new
            linkCategoriesToProduct(product, categories);
            categoriesForResponse = categories;
        } else {
            // keep existing categories for response
            List<ProductCategory> existingPCs = productCategoryRepository.findByProduct_ProductId(product.getProductId());
            categoriesForResponse = existingPCs == null ? List.of() :
                    existingPCs.stream().map(ProductCategory::getCategory).collect(Collectors.toList());
        }

        Asset linkedAsset = null;
        Author linkedAuthor = null;

        // 5. Asset handling:
        // - if url provided -> update existing MAIN asset or create new
        // - if url not provided -> keep existing main asset
        if (request.url() != null && !request.url().isBlank()) {
            ProductAsset existingPA = productAssetRepository.findFirstByProduct_ProductIdAndType(product.getProductId(), ProductAssetType.MAIN);
            if (existingPA != null && existingPA.getAsset() != null) {
                Asset a = existingPA.getAsset();
                // if url changed and exists elsewhere -> conflict
                if (!request.url().equals(a.getUrl()) && assetRepository.existsByUrl(request.url())) {
                    throw new AppException(ErrorCode.URL_EXISTED);
                }
                // update provided fields only
                a.setUrl(request.url());
                if (request.fileName() != null) a.setFileName(request.fileName());
                if (request.mimeType() != null) a.setMimeType(request.mimeType());
                if (request.width() != null) a.setWidth(request.width());
                if (request.height() != null) a.setHeight(request.height());
                if (request.sizeBytes() != null) a.setSizeBytes(request.sizeBytes());
                a.setUpdatedAt(LocalDateTime.now());
                linkedAsset = assetRepository.save(a);
            } else {
                // create new asset and link
                if (assetRepository.existsByUrl(request.url())) {
                    throw new AppException(ErrorCode.URL_EXISTED);
                }
                linkedAsset = createAndLinkAsset(request, product);
            }
        } else {
            // keep existing if any
            ProductAsset existingPA = productAssetRepository.findFirstByProduct_ProductIdAndType(product.getProductId(), ProductAssetType.MAIN);
            if (existingPA != null) linkedAsset = existingPA.getAsset();
        }

        // 6. Author handling:
        // - If authorId provided -> link to existing author (create BookAuthor if needed)
        // - Else if authorName provided -> find or create author, then link
        // - Else -> keep existing author relations untouched
        if (request.authorId() != null || (request.authorName() != null && !request.authorName().isBlank())) {
            // remove existing book-author relations first
            List<BookAuthor> existingBAs = bookAuthorRepository.findByProduct_ProductId(product.getProductId());
            if (existingBAs != null && !existingBAs.isEmpty()) {
                bookAuthorRepository.deleteAll(existingBAs);
            }

            if (request.authorId() != null) {
                linkedAuthor = authorRepository.findById(request.authorId())
                        .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));
                // create BookAuthor relation if not exists
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
                // authorName provided
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
            // request doesn't provide author info -> preserve existing author (if any)
            List<BookAuthor> existingBAs = bookAuthorRepository.findByProduct_ProductId(product.getProductId());
            if (existingBAs != null && !existingBAs.isEmpty()) {
                // pick first author for response (could be multiple)
                linkedAuthor = existingBAs.get(0).getAuthor();
            }
        }

        // 7. Update timestamps & save product
        product.setUpdatedAt(LocalDateTime.now());
        Product updatedProduct = productRepository.save(product);

        // 8. Return mapped response with categories (either updated or existing), linkedAsset and linkedAuthor
        return fullBookMapper.toFullResponse(updatedProduct, categoriesForResponse, linkedAsset, linkedAuthor);
    }

    private List<Category> validateAndGetCategories(List<Integer> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_REQUIRED);
        }

        List<Category> categories = categoryRepository.findAllById(categoryIds);

        // Kiểm tra xem tất cả categoryIds có tồn tại không
        if (categories.size() != categoryIds.size()) {
            List<Integer> foundIds = categories.stream()
                    .map(Category::getCategoryId)
                    .toList();

            List<Integer> missingIds = categoryIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        // Kiểm tra categories có active không
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

    // createAndLinkAsset for create-request
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

    // createAndLinkAsset overload for update-request
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

    // createAndLinkAuthor for create-request
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

    // createAndLinkAuthor overload for update-request
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
