package com.example.tuwaiqcapstone2.Controller;

import com.example.tuwaiqcapstone2.Api.ApiResponse;
import com.example.tuwaiqcapstone2.DTO.GenerateRecipeRequest;
import com.example.tuwaiqcapstone2.Enums.DifficultyLevel;
import com.example.tuwaiqcapstone2.Model.Recipe;
import com.example.tuwaiqcapstone2.Service.RecipeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;


    //BASIC CRUD ENDPOINTS
    @GetMapping("/get")
    public ResponseEntity<?> getAllRecipes(){
        return ResponseEntity.status(200).body(recipeService.getAllRecipes());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addRecipe(@RequestBody @Valid Recipe recipe, Errors errors){
        if(errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));

        recipeService.addRecipe(recipe);
        return ResponseEntity.status(200).body(new ApiResponse("Recipe added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRecipe(@PathVariable Integer id, @RequestBody @Valid Recipe recipe, Errors errors){
        if(errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));

        recipeService.updateRecipe(id, recipe);
        return ResponseEntity.status(200).body(new ApiResponse("Recipe updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Integer id){
        recipeService.deleteRecipe(id);
        return ResponseEntity.status(200).body(new ApiResponse("Recipe deleted successfully"));
    }


    //EXTRA ENDPOINTS
    @GetMapping("/get-category/{categoryId}")
    public ResponseEntity<?> findRecipeByCategoryId(@PathVariable Integer categoryId){
        return ResponseEntity.status(200).body(recipeService.findRecipeByCategoryId(categoryId));
    }

    @GetMapping("/get-user/{userId}")
    public ResponseEntity<?> findRecipeByUserId(@PathVariable Integer userId){
        return ResponseEntity.status(200).body(recipeService.findRecipeByUserId(userId));
    }

    @GetMapping("/get-difficulty/{difficulty}")
    public ResponseEntity<?> findRecipeByDifficulty(@PathVariable DifficultyLevel difficulty){
        return ResponseEntity.status(200).body(recipeService.findRecipeByDifficulty(difficulty));
    }

    @GetMapping("/get-no-allergen")
    public ResponseEntity<?> findRecipeWithNoAllergens(){
        return ResponseEntity.status(200).body(recipeService.findRecipeWithNoAllergens());
    }

    @GetMapping("/get-name/{keyword}")
    public ResponseEntity<?> findRecipeByName(@PathVariable String keyword){
        return ResponseEntity.status(200).body(recipeService.findRecipeByName(keyword));
    }
    //#2
    @GetMapping("/get-cookTime/{minutes}")
    public ResponseEntity<?> getRecipesWithCookTimeLessThan(@PathVariable Integer minutes){
        return ResponseEntity.status(200).body(recipeService.getRecipesWithCookTimeLessThan(minutes));
    }

    @GetMapping("/get-most-comment")
    public ResponseEntity<?> findRecipesSortedByMostComments(){
        return ResponseEntity.status(200).body(recipeService.findRecipesSortedByMostComments());
    }

    @GetMapping("/get-top-rated")
    public ResponseEntity<?> findTopRatedRecipes(){
        return ResponseEntity.status(200).body(recipeService.findTopRatedRecipes());
    }
    //#3
    @GetMapping("/get-top-rated-week")
    public ResponseEntity<?> findTopRatedRecipesThisWeek(){
        return ResponseEntity.status(200).body(recipeService.findTopRatedRecipesThisWeek());
    }
    //#4
    @GetMapping("/get-user-favorite/{userId}")
    public ResponseEntity<?> findUserFavoritesRecipes(@PathVariable Integer userId){
        return ResponseEntity.status(200).body(recipeService.findUserFavoritesRecipes(userId));
    }
    //#5
    @GetMapping("/get-similar/{recipeId}")
    public ResponseEntity<?> findSimilarRecipes(@PathVariable Integer recipeId){
        return ResponseEntity.status(200).body(recipeService.findSimilarRecipes(recipeId));
    }
    //#6
    @GetMapping("/get-safe-user/{userId}")
    public ResponseEntity<?> findSafeRecipesForUser(@PathVariable Integer userId){
        return ResponseEntity.status(200).body(recipeService.findSafeRecipesForUser(userId));
    }
    //#7
    @GetMapping("/get-feed/{userId}")
    public ResponseEntity<?> findRecipesFeed(@PathVariable Integer userId){
        return ResponseEntity.status(200).body(recipeService.findRecipesFeed(userId));
    }
    //#8
    @GetMapping("/details/{recipeId}")
    public ResponseEntity<?> getRecipeDetails(@PathVariable Integer recipeId){
        return ResponseEntity.status(200).body(recipeService.getRecipeDetails(recipeId));
    }
    //#12
    @GetMapping("/nutrition-analyze/{recipeId}")
    public ResponseEntity<?> analyzeRecipeNutrition(@PathVariable Integer recipeId) {
        return ResponseEntity.status(200).body(recipeService.analyzeRecipeNutrition(recipeId));
    }
    //#13
    @PostMapping("/generate")
    public ResponseEntity<?> GenerateRecipe(@RequestBody GenerateRecipeRequest request){
        return ResponseEntity.status(200).body(recipeService.GenerateRecipe(request));
    }
    //#14
    @PostMapping("/convert-serving/{recipeId}/{serving}")
    public ResponseEntity<?> ConvertServings(@PathVariable Integer recipeId, @PathVariable Integer serving){
        return ResponseEntity.status(200).body(recipeService.ConvertServings(recipeId, serving));
    }
}
