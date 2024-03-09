package com.ecomhubconnect.EcomHubConnect.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ecomhubconnect.EcomHubConnect.Entity.Users;
import com.ecomhubconnect.EcomHubConnect.Service.WoocommerceService;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
@RequestMapping("/woocommerce")
public class WoocommerceController {
	
	private WoocommerceService wooCommerceService;
	
	@Autowired
    public WoocommerceController(WoocommerceService wooCommerceService) {
        this.wooCommerceService = wooCommerceService;
    }
	
	@PostMapping("/addstore")
    public String home(@RequestBody Map<String, Object> connection_credentials, HttpSession session, Model m) {
		        
		wooCommerceService.addstore(connection_credentials.get("consumerKey").toString(), connection_credentials.get("domain").toString(),  connection_credentials.get("consumerSecret").toString());
//		System.out.println(connection_credentials.get("consumerSecret").toString());
//		System.out.println( connection_credentials.get("domain").toString());
//		System.out.println(connection_credentials.get("consumerKey").toString());
        return "Received Data";
    }
	
	@GetMapping("/store/{storeid}")
	public String getstore(@PathVariable int storeid) {
		return "Store";
		
	}
	
	@GetMapping("/syncorders/{storeid}")
	public String syncorders(@PathVariable int storeid) {
		
		wooCommerceService.fetchorders(storeid);
		return "Succesful";
	}
}
