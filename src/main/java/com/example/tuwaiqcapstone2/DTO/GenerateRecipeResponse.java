package com.example.tuwaiqcapstone2.DTO;

import com.example.tuwaiqcapstone2.Enums.DifficultyLevel;
import com.example.tuwaiqcapstone2.Model.Ingredient;
import com.example.tuwaiqcapstone2.Model.RecipeStep;
import lombok.Data;

import java.util.List;

@Data
public class GenerateRecipeResponse {
    private String name;
    private String description;
    private Integer cookTime;
    private DifficultyLevel difficulty;
    private Integer servings;
    private List<Ingredient> ingredients;
    private List<RecipeStep> steps;
}
