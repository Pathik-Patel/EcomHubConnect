package com.ecomhubconnect.EcomHubConnect.Config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

//import com.ecomhubconnect.EcomHubConnect.Entity.User;
import com.ecomhubconnect.EcomHubConnect.Entity.Users;
import com.ecomhubconnect.EcomHubConnect.Repo.UserRepository;


@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		System.out.println(username);
		Users user = userRepo.findByEmail(username).orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));;
		UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles("USER")
                        .build();
		
		
		return userDetails;
		

	}

}