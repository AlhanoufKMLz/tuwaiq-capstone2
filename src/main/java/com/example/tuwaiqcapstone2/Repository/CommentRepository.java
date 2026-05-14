package com.example.tuwaiqcapstone2.Repository;

import com.example.tuwaiqcapstone2.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Comment findCommentById(Integer id);

    List<Comment> findCommentByRecipeId(Integer recipeId);

    List<Comment> findCommentByUserId(Integer userId);
}
