package com.example.tuwaiqcapstone2.Service;

import com.example.tuwaiqcapstone2.Api.ApiException;
import com.example.tuwaiqcapstone2.Model.User;
import com.example.tuwaiqcapstone2.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    //BASIC CRUD
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void addUser(User user){
        userRepository.save(user);
    }

    public void updateUser(Integer id, User user){
        User oldUser = userRepository.findUserById(id);
        if(oldUser == null)
            throw new ApiException("User not found");

        oldUser.setName(user.getName());
        oldUser.setUsername(user.getUsername());
        oldUser.setPassword(user.getPassword());
        oldUser.setEmail(user.getEmail());
        oldUser.setAge(user.getAge());
        oldUser.setPhoneNumber(user.getPhoneNumber());
        oldUser.setAllergens(user.getAllergens());
        userRepository.save(oldUser);
    }

    public void deleteUser(Integer id){
        User user = userRepository.findUserById(id);
        if(user == null) throw new ApiException("User not found");

        userRepository.delete(user);
    }


    //EXTRA ENDPOINTS
    public User findUserWithMostRecipes(){
        User user = userRepository.findUserWithMostRecipes();

        if(user == null) throw new ApiException("User not found");

        return user;
    }
}
