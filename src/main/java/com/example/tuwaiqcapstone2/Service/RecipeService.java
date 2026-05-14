package com.example.tuwaiqcapstone2.Service;

import com.example.tuwaiqcapstone2.Api.ApiException;
import com.example.tuwaiqcapstone2.Model.Category;
import com.example.tuwaiqcapstone2.Model.Recipe;
import com.example.tuwaiqcapstone2.Model.User;
import com.example.tuwaiqcapstone2.Repository.CategoryRepository;
import com.example.tuwaiqcapstone2.Repository.RecipeRepository;
import com.example.tuwaiqcapstone2.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    //BASIC CRUD
    public List<Recipe> getAllRecipes(){
        return recipeRepository.findAll();
    }

    public void addRecipe(Recipe recipe){
        User user = userRepository.findUserById(recipe.getUserId());
        if(user == null) throw new ApiException("User not found"); //check user
        Category category = categoryRepository.findCategoryById(recipe.getCategoryId());
        if(category == null) throw new ApiException("Category not found"); //check category

        recipeRepository.save(recipe);
    }

    public void updateRecipe(Integer id, Recipe recipe){
        Recipe oldRecipe = recipeRepository.findRecipeById(id);
        if(oldRecipe == null) throw new ApiException("Recipe not found"); //check recipe

        User user = userRepository.findUserById(recipe.getUserId());
        if(user == null) throw new ApiException("User not found"); //check user
        Category category = categoryRepository.findCategoryById(recipe.getCategoryId());
        if(category == null) throw new ApiException("Category not found"); //check category

        oldRecipe.setUserId(recipe.getUserId());
        oldRecipe.setCategoryId(recipe.getCategoryId());
        oldRecipe.setName(recipe.getName());
        oldRecipe.setDescription(recipe.getDescription());
        oldRecipe.setCookTime(recipe.getCookTime());
        oldRecipe.setDifficulty(recipe.getDifficulty());
        oldRecipe.setServings(recipe.getServings());
        oldRecipe.setAllergens(recipe.getAllergens());
        recipeRepository.save(oldRecipe);
    }

    public void deleteRecipe(Integer id){
        Recipe recipe = recipeRepository.findRecipeById(id);
        if(recipe == null) throw new ApiException("Recipe not found"); //check recipe

        recipeRepository.delete(recipe);
    }


    //EXTRA ENDPOINTS
    public List<Recipe> findRecipeWithNoAllergens(){
        List<Recipe> recipes = recipeRepository.findRecipeByAllergensEmpty();

        if(recipes.isEmpty()) throw new ApiException("No recipes found");

        return recipes;
    }

    public List<Recipe> findRecipeByName(String keyword){
        List<Recipe> recipes = recipeRepository.findRecipeByNameKeyword(keyword);

        if(recipes.isEmpty()) throw new ApiException("Recipe not found");

        return recipes;
    }

    public List<Recipe> findRecipeWithMostComments(){
        List<Recipe> recipes = recipeRepository.findRecipeWithMostComments();

        if(recipes.isEmpty()) throw new ApiException("No recipes found");

        return recipes;
    }

    public List<Recipe> findTopRatedRecipes(){
        List<Recipe> recipes = recipeRepository.findTopRatedRecipes();

        if(recipes.isEmpty()) throw new ApiException("No recipes found");

        return recipes;
    }
}
