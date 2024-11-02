package com.example.Task.User.Service.Controller;

import com.example.Task.User.Service.Model.User;
import com.example.Task.User.Service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUserProfileByEmail")
    public ResponseEntity<User> getUserProfileByEmail(@RequestHeader("Authorization") String jwt)
    {
        User user = userService.getUserProfile(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }



    @GetMapping()
    public ResponseEntity<List<User>> GetAllUsers(@RequestHeader("Authorization") String jwt)
    {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users,HttpStatus.OK);
    }
}
