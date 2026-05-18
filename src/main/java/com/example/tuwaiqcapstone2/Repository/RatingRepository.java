package com.example.tuwaiqcapstone2.Repository;

import com.example.tuwaiqcapstone2.Model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {

    Rating findRatingById(Integer id);

    List<Rating> findRatingByRecipeId(Integer recipeId);

    List<Rating> findRatingByUserId(Integer userId);

    @Query("SELECT AVG(r.ratingValue) FROM Rating r WHERE r.recipeId = ?1")
    Double findAverageRatingByRecipeId(Integer recipeId);
}
