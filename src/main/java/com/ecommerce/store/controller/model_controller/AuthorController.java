package com.ecommerce.store.controller.model_controller;

import com.ecommerce.store.dto.request.model_request.AuthorRequest;
import com.ecommerce.store.dto.request.model_request.AuthorUpdateRequest;
import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.dto.response.model_response.AuthorResponse;
import com.ecommerce.store.dto.response.model_response.AuthorDetailResponse;
import com.ecommerce.store.service.model_service.AuthorService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorController {
    AuthorService authorService;

    @GetMapping("/public/{id}")
    public ApiResponse<AuthorDetailResponse> getAuthorDetail(@PathVariable("id") Integer authorId) {
        return ApiResponse.<AuthorDetailResponse>builder()
                .code(HttpStatus.OK.value())
                .result(authorService.getAuthorDetail(authorId))
                .build();
    }

    @GetMapping("/public/{id}/books")
    public ApiResponse<?> getAuthorBooks(@PathVariable("id") Integer authorId) {
        return ApiResponse.builder()
                .code(HttpStatus.OK.value())
                .result(authorService.getAuthorBooks(authorId))
                .build();
    }

    @GetMapping("/public/search")
    public ApiResponse<List<AuthorResponse>> searchAuthors(
            @RequestParam("q") String keyword,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit) {
        return ApiResponse.<List<AuthorResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(authorService.searchByName(keyword))
                .build();
    }

    @GetMapping("/public/popular")
    public ApiResponse<List<AuthorResponse>> getPopularAuthors(
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) {
        return ApiResponse.<List<AuthorResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(authorService.getPopularAuthors())
                .build();
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AuthorResponse> create(@RequestBody @Valid AuthorRequest request) {
        return ApiResponse.<AuthorResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Author created successfully")
                .result(authorService.create(request))
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<AuthorResponse>> getAll() {
        return ApiResponse.<List<AuthorResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(authorService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<AuthorResponse> getById(@PathVariable("id") Integer authorId) {
        return ApiResponse.<AuthorResponse>builder()
                .code(HttpStatus.OK.value())
                .result(authorService.getById(authorId))
                .build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<AuthorResponse> update(
            @PathVariable("id") Integer authorId,
            @RequestBody @Valid AuthorUpdateRequest request) {
        return ApiResponse.<AuthorResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Author updated successfully")
                .result(authorService.update(authorId, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable("id") Integer authorId) {
        authorService.delete(authorId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Author has been deleted successfully")
                .build();
    }

    @GetMapping("/name/{authorName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<AuthorResponse>> getByName(@PathVariable String authorName) {
        return ApiResponse.<List<AuthorResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(authorService.searchByName(authorName))
                .build();
    }
}