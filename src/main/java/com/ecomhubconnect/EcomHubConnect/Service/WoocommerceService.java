package com.ecomhubconnect.EcomHubConnect.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ecomhubconnect.EcomHubConnect.Config.AppException;
import com.ecomhubconnect.EcomHubConnect.Entity.Orders;
import com.ecomhubconnect.EcomHubConnect.Entity.Stores;
import com.ecomhubconnect.EcomHubConnect.Entity.Users;
import com.ecomhubconnect.EcomHubConnect.Repo.OrderRepository;
import com.ecomhubconnect.EcomHubConnect.Repo.StoreRepository;
import com.ecomhubconnect.EcomHubConnect.Repo.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WoocommerceService {
	
	@Autowired
    private StoreRepository storesRepository;
	
	@Autowired
    private UserRepository usersRepository;
	
	@Autowired
    private OrderRepository orderRepository;
	
	
	public String addstore(String consumerKey, String domain, String secretConsumerKey) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {            
            
            Users user = usersRepository.findByEmail(authentication.getName()).orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));;
            if (user != null) {
            	 Stores store = new Stores();
                 store.setDomain(domain);
                 store.setConsumerKey(consumerKey);
                 store.setConsumerSecretKey(secretConsumerKey);
                 store.setUser(user);

                 storesRepository.save(store);
                 
                 return "Success";
            } else {
                
                return ""; 
            }
            
           
            
        }
        

        return null;
	}
	
	public void fetchOrders(int storeId) {

	    // Check if the store exists in the database
	    Optional<Stores> storeOptional = storesRepository.findById(storeId);
	    
	    System.out.println(storeOptional);
	    Stores store = storeOptional.orElse(null);

	    if (store == null) {
	        // Handle the case where the store doesn't exist
	        return;
	    }

	    // Retrieve the last modified date of the latest order for this store from the database
	    Optional<Orders> latestOrderOptional = orderRepository.findFirstByStoreOrderByLastmodifiedDateDesc(store);
	    System.out.println(latestOrderOptional);
	    String lastModifiedDateStr = null;

	    if (latestOrderOptional.isPresent()) {
	        Orders latestOrder = latestOrderOptional.get();
	        lastModifiedDateStr = latestOrder.getLastmodifiedDate();
	    }
	    LocalDateTime lastModifiedDate = null;
	    if (lastModifiedDateStr != null) {
	        lastModifiedDate = LocalDateTime.parse(lastModifiedDateStr);
	    }
//	    LocalDateTime lastModifiedDate = latestOrderOptional.map(Orders::getLastmodifiedDate).orElse(null);

	    // Prepare the request URL and data
	    String url = "http://127.0.0.1:8000/hello";
	    WebClient webClient = WebClient.builder().baseUrl(url).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

	    // Prepare the request body with the last modified date if available
	    String requestBody = lastModifiedDate != null ? "{\"consumerKey\":\"" + store.getConsumerKey() + "\",\"domain\":\"" + store.getDomain() + "\",\"secretConsumerKey\":\"" + store.getConsumerSecretKey() + "\",\"lastModifiedDate\":\"" + lastModifiedDate.toString() + "\"}" : "{\"consumerKey\":\"" + store.getConsumerKey() + "\",\"domain\":\"" + store.getDomain() + "\",\"secretConsumerKey\":\"" + store.getConsumerSecretKey() + "\",\"lastModifiedDate\":\"Not Found\"}" ;

	    // Make the HTTP request
	    String jsonResponse = webClient.post()
	            .uri(url)
	            .body(BodyInserters.fromValue(requestBody))
	            .retrieve()
	            .bodyToMono(String.class)
	            .block();

	    // Process the response
	    
	    System.out.println(jsonResponse);
	    
//	    try {
//	        ObjectMapper objectMapper = new ObjectMapper();
//	        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
//	        JsonNode orders = jsonNode.get("reply");
//
//	        if (orders != null && orders.isArray()) {
//	            for (JsonNode orderNode : orders) {
//	                // Extract order details from JSON response
//	                int orderId = orderNode.get("id").asInt();
//	                LocalDateTime orderModifiedDate = LocalDateTime.parse(orderNode.get("date_modified").asText());
//
//	                // Check if the order already exists in the database
//	                Orders existingOrder = orderRepository.findByOrderidAndStore(orderId, store).orElse(null);
//
//	                if (existingOrder != null) {
//	                    // If the order exists, update it if it's modifie
//	                	if (existingOrder.getLastmodifiedDate() != null) {
//	                		if (orderModifiedDate.isAfter(LocalDateTime.parse(existingOrder.getLastmodifiedDate()))) {
//		                        existingOrder.setCustomerName(orderNode.get("billing").get("first_name").asText());
//		                        // Update other fields similarly
//
//		                        // Save the updated order
//		                        orderRepository.save(existingOrder);
//		                    }
//	                		else {
//			                    // If the order doesn't exist, create a new one
//			                    Orders newOrder = new Orders();
//			                    newOrder.setOrderid(orderId);
//			                    newOrder.setStore(store);
//			                    newOrder.setCustomerName(orderNode.get("billing").get("first_name").asText());
//			                    // Set other fields similarly
//
//			                    // Save the new order
//			                    orderRepository.save(newOrder);
//			                }
//		                } 
//	                	}
//	            	         
//	            	    }
//	                    
//	            
//	        } else {
//	            System.out.println("The 'reply' field either doesn't exist or is not an array.");
//	        }
//	    } catch (Exception ex) {
//	        ex.printStackTrace();
//	    }
	}
	

	
