package com.example.tuwaiqcapstone2.Repository;

import com.example.tuwaiqcapstone2.Model.RecipeStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeStepRepository extends JpaRepository<RecipeStep, Integer> {

    RecipeStep findRecipeStepById(Integer id);

    List<RecipeStep> findRecipeStepByRecipeId(Integer recipeId);

    List<RecipeStep> findRecipeStepsByRecipeIdOrderByStepNumberAsc(Integer recipeId);
}
