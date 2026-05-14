package com.example.tuwaiqcapstone2.Repository;

import com.example.tuwaiqcapstone2.Enums.DifficultyLevel;
import com.example.tuwaiqcapstone2.Model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    Recipe findRecipeById(Integer id);

    List<Recipe> findRecipeByCategoryId(Integer category);

    List<Recipe> findRecipeByUserId(Integer userId);

    List<Recipe> findRecipeByDifficulty(DifficultyLevel difficulty);

    List<Recipe> findRecipeByAllergensEmpty();

    @Query("SELECT r FROM Recipe r WHERE r.name LIKE %?1%")
    List<Recipe> findRecipeByNameKeyword(String keyword);

    @Query("SELECT r FROM Recipe r WHERE (r.cookTime <  ?1)")
    List<Recipe> getRecipesWithCookTimeLessThan(Integer cookTime);

    @Query("SELECT r FROM Recipe r WHERE r.id = (SELECT c.recipeId FROM Comment c GROUP BY c.recipeId ORDER BY count(c) desc limit 1)")
    List<Recipe> findRecipeWithMostComments();

    @Query("SELECT r FROM Recipe r WHERE r.id IN (SELECT rt.recipeId FROM Rating rt WHERE rt.ratingValue > (SELECT AVG(rt2.ratingValue) FROM Rating rt2))")
    List<Recipe> findTopRatedRecipes();

}
