package com.cookpad.mapper;

import com.cookpad.responses.RecipeWithNutritionResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecipeMapper {

    // Method to get recipes with their corresponding nutrition information
    List<RecipeWithNutritionResponse> getRecipesWithNutrition();

}
