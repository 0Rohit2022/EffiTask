package com.example.Task.User.Service.services;

import com.example.Task.User.Service.Model.User;

import java.util.List;

public interface UserService {
    public User getUserProfile(String jwt);

    public List<User> getAllUsers();
}
