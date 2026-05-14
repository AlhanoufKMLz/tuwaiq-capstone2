package com.example.tuwaiqcapstone2.Controller;

import com.example.tuwaiqcapstone2.Api.ApiResponse;
import com.example.tuwaiqcapstone2.Model.Recipe;
import com.example.tuwaiqcapstone2.Service.RecipeService;
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
    public ResponseEntity<?> addRatRecipe(@RequestBody @Valid Recipe recipe, Errors errors){
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
    @GetMapping("/get-no-allergen")
    public ResponseEntity<?> findRecipeWithNoAllergens(){
        return ResponseEntity.status(200).body(recipeService.findRecipeWithNoAllergens());
    }

    @GetMapping("/get-name/{keyword}")
    public ResponseEntity<?> findRecipeByName(@PathVariable String keyword){
        return ResponseEntity.status(200).body(recipeService.findRecipeByName(keyword));
    }

    @GetMapping("/get-most-comment")
    public ResponseEntity<?> findRecipeWithMostComments(){
        return ResponseEntity.status(200).body(recipeService.findRecipeWithMostComments());
    }

    @GetMapping("/get-top-rated")
    public ResponseEntity<?> findTopRatedRecipes(){
        return ResponseEntity.status(200).body(recipeService.findTopRatedRecipes());
    }
}
