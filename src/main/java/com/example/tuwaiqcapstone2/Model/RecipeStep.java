package com.example.tuwaiqcapstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
//no duplicate step number for each recipe
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"recipe_id", "step_number"}))
public class RecipeStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Recipe Id must not be null")
    private Integer recipeId;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Step number must not be null")
    @Positive(message = "Step number must be a positive number")
    private Integer stepNumber;

    @Column(columnDefinition = "varchar(500) not null")
    @NotEmpty(message = "Instruction must not be empty")
    @Size(max = 500, message = "Instruction must be at most 500 characters")
    private String instruction;
}
