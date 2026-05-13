package com.example.tuwaiqcapstone2.Model;

import com.example.tuwaiqcapstone2.Enums.AllergenType;
import com.example.tuwaiqcapstone2.Enums.DifficultyLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "User Id must not be null")
    private Integer userId;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Category Id must not be null")
    private Integer categoryId;

    @Column(columnDefinition = "varchar(20) not null")
    @NotEmpty(message = "Name must not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9 ]{3,100}$", message = "Name must be 3-100 alphanumeric characters")
    private String name;

    @Column(columnDefinition = "varchar(500) not null")
    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @Column(columnDefinition = "int check (cook_time > 0)")
    @Positive(message = "Cook time must be a positive number")
    private Integer cookTime;

    @Column(columnDefinition = "varchar(10) not null")
    @NotNull(message = "Difficulty must not be null")
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficulty;

    @Column(columnDefinition = "int check (servings > 0)")
    @Positive(message = "Servings must be a positive number")
    private Integer servings;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "recipe_allergens")
    @Column(name = "allergen")
    private List<AllergenType> allergens;

    @Column(columnDefinition = "datetime not null default CURRENT_TIMESTAMP", insertable = false)
    private LocalDateTime createdAt;
}
