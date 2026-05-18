package com.example.tuwaiqcapstone2.Service;

import com.example.tuwaiqcapstone2.Api.ApiException;
import com.example.tuwaiqcapstone2.Enums.AllergenType;
import com.example.tuwaiqcapstone2.Model.Favorite;
import com.example.tuwaiqcapstone2.Model.Recipe;
import com.example.tuwaiqcapstone2.Model.User;
import com.example.tuwaiqcapstone2.Repository.FavoriteRepository;
import com.example.tuwaiqcapstone2.Repository.RecipeRepository;
import com.example.tuwaiqcapstone2.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final WhatsappService whatsappService;


    //BASIC CRUD
    public List<Favorite> getAllFavorites(){
        return favoriteRepository.findAll();
    }

    public void addFavorite(Favorite favorite){
        Recipe recipe = checkRecipe(favorite.getRecipeId());
        User user  = checkUser(favorite.getUserId());

        //check allergens
        List<AllergenType> userAllergens = user.getAllergens();
        List<AllergenType> recipeAllergens = recipe.getAllergens();

        List<AllergenType> commonAllergens = recipeAllergens.stream()
                .filter(userAllergens::contains)
                .toList();

        if (!commonAllergens.isEmpty()) {
            //send whatsApp warning
            whatsappService.sendAllergenWarning(user, recipe, commonAllergens);
        }

        favoriteRepository.save(favorite);
    }

    public void updateFavorite(Integer id, Favorite favorite){
        Favorite oldFavorite = checkFavorite(id);
        checkRecipe(favorite.getRecipeId());
        checkUser(favorite.getUserId());

        oldFavorite.setUserId(favorite.getUserId());
        oldFavorite.setRecipeId(favorite.getRecipeId());
        favoriteRepository.save(oldFavorite);
    }

    public void deleteFavorite(Integer id){
        Favorite favorite = checkFavorite(id);

        favoriteRepository.delete(favorite);
    }


    //EXTRA ENDPOINTS
    public List<Favorite> findFavoriteByRecipeId(Integer recipeId){
        checkRecipe(recipeId);

        List<Favorite> favorites = favoriteRepository.findFavoriteByRecipeId(recipeId);

        if(favorites.isEmpty()) throw new ApiException("No favorites found");

        return favorites;
    }

    public List<Favorite> findFavoriteByUserId(Integer userId){
        checkRecipe(userId);

        List<Favorite> favorites = favoriteRepository.findFavoriteByUserId(userId);

        if(favorites.isEmpty()) throw new ApiException("No favorites found");

        return favorites;
    }


    //HELPER METHODS
    private Favorite checkFavorite(Integer id){
        Favorite favorite = favoriteRepository.findFavoriteById(id);
        if(favorite == null) throw new ApiException("Favorite not found"); //check favorite
        return favorite;
    }

    private User checkUser(Integer id){
        User user = userRepository.findUserById(id);
        if(user == null) throw new ApiException("User not found"); //user recipe

        return user;
    }

    private Recipe checkRecipe(Integer id){
        Recipe recipe = recipeRepository.findRecipeById(id);
        if(recipe == null) throw new ApiException("Recipe not found"); //check recipe

        return recipe;
    }
}
