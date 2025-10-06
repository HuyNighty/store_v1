package com.ecommerce.store.controller.model_controller;

import com.ecommerce.store.dto.request.model_request.ReviewRequest;
import com.ecommerce.store.dto.request.model_request.ReviewUpdateRequest;
import com.ecommerce.store.dto.response.ApiResponse;
import com.ecommerce.store.dto.response.model_response.ReviewResponse;
import com.ecommerce.store.service.model_service.ReviewService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {

    ReviewService reviewService;

    @PostMapping("/products/{productId}")
    public ApiResponse<ReviewResponse> createReview(
            @PathVariable Integer productId,
            @RequestBody @Valid ReviewRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.createReviewForUser(jwt, productId, request))
                .build();
    }

    @PatchMapping("/{reviewId}")
    public ApiResponse<ReviewResponse> updateReview(
            @PathVariable Integer reviewId,
            @RequestBody ReviewUpdateRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.updateReviewForUser(jwt, reviewId, request))
                .build();
    }

    @DeleteMapping("/{reviewId}")
    public ApiResponse<Void> deleteReview(
            @PathVariable Integer reviewId,
            @AuthenticationPrincipal Jwt jwt) {
        reviewService.deleteReviewForUser(jwt, reviewId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Deleted review successfully")
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<List<ReviewResponse>> getMyReviews(
            @AuthenticationPrincipal Jwt jwt) {
        return ApiResponse.<List<ReviewResponse>>builder()
                .result(reviewService.getReviewsForUser(jwt))
                .build();
    }

    @GetMapping("/products/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<ReviewResponse>> getReviewsByProduct(@PathVariable Integer productId) {
        return ApiResponse.<List<ReviewResponse>>builder()
                .result(reviewService.getReviewsByProduct(productId))
                .build();
    }

    @PatchMapping("/admin/{reviewId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ReviewResponse> approveReview(@PathVariable Integer reviewId) {
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.approveReview(reviewId))
                .build();
    }

    @DeleteMapping("/admin/{reviewId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteReviewAsAdmin(@PathVariable Integer reviewId) {
        reviewService.deleteReviewAsAdmin(reviewId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Deleted review successfully")
                .build();
    }
}
