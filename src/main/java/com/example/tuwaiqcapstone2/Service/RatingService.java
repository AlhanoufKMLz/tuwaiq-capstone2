package com.example.tuwaiqcapstone2.Service;

import com.example.tuwaiqcapstone2.Api.ApiException;
import com.example.tuwaiqcapstone2.Model.Rating;
import com.example.tuwaiqcapstone2.Model.Recipe;
import com.example.tuwaiqcapstone2.Model.User;
import com.example.tuwaiqcapstone2.Repository.RatingRepository;
import com.example.tuwaiqcapstone2.Repository.RecipeRepository;
import com.example.tuwaiqcapstone2.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;


    //BASIC CRUD
    public List<Rating> getAllRating(){
        return ratingRepository.findAll();
    }

    public void addRating(Rating rating){
        checkRecipe(rating.getRecipeId());
        checkUser(rating.getUserId());

        ratingRepository.save(rating);
    }

    public void updateRating(Integer id, Rating rating){
        Rating oldRating = checkRating(id);

        checkRecipe(rating.getRecipeId());
        checkUser(rating.getUserId());

        oldRating.setRecipeId(rating.getRecipeId());
        oldRating.setUserId(rating.getUserId());
        oldRating.setRatingValue(rating.getRatingValue());
        ratingRepository.save(oldRating);
    }

    public void deleteRating(Integer id){
        Rating rating = checkRating(id);

        ratingRepository.delete(rating);
    }


    //EXTRA ENDPOINTS
    public List<Rating> findRatingByRecipeId(Integer recipeId){
        checkRecipe(recipeId);

        List<Rating> ratings = ratingRepository.findRatingByRecipeId(recipeId);

        if(ratings.isEmpty()) throw new ApiException("No ratings found");

        return ratings;
    }

    public List<Rating> findRatingByUserId(Integer userId){
        checkUser(userId);

        List<Rating> ratings = ratingRepository.findRatingByUserId(userId);

        if(ratings.isEmpty()) throw new ApiException("No ratings found");

        return ratings;
    }


    //HELPER METHODS
    private Rating checkRating(Integer id){
        Rating rating = ratingRepository.findRatingById(id);
        if(rating == null) throw new ApiException("Rating not found"); //check rating

        return rating;
    }

    private void checkRecipe(Integer id){
        Recipe recipe = recipeRepository.findRecipeById(id);
        if(recipe == null) throw new ApiException("Recipe not found"); //check recipe
    }

    private void checkUser(Integer id){
        User user = userRepository.findUserById(id);
        if(user == null) throw new ApiException("User not found"); //check user
    }
}
