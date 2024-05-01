package com.cookpad.responses;


import com.cookpad.entities.enums.RecipeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipePreviewResponse {
    private Long id; // Recipe ID
    private String name; // Recipe name
    private RecipeType type; // Recipe type
    private int calories; // Nutrition calories
    private int protein; // Nutrition protein
    private int fat; // Nutrition fat
}
