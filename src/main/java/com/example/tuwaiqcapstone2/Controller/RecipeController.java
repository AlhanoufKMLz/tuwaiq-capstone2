package com.example.tuwaiqcapstone2.Controller;

import com.example.tuwaiqcapstone2.Api.ApiResponse;
import com.example.tuwaiqcapstone2.DTO.GenerateRecipeRequest;
import com.example.tuwaiqcapstone2.DTO.TranslateAndShareRecipeRequest;
import com.example.tuwaiqcapstone2.Enums.DifficultyLevel;
import com.example.tuwaiqcapstone2.Enums.LanguageCode;
import com.example.tuwaiqcapstone2.Model.Recipe;
import com.example.tuwaiqcapstone2.Service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> addRecipe(@RequestBody @Valid Recipe recipe){
        recipeService.addRecipe(recipe);
        return ResponseEntity.status(200).body(new ApiResponse("Recipe added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRecipe(@PathVariable Integer id, @RequestBody @Valid Recipe recipe){
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

    @GetMapping("/get-serving/{serving}")
    public ResponseEntity<?> findRecipeByServings(@PathVariable Integer serving){
        return ResponseEntity.status(200).body(recipeService.findRecipeByServings(serving));
    }

    @GetMapping("/get-name/{keyword}")
    public ResponseEntity<?> findRecipeByName(@PathVariable String keyword){
        return ResponseEntity.status(200).body(recipeService.findRecipeByName(keyword));
    }

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

    @GetMapping("/get-top-rated-week")
    public ResponseEntity<?> findTopRatedRecipesThisWeek(){
        return ResponseEntity.status(200).body(recipeService.findTopRatedRecipesThisWeek());
    }

    @GetMapping("/get-user-favorite/{userId}")
    public ResponseEntity<?> findUserFavoritesRecipes(@PathVariable Integer userId){
        return ResponseEntity.status(200).body(recipeService.findUserFavoritesRecipes(userId));
    }

    @GetMapping("/get-similar/{recipeId}")
    public ResponseEntity<?> findSimilarRecipes(@PathVariable Integer recipeId){
        return ResponseEntity.status(200).body(recipeService.findSimilarRecipes(recipeId));
    }

    @GetMapping("/get-safe-user/{userId}")
    public ResponseEntity<?> findSafeRecipesForUser(@PathVariable Integer userId){
        return ResponseEntity.status(200).body(recipeService.findSafeRecipesForUser(userId));
    }

    @GetMapping("/get-feed/{userId}")
    public ResponseEntity<?> findRecipesFeed(@PathVariable Integer userId){
        return ResponseEntity.status(200).body(recipeService.findRecipesFeed(userId));
    }

    @GetMapping("/details/{recipeId}")
    public ResponseEntity<?> getRecipeDetails(@PathVariable Integer recipeId){
        return ResponseEntity.status(200).body(recipeService.getRecipeDetails(recipeId));
    }

    @GetMapping("/nutrition-analyze/{recipeId}")
    public ResponseEntity<?> analyzeRecipeNutrition(@PathVariable Integer recipeId) {
        return ResponseEntity.status(200).body(recipeService.analyzeRecipeNutrition(recipeId));
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateRecipe(@RequestBody @Valid GenerateRecipeRequest request){
        return ResponseEntity.status(200).body(recipeService.generateRecipe(request));
    }

    @PostMapping("/convert-serving/{recipeId}/{serving}")
    public ResponseEntity<?> convertServings(@PathVariable Integer recipeId, @PathVariable Integer serving){
        return ResponseEntity.status(200).body(recipeService.convertServings(recipeId, serving));
    }

    @GetMapping("/translate/{recipeId}/{language}")
    public ResponseEntity<?> translateRecipe(@PathVariable Integer recipeId, @PathVariable LanguageCode language){
        return ResponseEntity.status(200).body(recipeService.translateRecipe(recipeId, language));
    }

    @PostMapping("/translate-share")
    public ResponseEntity<?> translateAndShare(@RequestBody @Valid TranslateAndShareRecipeRequest request){
        recipeService.translateAndShare(request.getRecipeId(), request.getLanguage(), request.getPhoneNumber());
        return ResponseEntity.status(200).body(new ApiResponse("Recipe shared successfully"));
    }

    @GetMapping("/get-altirnative/{recipeId}/{userId}")
    public ResponseEntity<?> getHealthyAlternative(@PathVariable Integer recipeId, @PathVariable Integer userId){
        return ResponseEntity.status(200).body(recipeService.getHealthyAlternative(recipeId,userId));
    }
}
