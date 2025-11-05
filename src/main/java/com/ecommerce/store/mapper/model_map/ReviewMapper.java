package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.response.model_response.ReviewResponse;
import com.ecommerce.store.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "review.product.productId", target = "productId")
    @Mapping(source = "review.product.productName", target = "productName")
    @Mapping(source = "review.user.userId", target = "userId")
    @Mapping(source = "review.user.userName", target = "userName")
    @Mapping(source = "review.user.email", target = "email")
    @Mapping(source = "review.user.customer.profileImage", target = "profileImage")
    ReviewResponse toResponse(Review review);

    List<ReviewResponse> toResponseList(List<Review> reviews);
}
