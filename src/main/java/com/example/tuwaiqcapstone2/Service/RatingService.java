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
        User user = userRepository.findUserById(rating.getUserId());
        if(user == null) throw new ApiException("User not found"); //check user
        Recipe recipe = recipeRepository.findRecipeById(rating.getRecipeId());
        if(recipe == null) throw new ApiException("Recipe not found"); //check recipe

        ratingRepository.save(rating);
    }

    public void updateRating(Integer id, Rating rating){
        Rating oldRating = ratingRepository.findRatingById(id);
        if(oldRating == null) throw new ApiException("Rating not found"); //check rating

        User user = userRepository.findUserById(rating.getUserId());
        if(user == null) throw new ApiException("User not found"); //check user
        Recipe recipe = recipeRepository.findRecipeById(rating.getRecipeId());
        if(recipe == null) throw new ApiException("Recipe not found"); //check recipe

        oldRating.setRecipeId(rating.getRecipeId());
        oldRating.setUserId(rating.getUserId());
        oldRating.setRatingValue(rating.getRatingValue());
        ratingRepository.save(oldRating);
    }

    public void deleteRating(Integer id){
        Rating rating = ratingRepository.findRatingById(id);
        if(rating == null) throw new ApiException("Rating not found"); //check rating

        ratingRepository.delete(rating);
    }
}
