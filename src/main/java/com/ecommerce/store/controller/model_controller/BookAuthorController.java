package com.ecommerce.store.controller.model_controller;

import com.ecommerce.store.dto.request.model_request.BookAuthorRequest;
import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.dto.response.model_response.BookAuthorResponse;
import com.ecommerce.store.entity.key.BookAuthorId;
import com.ecommerce.store.service.model_service.BookAuthorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-authors")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class BookAuthorController {

    BookAuthorService bookAuthorService;

    @PostMapping
    public ApiResponse<BookAuthorResponse> create(@RequestBody BookAuthorRequest request) {
        return ApiResponse
                .<BookAuthorResponse>builder()
                .result(bookAuthorService.create(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<BookAuthorResponse>> getAll() {
        return ApiResponse
                .<List<BookAuthorResponse>>builder()
                .result(bookAuthorService.getAll())
                .build();
    }

    @GetMapping("/search/{keyword}")
    public ApiResponse<List<BookAuthorResponse>> searchByAuthorName(@PathVariable String keyword) {
        return ApiResponse
                .<List<BookAuthorResponse>>builder()
                .result(bookAuthorService.getByAuthorNameContaining(keyword))
                .build();
    }

    @PatchMapping("/{productId}/{authorId}")
    public ApiResponse<BookAuthorResponse> update(
            @PathVariable Integer productId,
            @PathVariable Integer authorId,
            @RequestBody BookAuthorRequest request) {

        BookAuthorId id = new BookAuthorId(productId, authorId);
        BookAuthorResponse response = bookAuthorService.update(id, request);

        return ApiResponse
                .<BookAuthorResponse>builder()
                .code(200)
                .result(response)
                .build();
    }

    @DeleteMapping("/{productId}/{authorId}")
    public ApiResponse<Void> delete(@PathVariable Integer productId, @PathVariable Integer authorId) {
        bookAuthorService.delete(productId, authorId);
        return ApiResponse
                .<Void>builder()
                .code(200)
                .message("BookAuthor deleted successfully")
                .build();
    }
}

