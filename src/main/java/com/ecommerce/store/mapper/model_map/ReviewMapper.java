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
    ReviewResponse toResponse(Review review);

    List<ReviewResponse> toResponseList(List<Review> reviews);
}
