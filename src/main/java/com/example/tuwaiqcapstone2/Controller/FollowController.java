package com.example.tuwaiqcapstone2.Controller;

import com.example.tuwaiqcapstone2.Api.ApiResponse;
import com.example.tuwaiqcapstone2.Model.Follow;
import com.example.tuwaiqcapstone2.Service.FollowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;


    //BASIC CRUD ENDPOINTS
    @GetMapping("/get")
    public ResponseEntity<?> getAllFollows(){
        return ResponseEntity.status(200).body(followService.getAllFollows());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addFollow(@RequestBody @Valid Follow follow){
        followService.addFollow(follow);
        return ResponseEntity.status(200).body(new ApiResponse("Follow added successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFollow(@PathVariable Integer id){
        followService.deleteFollow(id);
        return ResponseEntity.status(200).body(new ApiResponse("Follow deleted successfully"));
    }
}
