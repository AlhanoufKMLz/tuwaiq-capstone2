package com.example.tuwaiqcapstone2.Model;

import com.example.tuwaiqcapstone2.Enums.AllergenType;
import com.example.tuwaiqcapstone2.Enums.LanguageCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(20) not null")
    @NotEmpty(message = "Name must not be empty")
    @Pattern(regexp = "^[A-Za-z\\s]{2,50}$", message = "Name must be 2-50 letters")
    private String name;

    @Column(columnDefinition = "varchar(20) not null unique")
    @NotEmpty(message = "Username must not be empty")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]{4,20}$", message = "Username must start with a letter and must be 4-20 alphanumeric characters or underscores")
    private String username;

    @Column(columnDefinition = "varchar(255) not null")
    @NotEmpty(message = "Password must not be empty")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Password must be at least 8 characters, " +
                    "include an uppercase letter, " +
                    "a lowercase letter, " +
                    "a number, " +
                    "and a special character.")
    private String password;

    @Column(columnDefinition = "varchar(50) not null unique")
    @NotEmpty(message = "E-mail must not be empty")
    @Email(message = "Invalid email")
    private String email;

    @Column(columnDefinition = "int check (age > 0)")
    @NotNull(message = "Age must not be null")
    @Positive(message = "Age must be positive number")
    private Integer age;

    @Column(columnDefinition = "varchar(15) not null unique")
    @NotEmpty(message = "Phone number must not be empty")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number")
    private String phoneNumber;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_allergens")
    @Column(name = "allergen")
    private List<AllergenType> allergens;

    @Column(columnDefinition = "boolean default false")
    private Boolean dailyRecipeSubscribed = false;

    @Column(columnDefinition = "time default '08:00:00'")
    private LocalTime dailyRecipeTime;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default 'EN'")
    private LanguageCode dailyRecipeLanguage;

    @Column(columnDefinition = "datetime not null default CURRENT_TIMESTAMP", insertable = false)
    private LocalDateTime createdAt;
}
