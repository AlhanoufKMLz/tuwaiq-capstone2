package com.example.tuwaiqcapstone2.Controller;

import com.example.tuwaiqcapstone2.Api.ApiResponse;
import com.example.tuwaiqcapstone2.DTO.RecipeStepsRequest;
import com.example.tuwaiqcapstone2.Model.RecipeStep;
import com.example.tuwaiqcapstone2.Service.RecipeStepService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recipe-step")
@RequiredArgsConstructor
public class RecipeStepController {

    private final RecipeStepService recipeStepService;


    //BASIC CRUD ENDPOINTS
    @GetMapping("/get")
    public ResponseEntity<?> getAllRecipeSteps(){
        return ResponseEntity.status(200).body(recipeStepService.getAllRecipeSteps());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addRecipeStep(@RequestBody @Valid RecipeStep recipeStep, Errors errors){
        if(errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));

        recipeStepService.addRecipeStep(recipeStep);
        return ResponseEntity.status(200).body(new ApiResponse("Recipe Step added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRecipeStep(@PathVariable Integer id, @RequestBody @Valid RecipeStep recipeStep, Errors errors){
        if(errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));

        recipeStepService.updateRecipeStep(id, recipeStep);
        return ResponseEntity.status(200).body(new ApiResponse("Recipe Step updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRecipeStep(@PathVariable Integer id){
        recipeStepService.deleteRecipeStep(id);
        return ResponseEntity.status(200).body(new ApiResponse("Recipe Step deleted successfully"));
    }


    //EXTRA ENDPOINT
    @GetMapping("/get-recipe/{recipeId}")
    public ResponseEntity<?> findRecipeStepByRecipeId(@PathVariable Integer recipeId){
        return ResponseEntity.status(200).body(recipeStepService.findRecipeStepByRecipeId(recipeId));
    }

    @PostMapping("/generate/{recipeId}")
    public ResponseEntity<?> generateRecipeSteps(@PathVariable Integer recipeId, @RequestBody @Valid RecipeStepsRequest request) {
        recipeStepService.generateRecipeSteps(recipeId, request.getInstructions());
        return ResponseEntity.ok("Steps generated successfully");
    }
}
