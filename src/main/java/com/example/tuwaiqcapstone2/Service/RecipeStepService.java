package com.example.tuwaiqcapstone2.Service;

import com.example.tuwaiqcapstone2.Api.ApiException;
import com.example.tuwaiqcapstone2.Model.Recipe;
import com.example.tuwaiqcapstone2.Model.RecipeStep;
import com.example.tuwaiqcapstone2.Repository.RecipeRepository;
import com.example.tuwaiqcapstone2.Repository.RecipeStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeStepService {

    private final RecipeStepRepository recipeStepRepository;
    private final RecipeRepository recipeRepository;


    //BASIC CRUD
    public List<RecipeStep> getAllRecipeSteps(){
        return recipeStepRepository.findAll();
    }

    public void addRecipeStep(RecipeStep recipeStep){
        Recipe recipe = recipeRepository.findRecipeById(recipeStep.getRecipeId());
        if(recipe == null) throw new ApiException("Recipe not found"); //check recipe

        recipeStepRepository.save(recipeStep);
    }

    public void updateRecipeStep(Integer id, RecipeStep recipeStep){
        RecipeStep oldRecipeStep = recipeStepRepository.findRecipeStepById(id);
        if(oldRecipeStep == null) throw new ApiException("Recipe Step not found"); //check recipeStep

        Recipe recipe = recipeRepository.findRecipeById(recipeStep.getRecipeId());
        if(recipe == null) throw new ApiException("Recipe not found"); //check recipe

        oldRecipeStep.setRecipeId(recipeStep.getRecipeId());
        oldRecipeStep.setStepNumber(recipeStep.getStepNumber());
        oldRecipeStep.setInstruction(recipeStep.getInstruction());

        recipeStepRepository.save(oldRecipeStep);
    }

    public void deleteRecipeStep(Integer id){
        RecipeStep recipeStep = recipeStepRepository.findRecipeStepById(id);
        if(recipeStep == null) throw new ApiException("Recipe Step not found"); //check recipeStep

        recipeStepRepository.delete(recipeStep);
    }
}
