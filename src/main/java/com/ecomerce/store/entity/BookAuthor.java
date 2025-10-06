package com.ecomerce.store.entity;

import com.ecomerce.store.entity.base.BaseTimeEntity;
import com.ecomerce.store.entity.key.BookAuthorId;
import com.ecomerce.store.enums.entity_enums.BookAuthorEnums.AuthorRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book_authors")
public class BookAuthor extends BaseTimeEntity {

    @EmbeddedId
    BookAuthorId bookAuthorId;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @ManyToOne
    @MapsId("authorId")
    @JoinColumn(name = "author_id",  nullable = false)
    Author author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    AuthorRole authorRole;
}
