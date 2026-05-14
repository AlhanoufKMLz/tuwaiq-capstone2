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
    List<Recipe> findRecipesWithCookTimeLessThan(Integer minutes);

    @Query(value = "SELECT * FROM recipe r WHERE r.id = (SELECT c.recipe_id FROM comment c GROUP BY c.recipe_id ORDER BY count(c) DESC LIMIT 1)", nativeQuery = true)
    List<Recipe> findRecipeWithMostComments();

    @Query("SELECT r FROM Recipe r WHERE r.id IN (SELECT rt.recipeId FROM Rating rt WHERE rt.ratingValue > (SELECT AVG(rt2.ratingValue) FROM Rating rt2))")
    List<Recipe> findTopRatedRecipes();

    @Query(value = "SELECT * FROM recipe r WHERE r.id IN (SELECT rt.recipe_id FROM rating rt WHERE rt.created_at BETWEEN DATE_SUB(NOW(), INTERVAL 7 DAY) AND NOW() AND rt.rating_value > (SELECT AVG(rt2.rating_value) FROM rating rt2))", nativeQuery = true)
    List<Recipe> findTopRatedRecipesThisWeek();

    @Query("SELECT r FROM Recipe r WHERE r.id IN (SELECT f.recipeId FROM Favorite f WHERE f.userId = ?1)")
    List<Recipe> findUserFavoritesRecipes(Integer userId);

    @Query("SELECT r FROM Recipe r WHERE r.difficulty = (SELECT r1.difficulty FROM  Recipe r1 WHERE r1.id = ?1) AND r.categoryId = (SELECT r2.categoryId FROM  Recipe r2 WHERE r2.id = ?1)")
    List<Recipe> findSimilarRecipes(Integer recipeId); //based on difficulty and category
}
