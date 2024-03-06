package com.ecomhubconnect.EcomHubConnect.Service;

import java.nio.CharBuffer;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ecomhubconnect.EcomHubConnect.Entity.Users;
import com.ecomhubconnect.EcomHubConnect.Repo.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService{

	@Autowired
	private UserRepository userRepo;
	

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public Users saveUser(Users user) {

		String password=passwordEncoder.encode(user.getPassword());
		user.setPassword(password);
		user.setRole("ROLE_USER");
		Users newuser = userRepo.save(user);

		return newuser;
	}

	
	public void removeSessionMessage() {

		HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
				.getSession();

		session.removeAttribute("msg");
	}
	   
}