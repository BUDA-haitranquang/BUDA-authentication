package com.buda.api.login.social.facebook;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buda.entities.User;
import com.buda.entities.enumeration.Provider;
import com.buda.repository.UserRepository;

@Service

public class UserService {
    @Autowired
    private UserRepository repo;
     
    public void processOAuthPostLogin(String username) {
    	System.out.println("username: " + username);
        Optional<User> existUser = repo.findUserByUserName(username);
         
        if (existUser == null) {
            User newUser = new User();
            newUser.setUserName(username);
            newUser.setProvider(Provider.FACEBOOK);
            newUser.setEnabled(true);          
             
            repo.save(newUser);        
        }
         
    }
}
