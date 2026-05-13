package com.example.tuwaiqcapstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "User Id must not be null")
    private Integer userId;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Recipe Id must not be null")
    private Integer recipeId;

    @Column(columnDefinition = "varchar(200) not null")
    @NotEmpty(message = "Content must not be empty")
    @Size(max = 200, message = "Content must be at most 200 characters")
    private String content;

    @Column(columnDefinition = "datetime not null default CURRENT_TIMESTAMP", insertable = false)
    private LocalDateTime createdAt;
}
