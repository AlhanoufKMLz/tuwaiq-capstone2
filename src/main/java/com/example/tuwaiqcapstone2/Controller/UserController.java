package com.example.tuwaiqcapstone2.Controller;

import com.example.tuwaiqcapstone2.Api.ApiResponse;
import com.example.tuwaiqcapstone2.Enums.LanguageCode;
import com.example.tuwaiqcapstone2.Model.User;
import com.example.tuwaiqcapstone2.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    //BASIC CRUD ENDPOINTS
    @GetMapping("/get")
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.status(200).body(userService.getAllUsers());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid User user){
        userService.addUser(user);
        return ResponseEntity.status(200).body(new ApiResponse("User added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody @Valid User user){
        userService.updateUser(id, user);
        return ResponseEntity.status(200).body(new ApiResponse("User updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
        return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));
    }


    //EXTRA ENDPOINTS
    @GetMapping("/get-most-recipe")
    public ResponseEntity<?> findUsersSortedByMostRecipes(){
        return ResponseEntity.status(200).body(userService.findUsersSortedByMostRecipes());
    }

    @GetMapping("/sort-top")
    public ResponseEntity<?> getUsersSortedByRecipeRating(){
        return ResponseEntity.status(200).body(userService.getUsersSortedByRecipeRating());
    }

    @GetMapping("/get-followers/{userId}")
    public ResponseEntity<?> findFollowers(@PathVariable Integer userId){
        return ResponseEntity.status(200).body(userService.findFollowers(userId));
    }

    @GetMapping("/get-followings/{userId}")
    public ResponseEntity<?> findFollowings(@PathVariable Integer userId){
        return ResponseEntity.status(200).body(userService.findFollowings(userId));
    }

    @GetMapping("/get-mutual/{userId}")
    public ResponseEntity<?> findMutualFollows(@PathVariable Integer userId){
        return ResponseEntity.status(200).body(userService.findMutualFollows(userId));
    }

    @GetMapping("/shopping-list/{userId}")
    public ResponseEntity<?> generateShoppingList(@PathVariable Integer userId){
        userService.generateShoppingList(userId);
        return ResponseEntity.status(200).body(new ApiResponse("Shopping list sent to whatsApp successfully"));
    }

    @PutMapping("/subscribe/{userId}/{time}/{language}")
    public ResponseEntity<?> subscribeToDailyRecipes(@PathVariable Integer userId, @PathVariable @DateTimeFormat(pattern = "HH:mm") LocalTime time, @PathVariable LanguageCode language) {
        userService.subscribeToDailyRecipes(userId, time, language);
        return ResponseEntity.status(200).body(new ApiResponse("Subscribed successfully"));
    }

    @PutMapping("/unsubscribe/{userId}")
    public ResponseEntity<?> unsubscribeToDailyRecipes(@PathVariable Integer userId) {
        userService.unsubscribeToDailyRecipes(userId);
        return ResponseEntity.status(200).body(new ApiResponse("Unsubscribed successfully"));
    }
}
