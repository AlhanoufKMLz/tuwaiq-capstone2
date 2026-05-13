package com.example.tuwaiqcapstone2.Service;

import com.example.tuwaiqcapstone2.Api.ApiException;
import com.example.tuwaiqcapstone2.Model.Comment;
import com.example.tuwaiqcapstone2.Model.Recipe;
import com.example.tuwaiqcapstone2.Model.User;
import com.example.tuwaiqcapstone2.Repository.CommentRepository;
import com.example.tuwaiqcapstone2.Repository.RecipeRepository;
import com.example.tuwaiqcapstone2.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    //BASIC CRUD
    public List<Comment> getAllComments(){
        return commentRepository.findAll();
    }

    public void addComment(Comment comment){
        User user = userRepository.findUserById(comment.getUserId());
        if(user == null) throw new ApiException("User not found"); //check user
        Recipe recipe = recipeRepository.findRecipeById(comment.getRecipeId());
        if(recipe == null) throw new ApiException("Recipe not found"); //check recipe

        commentRepository.save(comment);
    }

    public void updateComment(Integer id, Comment comment){
        Comment oldComment = commentRepository.findCommentById(id);
        if(oldComment == null) throw new ApiException("Comment not found"); //check comment

        User user = userRepository.findUserById(comment.getUserId());
        if(user == null) throw new ApiException("User not found"); //check user
        Recipe recipe = recipeRepository.findRecipeById(comment.getRecipeId());
        if(recipe == null) throw new ApiException("Recipe not found"); //check recipe

        oldComment.setUserId(comment.getUserId());
        oldComment.setUserId(comment.getUserId());
        oldComment.setRecipeId(comment.getRecipeId());
        commentRepository.save(oldComment);
    }

    public void deleteComment(Integer id){
        Comment comment = commentRepository.findCommentById(id);
        if(comment == null) throw new ApiException("Comment not found"); //check comment

        commentRepository.delete(comment);
    }
}
