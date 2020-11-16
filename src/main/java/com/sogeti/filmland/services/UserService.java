package com.sogeti.filmland.services;

import com.sogeti.filmland.models.UserAccount;
import com.sogeti.filmland.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

public class UserService implements UserDetailsService {
    @Autowired // connect to database
    private UserAccountRepository userAccountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * introduces user to spring security
     * @param userEmail
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
}
