package com.example.tuwaiqcapstone2.DTO;

import com.example.tuwaiqcapstone2.Model.Comment;
import com.example.tuwaiqcapstone2.Model.Ingredient;
import com.example.tuwaiqcapstone2.Model.Recipe;
import com.example.tuwaiqcapstone2.Model.RecipeStep;
import lombok.Data;

import java.util.List;

@Data
public class RecipeDetailsResponse {
    private Recipe recipe;
    private List<Ingredient> ingredients;
    private List<RecipeStep> steps;
    private Double averageRating;
    List<Comment> comments;
}