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
    private final AiService aiService;


    //BASIC CRUD
    public List<RecipeStep> getAllRecipeSteps(){
        return recipeStepRepository.findAll();
    }

    public void addRecipeStep(RecipeStep recipeStep){
        checkRecipe(recipeStep.getRecipeId());

        recipeStepRepository.save(recipeStep);
    }

    public void updateRecipeStep(Integer id, RecipeStep recipeStep){
        RecipeStep oldRecipeStep = checkRecipeStep(id);
        checkRecipe(recipeStep.getRecipeId());

        oldRecipeStep.setRecipeId(recipeStep.getRecipeId());
        oldRecipeStep.setStepNumber(recipeStep.getStepNumber());
        oldRecipeStep.setInstruction(recipeStep.getInstruction());

        recipeStepRepository.save(oldRecipeStep);
    }

    public void deleteRecipeStep(Integer id){
        RecipeStep recipeStep = checkRecipeStep(id);

        recipeStepRepository.delete(recipeStep);
    }


    //EXTRA ENDPOINTS
    public List<RecipeStep> findRecipeStepByRecipeId(Integer recipeId){
        checkRecipe(recipeId);

        return recipeStepRepository.findRecipeStepByRecipeId(recipeId);
    }

    public void generateRecipeSteps(Integer recipeId, String instructions){
        checkRecipe(recipeId);

        //using AI to split the instructions paragraph into clear steps
        String prompt = "Given this recipe instructions paragraph: " + instructions +
                ". Split it into clear numbered steps. " +
                "Return ONLY the steps separated by newlines in this exact format: " +
                "1. step one\n2. step two\n3. step three " +
                "No extra text, no introduction, just the numbered steps.";
        String response = aiService.chat(prompt);
        String[] steps = response.split("\n");

        //add the steps to the database
        for (int i = 0; i < steps.length; i++) {
            String instruction = steps[i].replaceAll("^\\d+\\.\\s*", "").trim();
            if (!instruction.isEmpty()) {
                RecipeStep step = new RecipeStep();
                step.setRecipeId(recipeId);
                step.setStepNumber(i + 1);
                step.setInstruction(instruction);
                recipeStepRepository.save(step);
            }
        }
    }


    //HELPER METHODS
    private RecipeStep checkRecipeStep(Integer id){
        RecipeStep recipeStep = recipeStepRepository.findRecipeStepById(id);
        if(recipeStep == null) throw new ApiException("Recipe Step not found"); //check recipeStep
        return recipeStep;
    }

    private void checkRecipe(Integer id){
        Recipe recipe = recipeRepository.findRecipeById(id);
        if(recipe == null) throw new ApiException("Recipe not found"); //check recipe
    }
}
