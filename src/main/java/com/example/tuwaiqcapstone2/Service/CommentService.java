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
        checkUser(comment.getUserId());
        checkRecipe(comment.getRecipeId());

        commentRepository.save(comment);
    }

    public void updateComment(Integer id, Comment comment){
        Comment oldComment = checkComment(id);
        checkUser(comment.getUserId());
        checkRecipe(comment.getRecipeId());

        oldComment.setContent(comment.getContent());
        commentRepository.save(oldComment);
    }

    public void deleteComment(Integer id){
        Comment comment = checkComment(id);
        commentRepository.delete(comment);
    }


    //EXTRA ENDPOINTS
    public List<Comment> findCommentByRecipeId(Integer recipeId){
        checkRecipe(recipeId);
        List<Comment> comments = commentRepository.findCommentByRecipeId(recipeId);

        if(comments.isEmpty()) throw new ApiException("No comments found");

        return comments;
    }

    public List<Comment> findCommentByUserId(Integer userId){
        checkUser(userId);
        List<Comment> comments = commentRepository.findCommentByUserId(userId);

        if(comments.isEmpty()) throw new ApiException("No comments found");

        return comments;
    }


    //HELPER METHODS
    public Comment checkComment(Integer id){
        Comment comment = commentRepository.findCommentById(id);
        if(comment == null) throw new ApiException("Comment not found"); //check comment

        return comment;
    }

    public void checkUser(Integer id){
        User user = userRepository.findUserById(id);
        if(user == null) throw new ApiException("User not found"); //check user
    }

    public void checkRecipe(Integer id){
        Recipe recipe = recipeRepository.findRecipeById(id);
        if(recipe == null) throw new ApiException("Recipe not found"); //check recipe
    }
}
