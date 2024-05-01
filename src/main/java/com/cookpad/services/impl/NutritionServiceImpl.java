package com.cookpad.services.impl;

import com.cookpad.dto.NutritionDto;
import com.cookpad.entities.Nutrition;
import com.cookpad.entities.Recipe;
import com.cookpad.exceptions.RecipeAPIException;
import com.cookpad.exceptions.ResourceNotFoundException;
import com.cookpad.repositories.NutritionRepository;
import com.cookpad.repositories.RecipeRepository;
import com.cookpad.services.NutritionService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NutritionServiceImpl implements NutritionService {

    private final RecipeRepository recipeRepository;
    private final NutritionRepository nutritionRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public NutritionServiceImpl(RecipeRepository recipeRepository, NutritionRepository nutritionRepository, ModelMapper modelMapper) {
        this.recipeRepository = recipeRepository;
        this.nutritionRepository = nutritionRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public NutritionDto getNutritionById(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "recipeId", recipeId));

        if (recipe.getNutrition() == null) {
            throw new RecipeAPIException(HttpStatus.BAD_REQUEST, "No nutrition record is present for Recipe ID: " + recipeId);
        }

        return mapToDTO(recipe.getNutrition());
    }

    @Override
    public NutritionDto createNutrition(Long recipeId, NutritionDto nutritionDto) {;
        log.info("recipeId: "  + recipeId);
        log.info("nutritionDto: "  + nutritionDto);

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "recipeId", recipeId));

        // Check if the recipe already has associated nutrition
        if (recipe.getNutrition() != null) {
            throw new RecipeAPIException(HttpStatus.BAD_REQUEST, "A nutrition record already exists for Recipe ID: " + recipeId);
        }

        Nutrition nutrition = mapToEntity(nutritionDto);
        nutrition.setRecipe(recipe);
        Nutrition savedNutrition = nutritionRepository.save(nutrition);

        return mapToDTO(savedNutrition);
    }

    @Override
    public NutritionDto updateNutrition(Long recipeId, NutritionDto nutritionDto) {
        log.info("recipeId: "  + recipeId);
        log.info("nutritionDto: "  + nutritionDto);
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "recipeId", recipeId));

        if (recipe.getNutrition() == null) {
            throw new RecipeAPIException(HttpStatus.BAD_REQUEST, "No nutrition record is present for Recipe ID: " + recipeId);
        }

        Nutrition existingNutrition = recipe.getNutrition();
        if (nutritionDto.getCalories() != null) {
            existingNutrition.setCalories(nutritionDto.getCalories());
        }
        if (nutritionDto.getProtein() != null) {
            existingNutrition.setProtein(nutritionDto.getProtein());
        }
        if (nutritionDto.getCarbs() != null) {
            existingNutrition.setCarbs(nutritionDto.getCarbs());
        }
        if (nutritionDto.getFat() != null) {
            existingNutrition.setFat(nutritionDto.getFat());
        }
        if (nutritionDto.getFiber() != null) {
            existingNutrition.setFiber(nutritionDto.getFiber());
        }

       return mapToDTO(nutritionRepository.save(existingNutrition));
    }

    @Override
    public void deleteNutrition(Long recipeId) {
        log.info("recipeId: "  + recipeId);
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "recipeId", recipeId));

        if (recipe.getNutrition() == null) {
            throw new RecipeAPIException(HttpStatus.BAD_REQUEST, "No nutrition record is present for Recipe ID: " + recipeId);
        }

        nutritionRepository.deleteById(recipe.getNutrition().getNutritionId());
    }

    @Override
    public NutritionDto mapToDTO(Nutrition nutrition) {
        return modelMapper.map(nutrition, NutritionDto.class);
    }

    @Override
    public Nutrition mapToEntity(NutritionDto nutritionDto) {
        return modelMapper.map(nutritionDto, Nutrition.class);
    }
}

