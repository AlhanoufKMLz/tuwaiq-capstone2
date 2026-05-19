package com.example.tuwaiqcapstone2.Controller;

import com.example.tuwaiqcapstone2.Api.ApiResponse;
import com.example.tuwaiqcapstone2.Model.Comment;
import com.example.tuwaiqcapstone2.Service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    //BASIC CRUD ENDPOINTS
    @GetMapping("/get")
    public ResponseEntity<?> getAllComments(){
        return ResponseEntity.status(200).body(commentService.getAllComments());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addComment(@RequestBody @Valid Comment comment){
        commentService.addComment(comment);
        return ResponseEntity.status(200).body(new ApiResponse("Comment added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Integer id, @RequestBody @Valid Comment comment){
        commentService.updateComment(id, comment);
        return ResponseEntity.status(200).body(new ApiResponse("Comment updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer id){
        commentService.deleteComment(id);
        return ResponseEntity.status(200).body(new ApiResponse("Comment deleted successfully"));
    }


    //EXTRA ENDPOINTS
    @GetMapping("/get-recipe/{recipeId}")
    public ResponseEntity<?> findCommentByRecipeId(@PathVariable Integer recipeId){
        return ResponseEntity.status(200).body(commentService.findCommentByRecipeId(recipeId));
    }

    @GetMapping("/get-user/{userId}")
    public ResponseEntity<?> findCommentByUserId(@PathVariable Integer userId){
        return ResponseEntity.status(200).body(commentService.findCommentByUserId(userId));
    }
}
