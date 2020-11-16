package com.sogeti.filmland.controllers;

import com.sogeti.filmland.dto.LoginRequest;
import com.sogeti.filmland.dto.ResponseMessage;
import com.sogeti.filmland.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("filmland") // end point
public class UserController {
    @Autowired // connect to database
    private UserService userService;

    /**
     * this method let the user login
     * by checking the requested user from the database
     * with encoded passwords
     * @return status and message
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login() {
        ResponseMessage responseMessage = new ResponseMessage();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
            responseMessage.setStatus("Login successful!");
            responseMessage.setMessage("Welcome to Filmland!");
        }else
        {
            responseMessage.setStatus("Login failed!");
            responseMessage.setMessage("Username or password is not correct.");
        }

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
    /**
     * adds a new user to database
     * @param user user
     * @return status and message
     */
    @PostMapping("/add-user")
    public ResponseEntity<String> addUser(@RequestBody LoginRequest user) {
        userService.addUser(user);
        return new ResponseEntity<>("User added", HttpStatus.OK);
    }
}
