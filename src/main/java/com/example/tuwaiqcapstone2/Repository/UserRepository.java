package com.example.tuwaiqcapstone2.Repository;

import com.example.tuwaiqcapstone2.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserById(Integer id);

    @Query(value = "SELECT * FROM user u WHERE u.id = (SELECT r.user_id FROM recipe r GROUP BY r.user_id ORDER BY count(r) DESC LIMIT 1)", nativeQuery = true)
    User findUserWithMostRecipes();
}
