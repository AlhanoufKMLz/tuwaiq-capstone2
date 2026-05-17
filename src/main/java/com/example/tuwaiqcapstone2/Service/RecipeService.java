package com.example.tuwaiqcapstone2.Service;

import com.example.tuwaiqcapstone2.Api.ApiException;
import com.example.tuwaiqcapstone2.Enums.DifficultyLevel;
import com.example.tuwaiqcapstone2.Model.Category;
import com.example.tuwaiqcapstone2.Model.Follow;
import com.example.tuwaiqcapstone2.Model.Recipe;
import com.example.tuwaiqcapstone2.Model.User;
import com.example.tuwaiqcapstone2.Repository.CategoryRepository;
import com.example.tuwaiqcapstone2.Repository.FollowRepository;
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
    private final FollowRepository followRepository;
    private final EmailSenderService emailSenderService;

    //BASIC CRUD
    public List<Recipe> getAllRecipes(){
        return recipeRepository.findAll();
    }

    public void addRecipe(Recipe recipe){
        User user = checkUser(recipe.getUserId());
        checkCategory(recipe.getCategoryId());

        recipeRepository.save(recipe);

        notifyFollowers(user, recipe);
    }

    public void updateRecipe(Integer id, Recipe recipe){
        Recipe oldRecipe = checkRecipe(id);
        checkUser(recipe.getUserId());
        checkCategory(recipe.getCategoryId());

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
        Recipe recipe = checkRecipe(id);
        recipeRepository.delete(recipe);
    }


    //EXTRA ENDPOINTS
    public List<Recipe> findRecipeByCategoryId(Integer categoryId){
        checkCategory(categoryId);

        List<Recipe> recipes = recipeRepository.findRecipeByCategoryId(categoryId);

        if(recipes.isEmpty()) throw new ApiException("No recipes found");

        return recipes;
    }

    public List<Recipe> findRecipeByUserId(Integer userId){
        checkUser(userId);

        List<Recipe> recipes = recipeRepository.findRecipeByUserId(userId);

        if(recipes.isEmpty()) throw new ApiException("No recipes found");

        return recipes;
    }

    public List<Recipe> findRecipeByDifficulty(DifficultyLevel difficulty){
        List<Recipe> recipes = recipeRepository.findRecipeByDifficulty(difficulty);

        if(recipes.isEmpty()) throw new ApiException("No recipes found");

        return recipes;
    }

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

    public List<Recipe> getRecipesWithCookTimeLessThan(Integer minutes){
        if(minutes < 0) throw new ApiException("Minutes must be positive number");

        List<Recipe> recipes = recipeRepository.findRecipesWithCookTimeLessThan(minutes);

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

    public List<Recipe> findTopRatedRecipesThisWeek(){
        List<Recipe> recipes = recipeRepository.findTopRatedRecipesThisWeek();

        if(recipes.isEmpty()) throw new ApiException("No recipes found");

        return recipes;
    }

    public List<Recipe> findUserFavoritesRecipes(Integer userId){
        checkUser(userId);

        List<Recipe> recipes = recipeRepository.findUserFavoritesRecipes(userId);

        if(recipes.isEmpty()) throw new ApiException("No recipes found");

        return recipes;
    }

    public List<Recipe> findSimilarRecipes(Integer recipeId){
        checkRecipe(recipeId);

        List<Recipe> recipes = recipeRepository.findSimilarRecipes(recipeId);

        if(recipes.isEmpty()) throw new ApiException("No recipes found");

        return recipes;
    }


    //HELPER METHODS
    private Recipe checkRecipe(Integer id){
        Recipe recipe = recipeRepository.findRecipeById(id);
        if(recipe == null) throw new ApiException("Recipe not found"); //check recipe
        return recipe;
    }

    private User checkUser(Integer id){
        User user = userRepository.findUserById(id);
        if(user == null) throw new ApiException("User not found"); //check user

        return user;
    }

    private void checkCategory(Integer id){
        Category category = categoryRepository.findCategoryById(id);
        if(category == null) throw new ApiException("Category not found"); //check category
    }

    private void notifyFollowers(User following, Recipe recipe){
        List<Follow> follows = followRepository.findFollowByFollowingId(following.getId());

        for(Follow f: follows){
            User follower = userRepository.findUserById(f.getFollowerId());

            emailSenderService.sendEmail(follower.getEmail(), "New Recipe Alert from " + following.getName() + " on RecipeHub!",
                    "Hi " + follower.getName() + ",\n" +
                            "\n" +
                            following.getName() + " just added a new recipe on RecipeHub!\n" +
                            "\n" +
                            "Recipe: " + recipe.getName() + "\n" +
                            "Difficulty: " + recipe.getDifficulty() + "\n" +
                            "Cook Time: " + recipe.getCookTime() + " minutes\n" +
                            "Servings: " + recipe.getServings() + "\n" +
                            "\n" +
                            "Check it out now on RecipeHub!\n" +
                            "\n" +
                            "Happy Cooking!\n" +
                            "RecipeHub Team");
        }
    }
}
