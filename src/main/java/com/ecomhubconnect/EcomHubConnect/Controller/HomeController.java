package com.ecomhubconnect.EcomHubConnect.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.ecomhubconnect.EcomHubConnect.Config.AppException;
import com.ecomhubconnect.EcomHubConnect.Config.CustomUserDetailsService;
import com.ecomhubconnect.EcomHubConnect.Dto.CredentialsDTO;
import com.ecomhubconnect.EcomHubConnect.Dto.ResponseDTO;
import com.ecomhubconnect.EcomHubConnect.Dto.UserDTO;
import com.ecomhubconnect.EcomHubConnect.Entity.Stores;
//import com.ecomhubconnect.EcomHubConnect.Entity.User;
import com.ecomhubconnect.EcomHubConnect.Entity.Users;
import com.ecomhubconnect.EcomHubConnect.Repo.UserRepository;
import com.ecomhubconnect.EcomHubConnect.Service.UserService;
import com.ecomhubconnect.EcomHubConnect.Service.UserService;
import com.ecomhubconnect.EcomHubConnect.Service.WoocommerceService;
import com.ecomhubconnect.EcomHubConnect.Session.InMemorySessionRegistry;

import java.io.Console;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("")
public class HomeController {

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
			return ResponseEntity.ok(message);

		} else {
			String message = "Failed";
			return ResponseEntity.ok(message);
		}
	   
	}

}