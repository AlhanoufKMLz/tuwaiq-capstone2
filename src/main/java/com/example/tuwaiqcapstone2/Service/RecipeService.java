package com.example.tuwaiqcapstone2.Service;

import com.example.tuwaiqcapstone2.Api.ApiException;
import com.example.tuwaiqcapstone2.DTO.*;
import com.example.tuwaiqcapstone2.Enums.AllergenType;
import com.example.tuwaiqcapstone2.Enums.DifficultyLevel;
import com.example.tuwaiqcapstone2.Enums.LanguageCode;
import com.example.tuwaiqcapstone2.Enums.UnitType;
import com.example.tuwaiqcapstone2.Model.*;
import com.example.tuwaiqcapstone2.Repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final FollowRepository followRepository;
    private final EmailSenderService emailSenderService;
    private final IngredientRepository ingredientRepository;
    private final RecipeStepRepository recipeStepRepository;
    private final RatingRepository ratingRepository;
    private final CommentRepository commentRepository;
    private final AiService aiService;
    private final TranslationService translationService;
    private final WhatsappService whatsappService;


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

    public List<Recipe> findRecipesSortedByMostComments(){
        List<Recipe> recipes = recipeRepository.findRecipesSortedByMostComments();

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

    public List<Recipe> findSafeRecipesForUser(Integer userId){
        checkUser(userId);

        List<Recipe> recipes = recipeRepository.findSafeRecipesForUser(userId);

        if(recipes.isEmpty()) throw new ApiException("No recipes found");

        return recipes;
    }

    public List<Recipe> findRecipesFeed(Integer userId){
        checkUser(userId);

        List<Recipe> recipes = recipeRepository.findRecipesFeed(userId);

        if(recipes.isEmpty()) throw new ApiException("No recipes found");

        return recipes;
    }

    public RecipeDetailsResponse getRecipeDetails(Integer recipeId) {
        Recipe recipe = checkRecipe(recipeId);

        RecipeDetailsResponse response = new RecipeDetailsResponse();
        response.setRecipe(recipe);
        response.setIngredients(ingredientRepository.findIngredientByRecipeId(recipeId));
        response.setSteps(recipeStepRepository.findRecipeStepsByRecipeIdOrderByStepNumberAsc(recipeId));
        response.setAverageRating(ratingRepository.findAverageRatingByRecipeId(recipeId));
        response.setComments(commentRepository.findCommentByRecipeId(recipeId));

        return response;
    }

    public NutritionAnalysisResponse analyzeRecipeNutrition(Integer recipeId) {
        checkRecipe(recipeId);
        List<Ingredient> ingredients = checkIngredient(recipeId);

        //convert the ingredients into string
        String ingredientsList = ingredients.stream()
                .map(i -> i.getAmount() + " " + i.getUnit() + " " + i.getName())
                .collect(Collectors.joining(", "));

        //Send the ingredient to AI
        String prompt = "Analyze the nutrition for a recipe with these ingredients: " + ingredientsList +
                ". Return ONLY a JSON object with these exact keys: calories, protein, carbs, fats, sugar, sodium. " +
                "All values must be numbers. Example: {\"calories\": 450, \"protein\": 20.5, \"carbs\": 35.0, \"fats\": 15.0, \"sugar\": 8.0, \"sodium\": 320.0}";

        String response = aiService.chat(prompt);
        //convert json response into NutritionAnalysis object
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response, NutritionAnalysisResponse.class);
        } catch (Exception e) {
            throw new ApiException("Failed to parse nutrition analysis");
        }
    }

    public GenerateRecipeResponse generateRecipe(GenerateRecipeRequest request){
        //check if the user allows extra ingredients
        String extra = request.getAllowExtraIngredients()
                ? "You can add extra ingredients if needed to complete the recipe."
                : "Use ONLY the provided ingredients, do not add anything else.";

        //send the request to AI
        String prompt = "Given these ingredients: " + request.getIngredients() +
                ". " + extra +
                " Generate a complete recipe. Return ONLY a JSON object with these exact keys: " +
                "name, description, cookTime (number in minutes), difficulty (EASY, MEDIUM, or HARD), servings (number), " +
                "ingredients (array of objects with: name, amount (number), unit must be one of: " + Arrays.toString(UnitType.values()) + "), " +
                "steps (array of objects with: stepNumber, instruction). " +
                "Example: {\"name\": \"Pasta\", \"description\": \"Delicious\", \"cookTime\": 30, \"difficulty\": \"EASY\", \"servings\": 4, " +
                "\"ingredients\": [{\"name\": \"pasta\", \"amount\": 400, \"unit\": \"GRAM\"}], " +
                "\"steps\": [{\"stepNumber\": 1, \"instruction\": \"Boil water\"}]}";

        String response = aiService.chat(prompt);

        //convert json response into GenerateRecipeResponse object
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.readValue(response, GenerateRecipeResponse.class);
        } catch (Exception e) {
            throw new ApiException("Failed to generate recipe");
        }
    }

    public RecipeDetailsResponse convertServings(Integer recipeId, Integer newServings){
        RecipeDetailsResponse recipeDetails = getRecipeDetails(recipeId);

        String prompt = "Given this recipe: " + recipeDetailsToString(recipeDetails) +
                ". Convert all ingredient amounts to serve " + newServings + " people instead of " + recipeDetails.getRecipe().getServings() + "." +
                " Return ONLY the same JSON structure with updated ingredient amounts and servings number. No extra text.";

        String response = aiService.chat(prompt);

        //convert json response into GenerateRecipeResponse object
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response, RecipeDetailsResponse.class);
        } catch (JsonProcessingException e){
            throw new ApiException("Failed to convert recipe");
        }
    }

    public TranslateRecipeResponse translateRecipe(Integer recipeId, LanguageCode language){
        RecipeDetailsResponse original = getRecipeDetails(recipeId);

        TranslateRecipeResponse translated = new TranslateRecipeResponse();
        translated.setAllergens(new ArrayList<>());
        translated.setIngredients(new ArrayList<>());
        translated.setSteps(new ArrayList<>());

        translated.setRecipeName(translationService.translate(original.getRecipe().getName(), language));
        translated.setDescription(translationService.translate(original.getRecipe().getDescription(), language));
        translated.setCookTime(original.getRecipe().getCookTime());
        translated.setDifficulty(translationService.translate(original.getRecipe().getDifficulty().toString(), language));
        translated.setServings(original.getRecipe().getServings());

        //translate allergens
        for(AllergenType a: original.getRecipe().getAllergens()){
            translated.getAllergens().add(translationService.translate(a.toString(), language));
        }

        //translate ingredients
        for (Ingredient i: original.getIngredients()){
            IngredientResponse ingredient = new IngredientResponse();
            ingredient.setName(translationService.translate(i.getName(), language));
            ingredient.setAmount(i.getAmount());
            ingredient.setUnit(translationService.translate(i.getUnit().toString(), language));

            translated.getIngredients().add(ingredient);
        }

        //translate steps
        for (RecipeStep s: original.getSteps()){
            RecipeStepResponse step = new RecipeStepResponse();
            step.setStepNumber(s.getStepNumber());
            step.setInstruction(translationService.translate(s.getInstruction(), language));
            translated.getSteps().add(step);
        }

        return translated;
    }

    public void translateAndShare(Integer recipeId, LanguageCode language, String phoneNumber){
        TranslateRecipeResponse recipe = translateRecipe(recipeId, language);

        whatsappService.shareRecipe(recipe, phoneNumber);
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

    private List<Ingredient> checkIngredient(Integer recipeId){
        List<Ingredient> ingredients = ingredientRepository.findIngredientByRecipeId(recipeId);

        if(ingredients.isEmpty()) throw new ApiException("No ingredients found");

        return ingredients;
    }

    private String recipeDetailsToString(RecipeDetailsResponse details) {
        Recipe r = details.getRecipe();
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        sb.append("\"recipe\": {");
        sb.append("\"name\": \"").append(r.getName()).append("\", ");
        sb.append("\"description\": \"").append(r.getDescription()).append("\", ");
        sb.append("\"cookTime\": ").append(r.getCookTime()).append(", ");
        sb.append("\"difficulty\": \"").append(r.getDifficulty()).append("\", ");
        sb.append("\"servings\": ").append(r.getServings());
        sb.append("}, ");

        sb.append("\"ingredients\": [");
        for (int i = 0; i < details.getIngredients().size(); i++) {
            Ingredient ing = details.getIngredients().get(i);
            sb.append("{\"name\": \"").append(ing.getName()).append("\", ");
            sb.append("\"amount\": ").append(ing.getAmount()).append(", ");
            sb.append("\"unit\": \"").append(ing.getUnit()).append("\"}");
            if (i < details.getIngredients().size() - 1) sb.append(", ");
        }
        sb.append("], ");

        sb.append("\"steps\": [");
        for (int i = 0; i < details.getSteps().size(); i++) {
            RecipeStep step = details.getSteps().get(i);
            sb.append("{\"stepNumber\": ").append(step.getStepNumber()).append(", ");
            sb.append("\"instruction\": \"").append(step.getInstruction()).append("\"}");
            if (i < details.getSteps().size() - 1) sb.append(", ");
        }
        sb.append("]}");

        return sb.toString();
    }
}
