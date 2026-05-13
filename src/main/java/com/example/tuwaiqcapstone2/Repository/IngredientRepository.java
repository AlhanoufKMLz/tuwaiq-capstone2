package com.example.tuwaiqcapstone2.Repository;

import com.example.tuwaiqcapstone2.Model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

    Ingredient findIngredientById(Integer id);
}
