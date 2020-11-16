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

}
