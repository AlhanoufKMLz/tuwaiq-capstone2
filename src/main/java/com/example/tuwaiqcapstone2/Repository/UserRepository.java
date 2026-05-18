package com.example.tuwaiqcapstone2.Repository;

import com.example.tuwaiqcapstone2.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserById(Integer id);

    @Query("SELECT u FROM User u WHERE u.id IN (SELECT r.userId FROM Recipe r GROUP BY r.userId) ORDER BY (SELECT COUNT(r2) FROM Recipe r2 WHERE r2.userId = u.id) DESC")
    List<User> findUsersSortedByMostRecipes();

    @Query(value = "SELECT u.* FROM user u JOIN recipe r ON u.id = r.user_id JOIN rating rt ON r.id = rt.recipe_id GROUP BY u.id ORDER BY AVG(rt.rating_value) DESC", nativeQuery = true)
    List<User> getUsersSortedByRecipeRating();

    @Query("SELECT u FROM User u WHERE u.id IN (SELECT f.followingId FROM Follow f WHERE f.followerId = ?1) AND u.id IN (SELECT f2.followerId FROM Follow f2 WHERE f2.followingId = ?1)")
    List<User> findMutualFollows(Integer userId); //who the user's follow, and they follow the user back
}
