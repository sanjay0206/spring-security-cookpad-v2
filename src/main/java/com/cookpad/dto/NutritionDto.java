package com.cookpad.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NutritionDto {
    private Long nutritionId;
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;
    private Double fiber;
}