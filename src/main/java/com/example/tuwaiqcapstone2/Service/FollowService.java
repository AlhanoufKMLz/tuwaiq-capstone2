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
    private final EmailSenderService emailSenderService;


    //BASIC CRUD
    public List<Follow> getAllFollows(){
        return followRepository.findAll();
    }

    public void addFollow(Follow follow){
        //get following and follower data
        User following = checkUser(follow.getFollowerId());
        User follower = checkUser(follow.getFollowingId());

        //send email
        emailSenderService.sendEmail(following.getEmail(), "You have a new follower on RecipeHub!",
                "Hi " + following.getName() + ",\n" +
                        "\n" +
                        follower.getName() + " started following you on RecipeHub!\n" +
                        "\n" +
                        "Check out their profile and recipes.\n" +
                        "\n" +
                        "Happy Cooking!\n" +
                        "RecipeHub Team");

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

    private User checkUser(Integer id){
        User user = userRepository.findUserById(id);
        if(user == null) throw new ApiException("User not found"); //check user

        return user;
    }
}
