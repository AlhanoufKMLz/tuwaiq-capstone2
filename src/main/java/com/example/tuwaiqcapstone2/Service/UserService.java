package com.example.tuwaiqcapstone2.Service;

import com.example.tuwaiqcapstone2.Api.ApiException;
import com.example.tuwaiqcapstone2.Model.Ingredient;
import com.example.tuwaiqcapstone2.Model.Recipe;
import com.example.tuwaiqcapstone2.Model.User;
import com.example.tuwaiqcapstone2.Repository.IngredientRepository;
import com.example.tuwaiqcapstone2.Repository.RecipeRepository;
import com.example.tuwaiqcapstone2.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
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


    //HELPER METHOD
    private User checkUser(Integer id){
        User user = userRepository.findUserById(id);
        if(user == null) throw new ApiException("User not found"); //check user

        return user;
    }
}
