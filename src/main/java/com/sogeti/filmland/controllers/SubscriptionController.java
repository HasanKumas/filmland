package com.sogeti.filmland.controllers;

import com.sogeti.filmland.models.Category;
import com.sogeti.filmland.models.Subscription;
import com.sogeti.filmland.models.UserAccount;
import com.sogeti.filmland.repositories.CategoryRepository;
import com.sogeti.filmland.repositories.SubscriptionRepository;
import com.sogeti.filmland.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("filmland") // end point
public class SubscriptionController {

}
