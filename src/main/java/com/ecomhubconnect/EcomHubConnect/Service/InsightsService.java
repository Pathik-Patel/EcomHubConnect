package com.ecomhubconnect.EcomHubConnect.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.query.NativeQuery.ReturnableResultNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecomhubconnect.EcomHubConnect.Entity.OrderedProducts;
import com.ecomhubconnect.EcomHubConnect.Entity.Orders;
import com.ecomhubconnect.EcomHubConnect.Entity.Stores;
import com.ecomhubconnect.EcomHubConnect.Repo.OrderRepository;
import com.ecomhubconnect.EcomHubConnect.Repo.OrderedProductsRepository;
import com.ecomhubconnect.EcomHubConnect.Repo.StoreRepository;
import com.ecomhubconnect.EcomHubConnect.Repo.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class InsightsService {
	@Autowired
	private StoreRepository storesRepository;

	@Autowired
	private UserRepository usersRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderedProductsRepository orderedProductsRepository;
	
	
	
	public String gettopfiveproductsforgivenstore(int storeid,Timestamp startdate, Timestamp enddate) {
	    
	    
	    // Get the store from the storeid
	    Stores store = new Stores();
	    store.setStoreid(storeid); // Set the storeid

	    // Find orders for the given store in the last 3 months
	    List<Orders> orders = orderRepository.findByStoreAndOrderCreatedDateBetween( store, startdate, enddate);

	    if (orders.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No orders found for store").toString();
	    }

	    // Get the order ids for the found orders
	    List<Integer> orderIds = orders.stream().map(Orders::getId).collect(Collectors.toList());

	    // Find ordered products for the found orders
	    List<OrderedProducts> orderedProducts = orderedProductsRepository.findByOrderIdIn(orderIds);

	    // Group the ordered products by productid and sum the quantities
	    Map<Integer, Integer> productQuantities = new HashMap<>();
	    Map<Integer, String> productNames = new HashMap<>();
	    Map<Integer, Integer> productPrices = new HashMap<>();
	    
	    for (OrderedProducts product : orderedProducts) {
	        int productId = product.getProductid();
	        productQuantities.merge(productId, product.getQuantity(), Integer::sum);
	        productNames.put(productId, product.getProductName());
	        productPrices.put(productId, product.getPrice());
	    }

	    // Sort the products by quantity sold and get the top 5
	    List<Map<String, Object>> topProducts = productQuantities.entrySet().stream()
	            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
	            .limit(5)
	            .map(entry -> {
	                int productId = entry.getKey();
	                int quantitySold = entry.getValue();
	                String productName = productNames.get(productId);
	                int price = productPrices.get(productId);
	                int revenue = quantitySold * price;
	                Map<String, Object> productInfo = new HashMap<>();
	                productInfo.put("productId", productId);
	                productInfo.put("productName", productName);
	                productInfo.put("quantitySold", quantitySold);
	                productInfo.put("revenue", revenue);
	                return productInfo;
	            })
	            .collect(Collectors.toList());
	    
	    // Serialize topProducts to JSON using Jackson
	    ObjectMapper mapper = new ObjectMapper();
	    try {
	        return mapper.writeValueAsString(topProducts);
	    } catch (JsonProcessingException e) {
	        e.printStackTrace();
	        return null; // Handle serialization error
	    }
	}

	
}
