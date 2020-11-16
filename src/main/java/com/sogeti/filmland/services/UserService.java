package com.sogeti.filmland.services;

import com.sogeti.filmland.dto.LoginRequest;
import com.sogeti.filmland.exceptions.BadRequestException;
import com.sogeti.filmland.models.Subscription;
import com.sogeti.filmland.models.UserAccount;
import com.sogeti.filmland.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Component
public class UserService implements UserDetailsService {
    @Autowired // connect to database
    private UserAccountRepository userAccountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * introduces user to spring security
     * @param userEmail email
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        UserAccount user = userAccountRepository.findOneByUserNameIgnoreCase(userEmail);

        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("user"));

        return new User(user.getUserName(), user.getPassword(), authorities);

    }
    /**
     * check database and get the user with given userEmail
     * @param userEmail user email
     * @return user
     */
    public UserAccount getExistingUser(String userEmail){
        return userAccountRepository.findOneByUserNameIgnoreCase(userEmail);
    }
    /**
     * adds a new user to database
     * @param user
     * @return true or false
     */
    public Boolean addUser(LoginRequest user) {
        UserAccount existingUser = userAccountRepository.findOneByUserNameIgnoreCase(user.getEmail());
        if(existingUser != null) {
            throw new BadRequestException("This user email is already registered!");
        }
        UserAccount newUser = new UserAccount();
        newUser.setUserName(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userAccountRepository.save(newUser);
        return true;
    }
    /**
     * checks authenticated user matches with requested user
     * @param user user
     * @return true or false
     */
    public Boolean isAuthenticated(String user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String userName = authentication.getName();
            return userName.equals(user);
        }
        return false;
    }
    /**
     * gets all subscriptions belong to user
     * @param user user
     * @return list of subscriptions
     */
    public List<Subscription> getAllUserSubscriptions(UserAccount user){
        return user.getSubscriptions();
    }

}
