package com.cookpad.services;

import com.cookpad.dto.NutritionDto;
import com.cookpad.entities.Nutrition;

public interface NutritionService {
    NutritionDto createNutrition(Long recipeId, NutritionDto nutritionDto);
    NutritionDto updateNutrition(Long recipeId, NutritionDto nutritionDto);
    void deleteNutrition(Long recipeId);

    NutritionDto mapToDTO (Nutrition nutrition);

    Nutrition mapToEntity(NutritionDto nutritionDto);
}
