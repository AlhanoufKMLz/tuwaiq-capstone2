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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"}))
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Follower Id must not be null")
    private Integer followerId;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Following Id must not be null")
    private Integer followingId;

    @Column(columnDefinition = "datetime not null default CURRENT_TIMESTAMP", insertable = false)
    private LocalDateTime createdAt;
}
