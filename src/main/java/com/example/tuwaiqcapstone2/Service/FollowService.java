package com.example.tuwaiqcapstone2.Service;

import com.example.tuwaiqcapstone2.Api.ApiException;
import com.example.tuwaiqcapstone2.Model.Follow;
import com.example.tuwaiqcapstone2.Model.User;
import com.example.tuwaiqcapstone2.Repository.FollowRepository;
import com.example.tuwaiqcapstone2.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;


    //BASIC CRUD
    public List<Follow> getAllFollows(){
        return followRepository.findAll();
    }

    public void addFollow(Follow follow){
        checkUser(follow.getFollowerId());
        checkUser(follow.getFollowingId());

        followRepository.save(follow);
    }

    public void deleteFollow(Integer id){
        Follow follow = checkFollow(id);

        followRepository.delete(follow);
    }


    //HELPER METHODS
    private Follow checkFollow(Integer id){
        Follow follow = followRepository.findFollowById(id);
        if(follow == null) throw new ApiException("Follow not found"); //check favorite
        return follow;
    }

    private void checkUser(Integer id){
        User user = userRepository.findUserById(id);
        if(user == null) throw new ApiException("User not found"); //user recipe
    }
}
