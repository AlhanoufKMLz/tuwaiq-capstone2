package com.example.tuwaiqcapstone2.DTO;

import lombok.Data;

import java.util.List;

@Data
public class GenerateRecipeRequest {
    List<String> ingredients;
    Boolean allowExtraIngredients;
}
