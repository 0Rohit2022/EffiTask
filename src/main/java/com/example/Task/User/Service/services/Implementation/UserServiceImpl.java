package com.example.Task.User.Service.services.Implementation;

import com.example.Task.User.Service.Config.JwtProvider;
import com.example.Task.User.Service.Model.User;
import com.example.Task.User.Service.Repository.UserRepository;
import com.example.Task.User.Service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserProfile(String jwt) {
        String email = JwtProvider.getEmailFromJwtToken(jwt);
        return userRepository.findByEmail(email);

    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


}
