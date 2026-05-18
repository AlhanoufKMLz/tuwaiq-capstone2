package com.example.tuwaiqcapstone2.Service;

import com.example.tuwaiqcapstone2.Api.ApiException;
import com.example.tuwaiqcapstone2.Enums.AllergenType;
import com.example.tuwaiqcapstone2.Model.Ingredient;
import com.example.tuwaiqcapstone2.Model.Recipe;
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
        Recipe recipe = checkRecipe(ingredient.getRecipeId());

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
        Ingredient oldIngredient = checkIngredient(id);
        checkRecipe(ingredient.getRecipeId());

        oldIngredient.setRecipeId(ingredient.getRecipeId());
        oldIngredient.setName(ingredient.getName());
        oldIngredient.setAmount(ingredient.getAmount());
        oldIngredient.setUnit(ingredient.getUnit());
        ingredientRepository.save(oldIngredient);
    }

    public void deleteIngredient(Integer id){
        Ingredient ingredient = checkIngredient(id);

        ingredientRepository.delete(ingredient);
    }


    //EXTRA ENDPOINT
    public List<Ingredient> findIngredientByRecipeId(Integer recipeId){
        checkRecipe(recipeId);

        List<Ingredient> ingredients = ingredientRepository.findIngredientByRecipeId(recipeId);

        if(ingredients.isEmpty()) throw new ApiException("No ingredients found");

        return ingredients;
    }

    public String findIngredientSubstitute(Integer ingredientId) {
        Ingredient ingredient = checkIngredient(ingredientId);

        String prompt = "Suggest one substitute for this ingredient: " + ingredient.getName() +
                ". Return ONLY the substitute name and a brief reason in one sentence. " +
                "Example: Use almond milk instead - it is dairy free and works the same way.";

        return aiService.chat(prompt);
    }


    //HELPER METHODS
    private Ingredient checkIngredient(Integer id){
        Ingredient ingredient = ingredientRepository.findIngredientById(id);
        if(ingredient == null) throw new ApiException("Ingredient not found"); //check ingredient

        return ingredient;
    }

    private Recipe checkRecipe(Integer id){
        Recipe recipe = recipeRepository.findRecipeById(id);
        if(recipe == null) throw new ApiException("Recipe not found"); //check recipe

        return recipe;
    }
}
