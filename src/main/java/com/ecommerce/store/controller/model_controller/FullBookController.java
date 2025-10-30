package com.ecommerce.store.controller.model_controller;

import com.ecommerce.store.dto.request.model_request.FullBookRequest;
import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.dto.response.model_response.FullBookResponse;
import com.ecommerce.store.service.model_service.FullBookService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/full-books")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class FullBookController {

    FullBookService fullBookService;

    @PostMapping
    public ApiResponse<FullBookResponse> createFullBook(
            @RequestBody @Valid FullBookRequest request) {
        FullBookResponse result = fullBookService.createFullBook(request);

        return ApiResponse
                .<FullBookResponse>builder()
                .result(result)
                .message("Book created successfully with all related entities")
                .build();
    }

}
