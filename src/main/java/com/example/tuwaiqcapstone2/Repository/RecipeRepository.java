package com.example.tuwaiqcapstone2.Repository;

import com.example.tuwaiqcapstone2.Model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    Recipe findRecipeById(Integer id);

    List<Recipe> findRecipeByName(String name);

    List<Recipe> findRecipeByAllergensEmpty();

    @Query("SELECT r FROM Recipe r WHERE r.id = (SELECT c.recipeId FROM Comment c GROUP BY c.recipeId ORDER BY count(c) desc limit 1)")
    List<Recipe> findRecipeWithMostComments();

    @Query("SELECT r FROM Recipe r WHERE r.id = (SELECT r.recipeId FROM Rating r WHERE r.ratingValue > (SELECT AVG(r2.ratingValue) FROM Rating r2))")
    List<Recipe> findTopRatedRecipes();
}
