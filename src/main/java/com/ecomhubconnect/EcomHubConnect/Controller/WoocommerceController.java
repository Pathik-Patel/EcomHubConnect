package com.ecomhubconnect.EcomHubConnect.Controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.ecomhubconnect.EcomHubConnect.Config.AppException;
import com.ecomhubconnect.EcomHubConnect.Entity.OrderedProducts;
import com.ecomhubconnect.EcomHubConnect.Entity.Orders;
import com.ecomhubconnect.EcomHubConnect.Entity.Stores;
import com.ecomhubconnect.EcomHubConnect.Entity.Users;
import com.ecomhubconnect.EcomHubConnect.Repo.OrderRepository;
import com.ecomhubconnect.EcomHubConnect.Repo.OrderedProductsRepository;
import com.ecomhubconnect.EcomHubConnect.Repo.StoreRepository;
import com.ecomhubconnect.EcomHubConnect.Repo.UserRepository;
import com.ecomhubconnect.EcomHubConnect.Service.InsightsService;
import com.ecomhubconnect.EcomHubConnect.Service.WoocommerceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin
@RequestMapping("/woocommerce")
public class WoocommerceController {
	
	private static final Logger logger = LoggerFactory.getLogger(WoocommerceController.class);
	

	private WoocommerceService wooCommerceService;
	
	private InsightsService insightsService;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
    private OrderedProductsRepository orderedProductsRepository;

	@Autowired
	public WoocommerceController(WoocommerceService wooCommerceService, InsightsService insightsService) {
		this.wooCommerceService = wooCommerceService;
		this.insightsService = insightsService;
	}

	@PostMapping("/addstore")
	public ResponseEntity<String> home(@RequestBody Map<String, Object> connection_credentials, HttpSession session,
			Model m) {
	
		wooCommerceService.addstore(connection_credentials.get("consumerKey").toString(),
				connection_credentials.get("domain").toString(),
				connection_credentials.get("consumerSecret").toString(),connection_credentials.get("storename").toString());

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
			user = userRepo.findByEmail(authentication.getName())
					.orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
			;
			if (user != null) {

				List<Stores> userStores = storeRepository.findByUser(user);

				return ResponseEntity.ok(userStores);
			} else {
				return ResponseEntity.notFound().build();
			}

		}
		Map<String, String> response = new HashMap<>();
		response.put("Message", "User not found");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

	}

	@GetMapping("/getforecasting/{storeid}")
	public Mono<ResponseEntity<String>> getforecasting(@PathVariable int storeid) {
	    List<Object> tempList = orderRepository.findTotalAndOrderCreatedAtByStoreid(storeid);
	    
	    String url = "http://127.0.0.1:8000/getforecasting";
	    WebClient webClient = WebClient.builder().baseUrl(url)
	            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

	    ObjectMapper objectMapper = new ObjectMapper();

	    // Convert the list of JSON objects to a JSON array
	    try {
	        String jsonArrayString = objectMapper.writeValueAsString(tempList);
	        return webClient.post()
	                .body(BodyInserters.fromValue(jsonArrayString))
	                .retrieve()
	                .toEntity(String.class)
	                .map(responseEntity -> {
	                    String responseBody = responseEntity.getBody();
	                    // Handle the response if needed
	                    System.out.println("Response received: " + responseBody);
	                    return ResponseEntity.ok(responseBody);
	                });
	    } catch (Exception e) {
	        // Handle exception
	        e.printStackTrace();
	        // Return an error response
	        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
	    }
	}

	
	@GetMapping("/syncorders/{storeid}")
	public ResponseEntity<?> syncorders(@PathVariable int storeid) {
		
		logger.info("Came into Woocommerce Controller");
		
		wooCommerceService.syncOrders(storeid);
		Stores userStore = storeRepository.findById(storeid).orElseThrow(() -> new AppException("No Stores for this ID", HttpStatus.NOT_FOUND));;;
		List<Orders> orders = orderRepository.findByStore(userStore);
		System.out.println(orders);
		if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No orders found for store " + storeid);
        } else {
            return ResponseEntity.ok(orders);
        }
		
	}
	
	@PostMapping("/insights/{storeid}")
    public ResponseEntity<?> getInsightsOfStore(@PathVariable int storeid, @RequestBody String dateRange) {
		
		
		ObjectMapper objectMapper = new ObjectMapper();

        // Convert JSON string to JSON object
		 JsonNode dates;
		try {
			dates = objectMapper.readTree(dateRange);
		
		
		LocalDate startDate = LocalDate.parse(dates.get("startdate").asText());
        LocalDateTime startOfDay = startDate.atStartOfDay();
        Timestamp startdateTimestamp =  Timestamp.valueOf(startOfDay);
        
        LocalDate endDate = LocalDate.parse(dates.get("enddate").asText());
        LocalDateTime endOfDay = endDate.atTime(23, 59, 59, 999999999);
        Timestamp enddateTimestamp =  Timestamp.valueOf(endOfDay);
        
        System.out.println(startdateTimestamp);
        System.out.println(enddateTimestamp);
		
		
		String topfiveproducts = insightsService.gettopfiveproductsforgivenstore(storeid, startdateTimestamp, enddateTimestamp);
		
		
		
		Map<String, Object> totalordersandsale = orderRepository.getTotalOrdersAndSale(storeid, startdateTimestamp, enddateTimestamp);
		
		
		
		List<Object[]> topfivestatesList = orderRepository.findTop5StatesByOrderCount(storeid, startdateTimestamp, enddateTimestamp);
		
		
		
		for (Object[] stateData : topfivestatesList) {
		    String state = (String) stateData[0]; // State
		    Long stateCount = (Long) stateData[1]; // State count
		    Double totalOrdersCount = (Double) stateData[3]; // Total orders count

		    // Print state along with its values
		    System.out.println("State: " + state);
		    System.out.println("State Count: " + stateCount);
		    System.out.println("Total Orders Count: " + totalOrdersCount);
		}
		
		ObjectMapper mapper = new ObjectMapper();
	    
	    // Create a map to represent your data
	    Map<String, Object> responseData = new HashMap<>();
	    responseData.put("topfiveproducts", topfiveproducts);
	    responseData.put("totalordersandsale", totalordersandsale);
	    responseData.put("topfivestatesList", topfivestatesList);
	    
	    try {
	        // Convert the map to a JSON string
	        String jsonResponse = mapper.writeValueAsString(responseData);
	        return ResponseEntity.ok(jsonResponse);
	        
	    } catch (JsonProcessingException e) {
	        // Handle JSON processing exception
	        e.printStackTrace();
	        return null;
	    }

		
		}
		catch (JsonProcessingException e){
			return null;
		}
    }
}
