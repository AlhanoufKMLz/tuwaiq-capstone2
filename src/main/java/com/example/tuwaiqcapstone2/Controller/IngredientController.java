package com.example.tuwaiqcapstone2.Controller;

import com.example.tuwaiqcapstone2.Api.ApiResponse;
import com.example.tuwaiqcapstone2.Model.Ingredient;
import com.example.tuwaiqcapstone2.Service.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ingredient")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;


    //BASIC CRUD ENDPOINTS
    @GetMapping("/get")
    public ResponseEntity<?> getAllIngredients(){
        return ResponseEntity.status(200).body(ingredientService.getAllIngredients());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addIngredient(@RequestBody @Valid Ingredient ingredient){
        ingredientService.addIngredient(ingredient);
        return ResponseEntity.status(200).body(new ApiResponse("Ingredient added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateIngredient(@PathVariable Integer id, @RequestBody @Valid Ingredient ingredient){
        ingredientService.updateIngredient(id, ingredient);
        return ResponseEntity.status(200).body(new ApiResponse("Ingredient updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteIngredient(@PathVariable Integer id){
        ingredientService.deleteIngredient(id);
        return ResponseEntity.status(200).body(new ApiResponse("Ingredient deleted successfully"));
    }


    //EXTRA ENDPOINTS
    @GetMapping("/get-recipe/{recipeId}")
    public ResponseEntity<?> findIngredientByRecipeId(@PathVariable Integer recipeId){
        return ResponseEntity.status(200).body(ingredientService.findIngredientByRecipeId(recipeId));
    }

    @GetMapping("/get-substitute/{ingredientId}")
    public ResponseEntity<?> findIngredientSubstitute(@PathVariable Integer ingredientId){
        return ResponseEntity.status(200).body(new ApiResponse(ingredientService.findIngredientSubstitute(ingredientId)));
    }
}
