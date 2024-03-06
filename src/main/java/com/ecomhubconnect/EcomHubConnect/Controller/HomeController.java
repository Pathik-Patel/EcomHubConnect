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
import com.ecomhubconnect.EcomHubConnect.Dto.UserDTO;
import com.ecomhubconnect.EcomHubConnect.Entity.Stores;
//import com.ecomhubconnect.EcomHubConnect.Entity.User;
import com.ecomhubconnect.EcomHubConnect.Entity.Users;
import com.ecomhubconnect.EcomHubConnect.Repo.UserRepository;
import com.ecomhubconnect.EcomHubConnect.Service.UserService;
import com.ecomhubconnect.EcomHubConnect.Service.UserService;
import com.ecomhubconnect.EcomHubConnect.Service.WoocommerceService;

import java.security.Principal;
import java.util.Map;

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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
@CrossOrigin
@RequestMapping("")
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	private WoocommerceService wooCommerceService;
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	
	
	@Autowired
    public  HomeController(WoocommerceService woocommerceService) {
	    this.wooCommerceService = wooCommerceService;
    }

	@GetMapping("/")
    public String getUserString() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {            
            
            Users user = userRepo.findByEmail(authentication.getName()).orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));;
            System.out.println(user);
        }
        return "xyz";
    }
	 @Autowired HttpSession session;
	
	@PostMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, @RequestBody CredentialsDTO loginRequest) {
		System.out.println("Beofre");
        Authentication authentication = authenticationManager.authenticate(
        		
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        System.out.println("After");
        try {
        	SecurityContext securityContext = SecurityContextHolder.getContext();
        	securityContext.setAuthentication(authentication);
        	HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,securityContext);
//        	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        	
//            UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getUsername());
//            session.setAttribute("userDetails", userDetails);
            return "Login Successful";
        }
        catch(Exception e){
        	return "Login Failed";
        }
        
    }

	@PostMapping("/saveUser")
	public String saveUser(@RequestBody Users user, HttpSession session, Model m) {

		 System.out.println(user);

		Users u = userService.saveUser(user);

		if (u != null) {
			// System.out.println("save sucess");
			session.setAttribute("msg", "Register successfully");

		} else {
			// System.out.println("error in server");
			session.setAttribute("msg", "Something wrong server");
		}
		return "redirect:/register";
		 
//		 return "test";
	}

}