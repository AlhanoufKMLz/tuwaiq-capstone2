package com.example.tuwaiqcapstone2.Repository;

import com.example.tuwaiqcapstone2.Model.RecipeStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeStepRepository extends JpaRepository<RecipeStep, Integer> {

    RecipeStep findRecipeStepById(Integer id);
}
