package com.buda.api.login.social.facebook;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import com.buda.entities.User;
import com.buda.repository.UserRepository;

@Service

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) 
			throws UsernameNotFoundException {
		Optional<User> userGet = userRepository.findUserByUserName(username);
		User user=userGet.get();
		if (user == null) {
			throw new UsernameNotFoundException("Could not find user");
		}
		
		return new MyUserDetails(user);
	}
}
