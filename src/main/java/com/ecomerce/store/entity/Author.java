package com.ecomerce.store.entity;

import com.ecomerce.store.entity.base.BaseTimeEntity;
import com.ecomerce.store.enums.AuthorEnums.Nationality;
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

    @NotNull
    @Column(nullable = false)
    String authorName;

    @Column(columnDefinition = "TEXT")
    String bio;

    LocalDate bornDate;

    LocalDate deathDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    Nationality nationality;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "asset_id",  nullable = false, unique = true, updatable = false)
    Asset asset;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<BookAuthor> bookAuthors;

}
