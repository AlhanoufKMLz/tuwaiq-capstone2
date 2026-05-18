package com.example.tuwaiqcapstone2.DTO;

import com.example.tuwaiqcapstone2.Enums.LanguageCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TranslateAndShareRecipeRequest {
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number")
    private String phoneNumber;

    @NotNull(message = "Recipe Id must not be null")
    private Integer recipeId;

    private LanguageCode language;
}
