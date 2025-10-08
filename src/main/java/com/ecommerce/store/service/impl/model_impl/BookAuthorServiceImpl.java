package com.ecommerce.store.service.impl.model_impl;

import com.ecommerce.store.dto.request.model_request.BookAuthorRequest;
import com.ecommerce.store.dto.response.model_response.BookAuthorResponse;
import com.ecommerce.store.entity.Author;
import com.ecommerce.store.entity.BookAuthor;
import com.ecommerce.store.entity.Product;
import com.ecommerce.store.entity.key.BookAuthorId;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.mapper.model_map.BookAuthorMapper;
import com.ecommerce.store.repository.AuthorRepository;
import com.ecommerce.store.repository.BookAuthorRepository;
import com.ecommerce.store.repository.ProductRepository;
import com.ecommerce.store.service.model_service.BookAuthorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookAuthorServiceImpl implements BookAuthorService {

    BookAuthorRepository bookAuthorRepository;
    ProductRepository productRepository;
    AuthorRepository authorRepository;
    BookAuthorMapper bookAuthorMapper;

    @Override
    public BookAuthorResponse create(BookAuthorRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Author author = authorRepository.findById(request.authorId())
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));

        BookAuthorId id = new BookAuthorId(product.getProductId(), author.getAuthorId());

        BookAuthor bookAuthor = BookAuthor
                .builder()
                .bookAuthorId(id)
                .author(author)
                .product(product)
                .authorRole(request.authorRole())
                .build();

        bookAuthorRepository.save(bookAuthor);
        return bookAuthorMapper.toResponse(bookAuthor);
    }

    @Override
    public BookAuthorResponse getById(Integer productId, Integer authorId) {
        BookAuthorId id = new BookAuthorId(productId, authorId);
        BookAuthor entity = bookAuthorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_AUTHOR_NOT_FOUND));
        return bookAuthorMapper.toResponse(entity);
    }

    @Override
    public List<BookAuthorResponse> getAll() {
        return bookAuthorRepository.findAll()
                .stream()
                .map(bookAuthorMapper::toResponse)
                .toList();
    }

    @Override
    public List<BookAuthorResponse> getByAuthorNameContaining(String keyword) {
        return bookAuthorRepository.findByAuthorAuthorNameContainingIgnoreCase(keyword)
                .stream()
                .map(bookAuthorMapper::toResponse)
                .toList();
    }

    @Override
    public List<BookAuthorResponse> getByProductId(Integer productId) {
        return bookAuthorRepository.findByProductProductId(productId)
                .stream()
                .map(bookAuthorMapper::toResponse)
                .toList();
    }

    @Override
    public BookAuthorResponse update(BookAuthorId id, BookAuthorRequest request) {
        BookAuthor bookAuthor = bookAuthorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_AUTHOR_NOT_FOUND));

        if (request.productId() != null) {
            Product product = productRepository.findById(request.productId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
            bookAuthor.setProduct(product);
        }

        if (request.authorId() != null) {
            Author author = authorRepository.findById(request.authorId())
                    .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));
            bookAuthor.setAuthor(author);
        }

        if (request.authorRole() != null) {
            bookAuthor.setAuthorRole(request.authorRole());
        }

        bookAuthor.setUpdatedAt(LocalDateTime.now());

        bookAuthorRepository.save(bookAuthor);
        return bookAuthorMapper.toResponse(bookAuthor);
    }


    @Override
    public void delete(Integer productId, Integer authorId) {
        BookAuthorId id = new BookAuthorId(productId, authorId);
        BookAuthor entity = bookAuthorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_AUTHOR_NOT_FOUND));
        bookAuthorRepository.delete(entity);
    }
}

