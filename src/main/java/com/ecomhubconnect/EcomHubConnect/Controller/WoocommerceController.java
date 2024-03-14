package com.ecomhubconnect.EcomHubConnect.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ecomhubconnect.EcomHubConnect.Config.AppException;
import com.ecomhubconnect.EcomHubConnect.Entity.Stores;
import com.ecomhubconnect.EcomHubConnect.Entity.Users;
import com.ecomhubconnect.EcomHubConnect.Repo.StoreRepository;
import com.ecomhubconnect.EcomHubConnect.Repo.UserRepository;
import com.ecomhubconnect.EcomHubConnect.Service.WoocommerceService;

import jakarta.servlet.http.HttpSession;


@RestController
@CrossOrigin
@RequestMapping("/woocommerce")
public class WoocommerceController {
	
	private WoocommerceService wooCommerceService;
	
	@Autowired
	private StoreRepository storeRepository;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
    public WoocommerceController(WoocommerceService wooCommerceService) {
        this.wooCommerceService = wooCommerceService;
    }
	
	@PostMapping("/addstore")
    public ResponseEntity<String> home(@RequestBody Map<String, Object> connection_credentials, HttpSession session, Model m) {
		        
		wooCommerceService.addstore(connection_credentials.get("consumerKey").toString(), connection_credentials.get("domain").toString(),  connection_credentials.get("consumerSecret").toString());
//		System.out.println(connection_credentials.get("consumerSecret").toString());
//		System.out.println( connection_credentials.get("domain").toString());
//		System.out.println(connection_credentials.get("consumerKey").toString());
		return ResponseEntity.ok("Received Data");
    }
	
	@GetMapping("/store/{storeid}")
	public String getstore(@PathVariable int storeid) {
		return "Store";
		
	}
	
	@GetMapping("/stores")
    public ResponseEntity<?> getUserString() {
		Users user;
		System.out.println("Came Into User Details");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();       
		System.out.println(authentication.getName());
		
		
        if (authentication != null && authentication.isAuthenticated()) {            
            user = userRepo.findByEmail(authentication.getName()).orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));;
            if (user != null) {
            	
            	List<Stores> userStores = storeRepository.findByUser(user);

                return ResponseEntity.ok(userStores);
            	// Assuming you have implemented the toString method in Stores entity to print necessary store details
//                return ResponseEntity.ok(user.getStores());
            } else {
                return ResponseEntity.notFound().build();
            }
            
        }
        Map<String, String> response = new HashMap<>();
        response.put("Message", "User not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        
    }
	
	@GetMapping("/syncorders/{storeid}")
	public String syncorders(@PathVariable int storeid) {
		
		wooCommerceService.fetchOrders(storeid);
		return "Succesful";
	}
}
