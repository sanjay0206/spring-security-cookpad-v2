package com.cookpad.controllers;

import com.cookpad.dto.NutritionDto;
import com.cookpad.services.NutritionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recipes/{recipeId}")
public class NutritionController {

    @Autowired
    private NutritionService nutritionService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-nutrition")
    public NutritionDto createNutrition(@PathVariable Long recipeId, @RequestBody NutritionDto nutritionDto) {
        return nutritionService.createNutrition(recipeId, nutritionDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update-nutrition")
    public ResponseEntity<NutritionDto> updateNutrition(@PathVariable Long recipeId, @RequestBody NutritionDto nutritionDto) {
        return ResponseEntity.ok(nutritionService.updateNutrition(recipeId, nutritionDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete-nutrition")
    public ResponseEntity<Void> deleteNutrition(@PathVariable Long recipeId) {
        nutritionService.deleteNutrition(recipeId);
        return ResponseEntity.noContent().build();
    }
}
