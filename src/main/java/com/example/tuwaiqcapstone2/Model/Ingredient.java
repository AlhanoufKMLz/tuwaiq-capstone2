package com.example.tuwaiqcapstone2.Model;

import com.example.tuwaiqcapstone2.Enums.UnitType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Recipe Id must not be null")
    private Integer recipeId;

    @Column(columnDefinition = "varchar(20) not null")
    @NotEmpty(message = "Name must not be empty")
    @Pattern(regexp =  "^[a-zA-Z0-9 ]{3,50}$", message = "Name must be 3-50 alphanumeric characters")
    private String name;

    @Column(columnDefinition = "int not null")
    @Positive(message = "Amount must be positive number")
    private Double amount;

    @Column(columnDefinition = "varchar(20) not null")
    @NotNull(message = "Unit must not be null")
    @Enumerated(EnumType.STRING)
    private UnitType unit;
}