//	public void fetchorders(int storeid) {
//		
//		Optional<Stores> storeOptional = storesRepository.findById(storeid);
//		
//		Stores store = storeOptional.orElse(null);
//		
//		String url = "http://127.0.0.1:8000/hello";
//
//        WebClient webClient = WebClient.builder().baseUrl(url).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
//        String jsonResponse = webClient.post()
//                .uri(url)
//                .body(BodyInserters.fromValue("{\"consumerKey\":\"" + store.getConsumerKey() + "\",\"domain\":\"" + store.getDomain() + "\",\"secretConsumerKey\":\"" + store.getConsumerSecretKey() + "\"}"))
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//        
//        System.out.println(jsonResponse);
//        
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            // Convert JSON string to JSON object
//            Object jsonObject = objectMapper.readValue(jsonResponse, Object.class);
//            
//            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
//            JsonNode orders = jsonNode.get("reply");
//            
//            if (orders != null && orders.isArray()) {
//                int length = orders.size();
//                for(int i=0;i<length;i++) {
//                	
//                	Orders order = new Orders();
//                    order.setOrderid(orders.get(i).get("id").asInt());
//                    order.setStore(store);
//                    order.setCustomerName(orders.get(i).get("billing").get("first_name").asText());
//                    order.setEmail(orders.get(i).get("billing").get("email").asText());
//                    order.setPhone(orders.get(i).get("billing") .get("phone").asText());
//                    order.setAddress(orders.get(i).get("billing") .get("address_1").asText());
//                    order.setCity(orders.get(i).get("billing") .get("city").asText());
//                    order.setState(orders.get(i).get("billing") .get("state").asText());
//                    order.setCountry( orders.get(i).get("billing") .get("country").asText());
//                    order.setPostcode(orders.get(i).get("billing") .get("postcode").asText());
//                    order.setTotal(orders.get(i).get("total").asText());
//                    order.setStatus(orders.get(i).get("status").asText());
//                    order.setCurrency(orders.get(i).get("currency").asText());
//                    order.setPaymentMethod(orders.get(i).get("payment_method").asText());
//                    order.setProduct(orders.get(i).get("line_items").get(0).get("name").asText());
//                    order.setOrderCreatedDate(orders.get(i).get("date_created").asText());
//                    order.setLastmodifiedDate(orders.get(i).get("date_modified").asText());
//
//                    orderRepository.save(order);
//                               
//                }
//
//            } else {
//                System.out.println("The 'reply' field either doesn't exist or is not an array.");
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        
//}
//	

}


