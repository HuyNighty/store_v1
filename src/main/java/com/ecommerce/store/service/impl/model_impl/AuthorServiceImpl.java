package com.ecommerce.store.service.impl.model_impl;

import com.ecommerce.store.dto.request.model_request.AuthorRequest;
import com.ecommerce.store.dto.request.model_request.AuthorUpdateRequest;
import com.ecommerce.store.dto.response.model_response.AuthorDetailResponse;
import com.ecommerce.store.dto.response.model_response.AuthorResponse;
import com.ecommerce.store.dto.response.model_response.ProductResponse;
import com.ecommerce.store.entity.Asset;
import com.ecommerce.store.entity.Author;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.mapper.model_map.AuthorMapper;
import com.ecommerce.store.mapper.model_map.ProductMapper;
import com.ecommerce.store.repository.AssetRepository;
import com.ecommerce.store.repository.AuthorRepository;
import com.ecommerce.store.repository.ProductRepository;
import com.ecommerce.store.service.model_service.AuthorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorServiceImpl implements AuthorService {

    AuthorRepository authorRepository;
    AssetRepository assetRepository;
    ProductRepository productRepository;
    ProductMapper productMapper;
    AuthorMapper authorMapper;

    @Override
    public AuthorResponse create(AuthorRequest request) {
        if (request == null) {
            throw new AppException(ErrorCode.AUTHOR_NOT_FOUND);
        }

        if (authorRepository.existsByAuthorName(request.authorName())) {
            throw new AppException(ErrorCode.AUTHOR_EXISTED);
        }

        Author author = authorMapper.toEntity(request);

        if (request.assetId() != null) {
            Asset asset = assetRepository.findById(request.assetId())
                    .orElseThrow(() -> new AppException(ErrorCode.ASSET_NOT_FOUND));
            author.setAsset(asset);
            author.setPortraitUrl(null);
        } else if (request.portraitUrl() != null && !request.portraitUrl().isBlank()) {
            author.setPortraitUrl(request.portraitUrl());
        }

        author.setWikiUrl(request.wikiUrl());

        authorRepository.save(author);
        return authorMapper.toResponse(author);
    }

    @Override
    public AuthorResponse update(Integer authorId, AuthorUpdateRequest request) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));

        if (request == null) {
            throw new AppException(ErrorCode.AUTHOR_NOT_FOUND);
        }

        if (request.assetId() != null) {
            Asset asset = assetRepository.findById(request.assetId())
                    .orElseThrow(() -> new AppException(ErrorCode.ASSET_NOT_FOUND));
            author.setAsset(asset);
            author.setPortraitUrl(null);
        } else if (request.portraitUrl() != null) {
            author.setPortraitUrl(request.portraitUrl());
        }

        if (request.deathDate() != null) {
            author.setDeathDate(request.deathDate());
        }

        if (request.wikiUrl() != null) {
            author.setWikiUrl(request.wikiUrl());
        }

        author.setUpdatedAt(LocalDateTime.now());
        authorMapper.updateAuthorFromRequest(request, author);

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
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toResponse)
                .toList();
    }

    @Override
    public List<AuthorResponse> searchByName(String keyword) {
        List<Author> authors = authorRepository.findByAuthorNameContainingIgnoreCase(keyword)
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));
        return authors.stream()
                .map(authorMapper::toResponse)
                .toList();
    }

    @Override
    public AuthorDetailResponse getAuthorDetail(Integer authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));

        List<ProductResponse> books = productRepository.findByAuthorId(authorId)
                .stream()
                .map(productMapper::toProductResponse)
                .toList();

        Integer totalBooks = books.size();
        Double averageRating = calculateAverageRating(books);

        return AuthorDetailResponse.builder()
                .authorId(author.getAuthorId())
                .authorName(author.getAuthorName())
                .bio(author.getBio())
                .bornDate(author.getBornDate())
                .deathDate(author.getDeathDate())
                .nationality(author.getNationality())
                .portraitUrl(getAuthorPortraitUrl(author))
                .wikiUrl(author.getWikiUrl())
                .totalBooks(totalBooks)
                .averageRating(averageRating)
                .books(books)
                .build();
    }

    @Override
    public Object getAuthorBooks(Integer authorId) {
        List<ProductResponse> books = productRepository.findByAuthorId(authorId)
                .stream()
                .map(productMapper::toProductResponse)
                .toList();

        return Map.of(
                "authorId", authorId,
                "books", books,
                "total", books.size()
        );
    }

    @Override
    public List<AuthorResponse> getPopularAuthors() {
        return authorRepository.findPopularAuthors()
                .stream()
                .map(authorMapper::toResponse)
                .toList();
    }

    private String getAuthorPortraitUrl(Author author) {
        if (author == null) return "/images/default-author.jpg";
        if (author.getPortraitUrl() != null && !author.getPortraitUrl().isBlank()) {
            return author.getPortraitUrl();
        }
        if (author.getAsset() != null && author.getAsset().getUrl() != null && !author.getAsset().getUrl().isBlank()) {
            return author.getAsset().getUrl();
        }
        return "/images/default-author.jpg";
    }

    private Double calculateAverageRating(List<ProductResponse> books) {
        if (books == null || books.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;
        int count = 0;

        for (ProductResponse b : books) {
            try {
                Object val = null;
                Class<?> cls = b.getClass();
                String[] candidates = new String[] {
                        "getAverageRating", "averageRating", "getRating", "rating", "getAvgRating", "avgRating"
                };
                for (String m : candidates) {
                    try {
                        java.lang.reflect.Method method = cls.getMethod(m);
                        val = method.invoke(b);
                        if (val != null) break;
                    } catch (NoSuchMethodException ignored) {
                    }
                }
                if (val instanceof Number) {
                    sum += ((Number) val).doubleValue();
                    count++;
                } else if (val instanceof String) {
                    try {
                        sum += Double.parseDouble((String) val);
                        count++;
                    } catch (NumberFormatException ignored) {
                    }
                }
            } catch (Exception ignored) {
            }
        }

        if (count == 0) return 0.0;
        return sum / count;
    }
}
