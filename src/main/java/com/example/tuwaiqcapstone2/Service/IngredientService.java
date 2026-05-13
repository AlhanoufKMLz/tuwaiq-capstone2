package com.example.tuwaiqcapstone2.Service;

import com.example.tuwaiqcapstone2.Api.ApiException;
import com.example.tuwaiqcapstone2.Enums.AllergenType;
import com.example.tuwaiqcapstone2.Model.Ingredient;
import com.example.tuwaiqcapstone2.Model.Recipe;
import com.example.tuwaiqcapstone2.Model.User;
import com.example.tuwaiqcapstone2.Repository.IngredientRepository;
import com.example.tuwaiqcapstone2.Repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final AiService aiService;

    //BASIC CRUD
    public List<Ingredient> getAllIngredients(){
        return ingredientRepository.findAll();
    }

    public void addIngredient(Ingredient ingredient){
        Recipe recipe = recipeRepository.findRecipeById(ingredient.getRecipeId());
        if(recipe == null) throw new ApiException("Recipe not found"); //check recipe

        //use Ai to update recipe allergens
        String prompt = "Given this ingredient: " + ingredient.getName() +
                ". Identify if it contains any of these allergens: " +
                Arrays.toString(AllergenType.values()) +
                ". Return ONLY the allergen names separated by commas, nothing else. Example: MILK,GLUTEN. " +
                "If no allergens found return empty string.";
        String chatRes = aiService.chat(prompt);
        List<AllergenType> newAllergens = Arrays.stream(chatRes.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(AllergenType::valueOf)
                .toList();

        //update recipe allergens
        List<AllergenType> currentAllergens = recipe.getAllergens();
        for(AllergenType allergen : newAllergens){
            if(!currentAllergens.contains(allergen)){
                currentAllergens.add(allergen);
            }
        }

        ingredientRepository.save(ingredient);
    }

    public void updateIngredient(Integer id, Ingredient ingredient){
        Ingredient oldIngredient = ingredientRepository.findIngredientById(id);
        if(oldIngredient == null) throw new ApiException("Ingredient not found"); //check ingredient

        Recipe recipe = recipeRepository.findRecipeById(ingredient.getRecipeId());
        if(recipe == null) throw new ApiException("Recipe not found"); //check recipe

        oldIngredient.setRecipeId(ingredient.getRecipeId());
        oldIngredient.setName(ingredient.getName());
        oldIngredient.setAmount(ingredient.getAmount());
        oldIngredient.setUnit(ingredient.getUnit());
        ingredientRepository.save(oldIngredient);
    }

    public void deleteIngredient(Integer id){
        Ingredient ingredient = ingredientRepository.findIngredientById(id);
        if(ingredient == null) throw new ApiException("Ingredient not found"); //check ingredient

        ingredientRepository.delete(ingredient);
    }
}
