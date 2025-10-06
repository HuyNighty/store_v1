package com.ecommerce.store.controller.model_controller;

import com.ecommerce.store.dto.request.model_request.AuthorRequest;
import com.ecommerce.store.dto.request.model_request.AuthorUpdateRequest;
import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.dto.response.model_response.AuthorResponse;
import com.ecommerce.store.service.model_service.AuthorService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorController {
    AuthorService authorService;

    @PostMapping
    public ApiResponse<AuthorResponse> create(@RequestBody @Valid AuthorRequest request) {
        return ApiResponse
                .<AuthorResponse>builder()
                .result(authorService.create(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<AuthorResponse>> getAll() {
        return ApiResponse
                .<List<AuthorResponse>>builder()
                .result(authorService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<AuthorResponse> getById(@PathVariable Integer id) {
        return ApiResponse
                .<AuthorResponse>builder()
                .result(authorService.getById(id))
                .build();
    }

    @PatchMapping("/{id}")
    public ApiResponse<AuthorResponse> update(@PathVariable Integer id,
                                                 @RequestBody @Valid AuthorUpdateRequest request) {
        return ApiResponse
                .<AuthorResponse>builder()
                .result(authorService.update(id,request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Integer id) {
        authorService.delete(id);
        return ApiResponse
                .<Void>builder()
                .code(200)
                .message("Author has been deleted")
                .build();
    }

    @GetMapping("/name/{authorName}")
    public ApiResponse<List<AuthorResponse>> getByName(@PathVariable String authorName) {
        return ApiResponse
                .<List<AuthorResponse>>builder()
                .result(authorService.searchByName(authorName))
                .build();
    }
}