package com.cookpad.dto;


import com.cookpad.entities.enums.RecipeType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDtoV2 {

    private Long recipeId;

    @NotEmpty(message = "Recipe name should not be null or empty")
    private String recipeName;

    private RecipeType recipeType;

    private Integer prepTime;

    private Integer cookingTime;

    private Integer serves;

    @NotEmpty(message = "Ingredients should not be null or empty")
    @Size(min = 10, message = "Ingredients should have at least 10 characters")
    private String ingredients;

    @NotEmpty(message = "Cooking method should not be null or empty")
    @Size(min = 10, message = "Cooking method should have at least 10 characters")
    private String cookingMethod;

    private String imageUrl;

    // Added Nutrition also to Recipe in response in V2
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private NutritionDto nutrition;

}


