package com.example.tuwaiqcapstone2.Controller;

import com.example.tuwaiqcapstone2.Api.ApiResponse;
import com.example.tuwaiqcapstone2.Model.Favorite;
import com.example.tuwaiqcapstone2.Service.FavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;


    //BASIC CRUD ENDPOINTS
    @GetMapping("/get")
    public ResponseEntity<?> getAllFavorites(){
        return ResponseEntity.status(200).body(favoriteService.getAllFavorites());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addFavorite(@RequestBody @Valid Favorite favorite, Errors errors){
        if(errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));

        favoriteService.addFavorite(favorite);
        return ResponseEntity.status(200).body(new ApiResponse("Favorite added successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFavorite(@PathVariable Integer id){
        favoriteService.deleteFavorite(id);
        return ResponseEntity.status(200).body(new ApiResponse("Favorite deleted successfully"));
    }


    //EXTRA ENDPOINTS
    @GetMapping("/get-recipe/{recipeId}")
    public ResponseEntity<?> findFavoriteByRecipeId(@PathVariable Integer recipeId){
        return ResponseEntity.status(200).body(favoriteService.findFavoriteByRecipeId(recipeId));
    }

    @GetMapping("/get-user/{userId}")
    public ResponseEntity<?> findFavoriteByUserId(@PathVariable Integer userId){
        return ResponseEntity.status(200).body(favoriteService.findFavoriteByRecipeId(userId));
    }
}
