package com.ecomhubconnect.EcomHubConnect.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ecomhubconnect.EcomHubConnect.Config.AppException;
import com.ecomhubconnect.EcomHubConnect.Dto.CredentialsDTO;
import com.ecomhubconnect.EcomHubConnect.Dto.ResponseDTO;
//import com.ecomhubconnect.EcomHubConnect.Entity.User;
import com.ecomhubconnect.EcomHubConnect.Entity.Users;
import com.ecomhubconnect.EcomHubConnect.Repo.UserRepository;
import com.ecomhubconnect.EcomHubConnect.Service.UserService;
import com.ecomhubconnect.EcomHubConnect.Session.InMemorySessionRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("")
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);


	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
    public InMemorySessionRegistry sessionRegistry;
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
	
	@GetMapping("/logout")
    public ResponseEntity<?> logoutuser() {
		
        Map<String, String> response = new HashMap<>();
        response.put("Message", "Logged Out Successfully!");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        
    }

	@GetMapping("/userdetails")
    public ResponseEntity<?> getUserString() {
		Users user;
		System.out.println("Came Into User Details");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();        
        if (authentication != null && authentication.isAuthenticated()) {            
            user = userRepo.findByEmail(authentication.getName()).orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));;
            return ResponseEntity.ok(user);
        }
        Map<String, String> response = new HashMap<>();
        response.put("Message", "User not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        
    }

	
	@PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response, @RequestBody CredentialsDTO loginRequest) {
		
        	
		try {
			
        	UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        	Optional<Users> userOptional = userRepo.findByEmail(loginRequest.getUsername());
        	if (!userOptional.isPresent()) {

        		Map<String, String> responsed = new HashMap<>();
                responsed.put("Message", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        	}
        	Authentication authentication = authenticationManager.authenticate(authRequest);
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
            securityContextRepository.saveContext(securityContext, request, response);
            final String sessionId = sessionRegistry.registerSession(loginRequest.getUsername());
            
            ResponseDTO responsed = new ResponseDTO();
            responsed.setSessionid(sessionId);
            Users user = userOptional.get();
            responsed.setUsername(user.getEmail());
            responsed.setFirstname(user.getFirstname());
            System.out.println(userOptional);
            return ResponseEntity.ok(responsed);

		}
		catch (Exception e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
    }

	@PostMapping("/saveUser")
	public ResponseEntity<String> saveUser(@RequestBody Users user, HttpSession session, Model m) {

		user.setActive("true");
		Users u = userService.saveUser(user);
		
		 
		

		if (u != null) {
			String message = "Register successfully";
			logger.info("Register successfully");
		     logger.debug("Register successfully");
			return ResponseEntity.ok(message);

		} else {
			String message = "Failed";
			return ResponseEntity.ok(message);
		}
	   
	}

}