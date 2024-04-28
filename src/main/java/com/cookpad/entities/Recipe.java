package com.cookpad.entities;

import com.cookpad.entities.enums.RecipeType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Entity
@Table(name = "recipes",
        indexes = {
                @Index(name = "idx_recipe_name", columnList = "recipe_name"),
        })
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private Long recipeId;

    @Column(name = "recipe_name", nullable = false)
    private String recipeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "recipe_type")
    private RecipeType recipeType;

    @Column(name = "prep_time")
    private Integer prepTime;

    @Column(name = "cooking_time")
    private Integer cookingTime;

    @Column(name = "serves")
    private Integer serves;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "ingredients", columnDefinition = "TEXT", nullable = false)
    private String ingredients;

    @Column(name = "cooking_method", columnDefinition = "TEXT", nullable = false)
    private String cookingMethod;

    @OneToOne(mappedBy = "recipe", cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    private Nutrition nutrition;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    public Recipe(String recipeName,
                  RecipeType recipeType,
                  Integer prepTime,
                  Integer cookingTime,
                  Integer serves,
                  String imageUrl,
                  String ingredients,
                  String cookingMethod,
                  LocalDateTime createdAt,
                  LocalDateTime modifiedAt) {
        this.recipeName = recipeName;
        this.recipeType = recipeType;
        this.prepTime = prepTime;
        this.cookingTime = cookingTime;
        this.serves = serves;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
        this.cookingMethod = cookingMethod;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
