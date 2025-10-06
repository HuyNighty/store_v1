package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.request.model_request.AuthorRequest;
import com.ecommerce.store.dto.request.model_request.AuthorUpdateRequest;
import com.ecommerce.store.dto.response.model_response.AuthorResponse;
import com.ecommerce.store.entity.Author;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author toEntity(AuthorRequest authorRequest);

    @Mapping(source = "asset.assetId", target = "assetId")
    AuthorResponse toResponse(Author author);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAuthorFromRequest(AuthorUpdateRequest authorRequest, @MappingTarget Author author);
}
