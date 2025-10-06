package com.ecommerce.store.service.model_service;

import com.ecommerce.store.dto.request.model_request.ReviewRequest;
import com.ecommerce.store.dto.request.model_request.ReviewUpdateRequest;
import com.ecommerce.store.dto.response.model_response.ReviewResponse;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface ReviewService {

    ReviewResponse createReviewForUser(Jwt jwt, Integer productId, ReviewRequest request);

    ReviewResponse updateReviewForUser(Jwt jwt, Integer reviewId, ReviewUpdateRequest request);

    void deleteReviewForUser(Jwt jwt, Integer reviewId);

    List<ReviewResponse> getReviewsForUser(Jwt jwt);

    List<ReviewResponse> getReviewsByProduct(Integer productId);

    ReviewResponse approveReview(Integer reviewId);

    void deleteReviewAsAdmin(Integer reviewId);
}
