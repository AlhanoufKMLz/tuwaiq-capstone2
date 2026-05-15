package com.example.tuwaiqcapstone2.Controller;

import com.example.tuwaiqcapstone2.Api.ApiResponse;
import com.example.tuwaiqcapstone2.Model.Rating;
import com.example.tuwaiqcapstone2.Service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;


    //BASIC CRUD ENDPOINTS
    @GetMapping("/get")
    public ResponseEntity<?> getAllRatings(){
        return ResponseEntity.status(200).body(ratingService.getAllRating());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addRating(@RequestBody @Valid Rating rating, Errors errors){
        if(errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));

        ratingService.addRating(rating);
        return ResponseEntity.status(200).body(new ApiResponse("Rating added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRating(@PathVariable Integer id, @RequestBody @Valid Rating rating, Errors errors){
        if(errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));

        ratingService.updateRating(id, rating);
        return ResponseEntity.status(200).body(new ApiResponse("Rating updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable Integer id){
        ratingService.deleteRating(id);
        return ResponseEntity.status(200).body(new ApiResponse("Rating deleted successfully"));
    }


    //EXTRA ENDPOINTS
    @GetMapping("/get-recipe/{recipeId}")
    public ResponseEntity<?> findRatingByRecipeId(@PathVariable Integer recipeId){
        return ResponseEntity.status(200).body(ratingService.findRatingByRecipeId(recipeId));
    }

    @GetMapping("/get-user/{userId}")
    public ResponseEntity<?> findRatingByUserId(@PathVariable Integer userId){
        return ResponseEntity.status(200).body(ratingService.findRatingByUserId(userId));
    }
}
