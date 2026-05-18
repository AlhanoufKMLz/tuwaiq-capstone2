package com.example.tuwaiqcapstone2.DTO;

import lombok.Data;

@Data
public class NutritionAnalysisResponse {
    private Integer calories;
    private Double protein;
    private Double carbs;
    private Double fats;
    private Double sugar;
    private Double sodium;
}
