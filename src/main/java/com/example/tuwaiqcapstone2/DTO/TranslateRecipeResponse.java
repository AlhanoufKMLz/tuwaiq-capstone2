package com.example.tuwaiqcapstone2.DTO;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TranslateRecipeResponse {
    private String recipeName;
    private String description;
    private Integer cookTime;
    private String difficulty;
    private Integer servings;
    private List<String> allergens;

    private List<IngredientResponse> ingredients;
    private List<RecipeStepResponse> steps;
}
