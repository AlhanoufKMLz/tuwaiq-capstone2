package com.example.tuwaiqcapstone2.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RecipeStepsRequest {
    @NotEmpty(message = "Instructions must not be empty")
    private String instructions;
}
