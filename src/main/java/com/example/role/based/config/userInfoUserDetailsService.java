package com.example.role.based.config;


import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.role.based.entity.User;
import com.example.role.based.repository.UserRepository;

public class userInfoUserDetailsService implements UserDetailsService {

	
	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		Optional<User> usrName=userRepository.findByName(username);
		
	    
		return usrName.map(UserInfoUserDetails::new).orElseThrow(()->new UsernameNotFoundException("user not found"));
		
	}

}
