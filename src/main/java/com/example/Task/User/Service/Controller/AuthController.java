package com.example.Task.User.Service.Controller;

import com.example.Task.User.Service.Config.JwtProvider;
import com.example.Task.User.Service.Model.LoginRequest;
import com.example.Task.User.Service.Model.User;
import com.example.Task.User.Service.Repository.UserRepository;
import com.example.Task.User.Service.response.AuthResponse;
import com.example.Task.User.Service.services.CustomServiceUserImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomServiceUserImplementation userImplementation;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> CreateUserHandler(@RequestBody User user ) throws Exception
    {
        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();
        String role = user.getRole();

        User isEmailExist = userRepository.findByEmail(email);

        if(isEmailExist!=null)
        {
            throw new Exception("Email is already used with another account");
        }


        //Otherwise Crate New User
        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setFullName(fullName);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setRole(role);


        User savedUser = userRepository.save(createdUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        String token = JwtProvider.generateToken(authentication);


        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Register Success");
        authResponse.setStatus(true);

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
    }


    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest loginRequest)
    {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();


        System.out.println(username + "------" + password);

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);



        String token = JwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();

        authResponse.setJwt(token);
        authResponse.setMessage("Login Succcessfull");
        authResponse.setStatus(true);

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String username , String password)
    {
        UserDetails userDetails = userImplementation.loadUserByUsername(username);


        System.out.println("Sign in with UserDetails " + userDetails);


        if(userDetails == null)
        {
            System.out.println("Sign in UserDetails " + userDetails);
            throw new BadCredentialsException("Invalid Username or password");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword()))
        {
            System.out.println("Sign in UserDetails : Password not match " + userDetails);
            throw new BadCredentialsException("Invalid Username or Password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null , userDetails.getAuthorities());
    }




}
