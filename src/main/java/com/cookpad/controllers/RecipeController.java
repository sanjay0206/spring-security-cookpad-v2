package com.cookpad.controllers;


import com.cookpad.dto.RecipeDto;
import com.cookpad.responses.RecipeResponse;
import com.cookpad.responses.RecipeWithNutritionResponse;
import com.cookpad.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {
    private static final String DEFAULT_PAGE_NUMBER = "0";
    private static final String DEFAULT_PAGE_SIZE = "2";
    private static final String DEFAULT_SORT_BY = "recipeId";
    private static final String DEFAULT_SORT_DIRECTION = "ASC";

    @Autowired
    private RecipeService recipeService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public RecipeResponse getAllRecipes(@RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                        @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
                                        @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return recipeService.getAllRecipes(pageNo, pageSize, sortBy, sortDir);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/recipes-with-nutrition")
    public List<RecipeWithNutritionResponse> getRecipesWithNutrition() {
        return recipeService.getRecipesWithNutrition();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeDto> getRecipeById(@PathVariable Long recipeId) {
        return ResponseEntity.ok(recipeService.getRecipeById(recipeId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-recipe")
    public RecipeDto createRecipe(@RequestBody @Valid RecipeDto recipeDto) {
        return recipeService.createRecipe(recipeDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update-recipe/{recipeId}")
    public ResponseEntity<RecipeDto> updateRecipe(@PathVariable Long recipeId, @RequestBody RecipeDto RecipeDto) {
        return ResponseEntity.ok(recipeService.updateRecipe(recipeId, RecipeDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete-recipe/{recipeId}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long recipeId) {
        recipeService.deleteRecipe(recipeId);
        return ResponseEntity.noContent().build();
    }
}
