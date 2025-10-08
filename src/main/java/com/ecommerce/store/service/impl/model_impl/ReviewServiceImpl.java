package com.ecommerce.store.service.impl.model_impl;

import com.ecommerce.store.dto.request.model_request.ReviewRequest;
import com.ecommerce.store.dto.request.model_request.ReviewUpdateRequest;
import com.ecommerce.store.dto.response.model_response.ReviewResponse;
import com.ecommerce.store.entity.Product;
import com.ecommerce.store.entity.Review;
import com.ecommerce.store.entity.User;
import com.ecommerce.store.enums.error.ErrorCode;
import com.ecommerce.store.exception.AppException;
import com.ecommerce.store.mapper.model_map.ReviewMapper;
import com.ecommerce.store.repository.ProductRepository;
import com.ecommerce.store.repository.ReviewRepository;
import com.ecommerce.store.repository.UserRepository;
import com.ecommerce.store.service.model_service.ReviewService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewServiceImpl implements ReviewService {

    ReviewRepository reviewRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    ReviewMapper reviewMapper;

    @Override
    public ReviewResponse createReviewForUser(Jwt jwt, Integer productId, ReviewRequest request) {
        String userId = jwt.getClaimAsString("id");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Review review = Review.builder()
                .product(product)
                .user(user)
                .rating(request.rating())
                .comment(request.comment())
                .isApproved(false)
                .build();

        reviewRepository.save(review);
        return reviewMapper.toResponse(review);
    }

    @Override
    public ReviewResponse updateReviewForUser(Jwt jwt, Integer reviewId, ReviewUpdateRequest request) {
        String userId = jwt.getClaimAsString("id");

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getUser().getUserId().equals(userId)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        if (request.rating() != null) {
            if (request.rating() < 0 || request.rating() > 10) {
                throw new AppException(ErrorCode.INVALID_RATING);
            }
            review.setRating(request.rating());
        }
        if (request.comment() != null) review.setComment(request.comment());

        review.setUpdatedAt(LocalDateTime.now());
        reviewRepository.save(review);
        return reviewMapper.toResponse(review);
    }

    @Transactional
    @Override
    public void deleteReviewForUser(Jwt jwt, Integer reviewId) {
        String userId = jwt.getClaimAsString("id");

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getUser().getUserId().equals(userId)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        reviewRepository.delete(review);
    }

    @Override
    public List<ReviewResponse> getReviewsForUser(Jwt jwt) {
        String userId = jwt.getClaimAsString("id");

        return reviewMapper.toResponseList(
                reviewRepository.findByUserUserId(userId)
        );
    }

    @Override
    public List<ReviewResponse> getReviewsByProduct(Integer productId) {
        return reviewMapper.toResponseList(
                reviewRepository.findByProductProductId(productId)
        );
    }

    @Transactional
    @Override
    public ReviewResponse approveReview(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        review.setIsApproved(true);
        review.setApprovedAt(LocalDateTime.now());
        reviewRepository.save(review);
        return reviewMapper.toResponse(review);
    }

    @Transactional
    @Override
    public void deleteReviewAsAdmin(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        reviewRepository.delete(review);
    }
}
