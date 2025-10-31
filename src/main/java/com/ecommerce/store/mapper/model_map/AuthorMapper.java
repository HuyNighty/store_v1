package com.ecommerce.store.mapper.model_map;

import com.ecommerce.store.dto.request.model_request.AuthorRequest;
import com.ecommerce.store.dto.request.model_request.AuthorUpdateRequest;
import com.ecommerce.store.dto.response.model_response.AuthorResponse;
import com.ecommerce.store.entity.Author;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(target = "authorId", ignore = true)
    @Mapping(target = "bookAuthors", ignore = true)
    Author toEntity(AuthorRequest authorRequest);

    default AuthorResponse toResponse(Author author) {
        if (author == null) {
            return null;
        }

        return AuthorResponse.builder()
                .authorId(author.getAuthorId())
                .authorName(author.getAuthorName())
                .bio(author.getBio())
                .bornDate(author.getBornDate())
                .deathDate(author.getDeathDate())
                .nationality(author.getNationality())
                .assetId(author.getAsset() != null ? author.getAsset().getAssetId() : null)
                .build();
    }

    @Mapping(source = "asset.assetId", target = "assetId")
    AuthorResponse toResponseWithMapping(Author author);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAuthorFromRequest(AuthorUpdateRequest authorRequest, @MappingTarget Author author);

    default String mapPortraitUrl(Author author) {
        if (author.getPortraitUrl() != null && !author.getPortraitUrl().isBlank()) {
            return author.getPortraitUrl();
        }
        if (author.getAsset() != null && author.getAsset().getUrl() != null) {
            return author.getAsset().getUrl();
        }
        return "/images/default-author.jpg";
    }
}