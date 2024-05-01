package com.cookpad.controllers;


import com.cookpad.dto.NutritionDto;
import com.cookpad.dto.RecipeDto;
import com.cookpad.dto.RecipeDtoV2;
import com.cookpad.responses.RecipePreviewResponse;
import com.cookpad.responses.RecipeResponse;
import com.cookpad.services.NutritionService;
import com.cookpad.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
public class RecipeController {
    private static final String DEFAULT_PAGE_NUMBER = "0";
    private static final String DEFAULT_PAGE_SIZE = "2";
    private static final String DEFAULT_SORT_BY = "recipeId";
    private static final String DEFAULT_SORT_DIRECTION = "ASC";

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private NutritionService nutritionService;

    @GetMapping("/api/v1/recipes")
    public RecipeResponse getAllRecipes(@RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                        @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
                                        @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return recipeService.getAllRecipes(pageNo, pageSize, sortBy, sortDir);
    }

    @PreAuthorize("hasAuthority('SCOPE_recipe:read')")
    @GetMapping("/api/v1/recipes/recipes-preview")
    public List<RecipePreviewResponse> getAllRecipesPreview() {
        return recipeService.getAllRecipesPreview();
    }

    @PreAuthorize("hasAuthority('SCOPE_recipe:read')")
    @GetMapping(value = "/api/recipes/{recipeId}", produces = "application/vnd.cookpad.v1+json")
    public ResponseEntity<RecipeDto> getRecipeByIdContentNegotiation(@PathVariable Long recipeId) {
        return ResponseEntity.ok(recipeService.getRecipeById(recipeId));
    }

    @PreAuthorize("hasAuthority('SCOPE_recipe:read')")
    @GetMapping(value = "/api/recipes/{recipeId}", headers = "X-API-VERSION=1")
    public ResponseEntity<RecipeDto> getRecipeByIdHeader(@PathVariable Long recipeId) {
        return ResponseEntity.ok(recipeService.getRecipeById(recipeId));
    }

    @PreAuthorize("hasAuthority('SCOPE_recipe:read')")
    @GetMapping(value = "/api/recipes/{recipeId}", params = "version=2")
    public ResponseEntity<RecipeDtoV2> getRecipeByIdParam(@PathVariable Long recipeId) {
        RecipeDto recipeDto = recipeService.getRecipeById(recipeId);

        RecipeDtoV2 recipeDtoV2 = new RecipeDtoV2();
        recipeDtoV2.setRecipeId(recipeDto.getRecipeId());
        recipeDtoV2.setRecipeName(recipeDto.getRecipeName());
        recipeDtoV2.setRecipeType(recipeDto.getRecipeType());
        recipeDtoV2.setPrepTime(recipeDto.getPrepTime());
        recipeDtoV2.setCookingTime(recipeDto.getCookingTime());
        recipeDtoV2.setServes(recipeDtoV2.getServes());
        recipeDtoV2.setIngredients(recipeDtoV2.getIngredients());
        recipeDtoV2.setCookingMethod(recipeDto.getCookingMethod());
        recipeDtoV2.setImageUrl(recipeDto.getImageUrl());

        // Add Nutrition also the response
        NutritionDto nutritionDto = nutritionService.getNutritionById(recipeId);
        recipeDtoV2.setNutrition(nutritionDto);

        return ResponseEntity.ok(recipeDtoV2);
    }

    @PreAuthorize("hasAuthority('SCOPE_recipe:create')")
    @PostMapping("/api/v1/recipes/add-recipe")
    public RecipeDto createRecipe(@RequestBody @Valid RecipeDto recipeDto) {
        return recipeService.createRecipe(recipeDto);
    }

    @PreAuthorize("hasAuthority('SCOPE_recipe:update')")
    @PutMapping("/api/v1/recipes/update-recipe/{recipeId}")
    public ResponseEntity<RecipeDto> updateRecipe(@PathVariable Long recipeId, @RequestBody RecipeDto RecipeDto) {
        return ResponseEntity.ok(recipeService.updateRecipe(recipeId, RecipeDto));
    }

    @PreAuthorize("hasAuthority('SCOPE_recipe:delete')")
    @DeleteMapping("/api/v1/recipes/delete-recipe/{recipeId}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long recipeId) {
        recipeService.deleteRecipe(recipeId);
        return ResponseEntity.noContent().build();
    }
}
