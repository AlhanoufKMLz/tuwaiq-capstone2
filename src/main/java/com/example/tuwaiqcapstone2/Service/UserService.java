package com.example.tuwaiqcapstone2.Service;

import com.example.tuwaiqcapstone2.Api.ApiException;
import com.example.tuwaiqcapstone2.Enums.LanguageCode;
import com.example.tuwaiqcapstone2.Model.Follow;
import com.example.tuwaiqcapstone2.Model.Ingredient;
import com.example.tuwaiqcapstone2.Model.Recipe;
import com.example.tuwaiqcapstone2.Model.User;
import com.example.tuwaiqcapstone2.Repository.FollowRepository;
import com.example.tuwaiqcapstone2.Repository.IngredientRepository;
import com.example.tuwaiqcapstone2.Repository.RecipeRepository;
import com.example.tuwaiqcapstone2.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final WhatsappService whatsappService;


    //BASIC CRUD
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void addUser(User user){
        userRepository.save(user);
    }

    public void updateUser(Integer id, User user){
        User oldUser = checkUser(id);

        oldUser.setName(user.getName());
        oldUser.setUsername(user.getUsername());
        oldUser.setPassword(user.getPassword());
        oldUser.setEmail(user.getEmail());
        oldUser.setAge(user.getAge());
        oldUser.setPhoneNumber(user.getPhoneNumber());
        oldUser.setAllergens(user.getAllergens());
        oldUser.setDailyRecipeSubscribed(user.getDailyRecipeSubscribed());
        oldUser.setDailyRecipeTime(user.getDailyRecipeTime());
        userRepository.save(oldUser);
    }

    public void deleteUser(Integer id){
        User user = checkUser(id);

        userRepository.delete(user);
    }


    //EXTRA ENDPOINTS
    public List<User> findUsersSortedByMostRecipes(){
        List<User> users = userRepository.findUsersSortedByMostRecipes();

        if(users.isEmpty()) throw new ApiException("No users found");

        return users;
    }

    public List<User> getUsersSortedByRecipeRating(){
        List<User> sortedUsers = userRepository.getUsersSortedByRecipeRating();

        if(sortedUsers.isEmpty()) throw new ApiException("No users found");

        return sortedUsers;
    }

    public List<User> findFollowers(Integer userId){
        checkUser(userId);

        List<Follow> follows = followRepository.findFollowByFollowingId(userId);

        if(follows.isEmpty()) throw new ApiException("No followers found");

        List<User> followers = new ArrayList<>();

        for(Follow f: follows){
            followers.add(userRepository.findUserById(f.getFollowerId()));
        }
        return followers;
    }

    public List<User> findFollowings(Integer userId){
        checkUser(userId);

        List<Follow> follows = followRepository.findFollowByFollowerId(userId);

        if(follows.isEmpty()) throw new ApiException("No followings found");

        List<User> followings = new ArrayList<>();

        for(Follow f: follows){
            followings.add(userRepository.findUserById(f.getFollowingId()));
        }
        return followings;
    }

    public List<User> findMutualFollows(Integer userId){
        checkUser(userId);

        List<User> users = userRepository.findMutualFollows(userId);

        if(users.isEmpty()) throw new ApiException("No users found");

        return users;
    }

    public void generateShoppingList(Integer userId){
        User user = checkUser(userId);
        List<Recipe> userFavorites = recipeRepository.findUserFavoritesRecipes(userId);
        if(userFavorites.isEmpty()) throw new ApiException("No recipes found in the favorite");

        List<String> shoppingList = new ArrayList<>();

        for(Recipe r: userFavorites){
            List<Ingredient> recipeIngredients = ingredientRepository.findIngredientByRecipeId(r.getId());
            for(Ingredient i: recipeIngredients){
               if(!shoppingList.contains(i.getName()))
                   shoppingList.add(i.getName());
            }
        }
        whatsappService.sendShoppingList(user.getPhoneNumber(), shoppingList);

    }

    public void subscribeToDailyRecipes(Integer userId , LocalTime time, LanguageCode language){
        User user = checkUser(userId);

        if(Boolean.TRUE.equals(user.getDailyRecipeSubscribed())) throw new ApiException("You already subscribed to daily recipes");

        user.setDailyRecipeSubscribed(true);
        user.setDailyRecipeTime(time);
        user.setDailyRecipeLanguage(language);
        userRepository.save(user);
    }

    public void unsubscribeToDailyRecipes(Integer userId){
        User user = checkUser(userId);

        if(!user.getDailyRecipeSubscribed()) throw new ApiException("You are not subscribed to daily recipes");

        user.setDailyRecipeSubscribed(false);
        userRepository.save(user);
    }


    //HELPER METHOD
    private User checkUser(Integer id){
        User user = userRepository.findUserById(id);
        if(user == null) throw new ApiException("User not found"); //check user

        return user;
    }
}
