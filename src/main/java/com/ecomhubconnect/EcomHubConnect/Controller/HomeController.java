package com.ecomhubconnect.EcomHubConnect.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.ecomhubconnect.EcomHubConnect.Dto.UserDTO;
//import com.ecomhubconnect.EcomHubConnect.Entity.User;
import com.ecomhubconnect.EcomHubConnect.Entity.Users;
import com.ecomhubconnect.EcomHubConnect.Repo.UserRepository;
import com.ecomhubconnect.EcomHubConnect.Service.UserService;
import com.ecomhubconnect.EcomHubConnect.Service.UserServiceImpl;
import com.ecomhubconnect.EcomHubConnect.Service.WoocommerceService;

import java.security.Principal;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import jakarta.servlet.http.HttpSession;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin
@RequestMapping("")
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepo;

//	@ModelAttribute
//	public void commonUser(Principal p, Model m) {
//		if (p != null) {
//			String email = p.getName();
//			User user = userRepo.findByEmail(email);
//			m.addAttribute("user", user);
//		}
//
//	}
	
	

	public String fetchCSRFTokenFromServer(String csrfTokenEndpoint) {
        // Create a WebClient instance
		WebClient webClient = WebClient.builder().exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)).build()).build();

        // Send a GET request to fetch the CSRF token from the server
        String csrfToken = webClient.get()
                .uri(csrfTokenEndpoint)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    return response.contains("csrfToken") ? response.substring(response.indexOf(":") + 3, response.length() - 2) : "";
                })
                .block();
        System.out.println(csrfToken);
        return csrfToken;
    }
	
	
	
	
	public void sendDataToEndpoint(String consumerKey, String domain, String secretConsumerKey, String version) {
		String url = "http://127.0.0.1:8000/hello";

        // Create a WebClient instance
        WebClient webClient = WebClient.builder().baseUrl(url).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

        // Fetch CSRF token
//        String csrfToken = fetchCSRFTokenFromServer();
        String csrfTokenEndpoint = "http://127.0.0.1:8000/get-csrf-token/";
    	String csrfToken = fetchCSRFTokenFromServer(csrfTokenEndpoint);
    	
        // Send a POST request with the POST data and retrieve the JSON response
        String jsonResponse = webClient.post()
                .uri(url)
                .header("X-CSRFToken", csrfToken)
                .cookie("X-CSRFToken", csrfToken)
                .body(BodyInserters.fromValue("{\"consumerKey\":\"" + consumerKey + "\",\"domain\":\"" + domain + "\",\"secretConsumerKey\":\"" + secretConsumerKey + "\",\"version\":\"" + version + "\",\"X-CSRFToken\":\"" + csrfToken + "\"}"))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Print the JSON response
        System.out.println("JSON response: " + jsonResponse);
}
	

	@GetMapping("/")
	public String index() {
		sendDataToEndpoint("ck_a60d52112817d38e0cba45c677e819659e3ff602", "http://localhost/wordpress",  "cs_04c9ebcb880f15a42bfc2bf2939d97cda895f4ca", "4");
		return "index";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@GetMapping("/signin")
	public String login() {
		return "login";
	}

	@GetMapping("/user/profile")
	public String profile(Principal p, Model m) {
//		String email = p.getName();
//		User user = userRepo.findByEmail(email);
//		m.addAttribute("user", user);
		return "profile";
	}

	@GetMapping("/user/home")
	public String home() {
		return "home";
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