package com.cookpad.services;

import com.cookpad.dto.RecipeDto;
import com.cookpad.entities.Recipe;
import com.cookpad.responses.RecipePreviewResponse;
import com.cookpad.responses.RecipeResponse;

import java.util.List;

public interface RecipeService {
    RecipeResponse getAllRecipes(int pageNo, int pageSize, String sortBy, String sortDir);
    List<RecipePreviewResponse> getAllRecipesPreview();
    RecipeDto getRecipeById(Long recipeId);
    RecipeDto createRecipe(RecipeDto recipeDto);
    RecipeDto updateRecipe(Long recipeId, RecipeDto recipeDto);
    void deleteRecipe(Long recipeId);
    RecipeDto mapToDTO (Recipe recipe);
    Recipe mapToEntity(RecipeDto recipeDto);

}

