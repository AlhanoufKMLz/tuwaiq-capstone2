package com.example.tuwaiqcapstone2.Model;

import jakarta.persistence.*;
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
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "User Id must not be null")
    private Integer userId;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Recipe Id must not be null")
    private Integer recipeId;

    @Column(columnDefinition = "datetime not null default CURRENT_TIMESTAMP", insertable = false)
    private LocalDateTime createdAt;
}
