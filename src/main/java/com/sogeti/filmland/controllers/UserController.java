package com.sogeti.filmland.controllers;

import com.sogeti.filmland.dto.LoginRequest;
import com.sogeti.filmland.dto.ResponseMessage;
import com.sogeti.filmland.repositories.UserAccountRepository;
import com.sogeti.filmland.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("filmland") // end point
public class UserController {
    @Autowired // connect to database
    private UserAccountRepository userAccountRepository;
    @Autowired
    private UserService userService;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    /**
     * this method let the user login
     * by checking the requested user from the database
     * with encoded passwords
     * @param user
     * @return status and message
     */
    @PostMapping("/login")
    public ResponseMessage login(@RequestBody LoginRequest user) {
        UserDetails existingUser = userService.loadUserByUsername(user.getEmail());

        return validateUser(user, existingUser);
    }
    /**
     * adds a new user to database
     * @param user
     * @return status and message
     */
    @PostMapping("/add-user")
    public ResponseEntity<String> addUser(@RequestBody LoginRequest user) {
        userService.addUser(user);
        return new ResponseEntity<>("User added", HttpStatus.OK);
    }
    /**
     * checks if login credentials match
     * @param user
     * @param existingUser
     * @return status and message
     */
    private ResponseMessage validateUser(LoginRequest user, UserDetails existingUser) {
        ResponseMessage responseMessage = new ResponseMessage();

        if (existingUser != null && (passwordEncoder.matches(user.getPassword(), existingUser.getPassword()))) {
            responseMessage.setStatus("Login successful!");
            responseMessage.setMessage("Welcome to Filmland!");
        }else
        {
            responseMessage.setStatus("Login failed!");
            responseMessage.setMessage("Username or password is not correct.");
        }
        return responseMessage;
    }
}
