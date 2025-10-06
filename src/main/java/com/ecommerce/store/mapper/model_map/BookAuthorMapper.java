package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.request.model_request.BookAuthorRequest;
import com.ecommerce.store.dto.response.model_response.BookAuthorResponse;
import com.ecommerce.store.entity.BookAuthor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookAuthorMapper {

    BookAuthor toEntity(BookAuthorRequest request);

    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "author.authorId", target = "authorId")
    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "author.authorName", target = "authorName")
    BookAuthorResponse toResponse(BookAuthor bookAuthor);
}
