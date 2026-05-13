package com.example.tuwaiqcapstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "recipe_id"}))
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "User Id must not be null")
    private Integer userId;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Recipe Id must not be null")
    private Integer recipeId;

    @Column(columnDefinition = "double not null")
    @NotNull(message = "Rating value must not be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer ratingValue;

    @Column(columnDefinition = "datetime not null default CURRENT_TIMESTAMP", insertable = false)
    private LocalDateTime createdAt;
}
