package com.ecommerce.store.entity;

import com.ecommerce.store.entity.base.BaseTimeEntity;
import com.ecommerce.store.enums.entity_enums.AuthorEnums.Nationality;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authors")
public class Author extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer authorId;

    @Column(nullable = false)
    String authorName;

    @Column(columnDefinition = "TEXT")
    String bio;

    LocalDate bornDate;

    LocalDate deathDate;

    @Enumerated(EnumType.STRING)
    Nationality nationality;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "asset_id", unique = true, updatable = false)
    Asset asset;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<BookAuthor> bookAuthors;

}
