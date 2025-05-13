package com.ecomhubconnect.EcomHubConnect.Service;

import java.sql.Timestamp;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapSetter;
import io.opentelemetry.api.GlobalOpenTelemetry;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.ecomhubconnect.EcomHubConnect.Controller.HomeController;
import com.ecomhubconnect.EcomHubConnect.Entity.OrderedProducts;
import com.ecomhubconnect.EcomHubConnect.Entity.Orders;
import com.ecomhubconnect.EcomHubConnect.Entity.Stores;
import com.ecomhubconnect.EcomHubConnect.Entity.Users;
import com.ecomhubconnect.EcomHubConnect.Repo.OrderRepository;
import com.ecomhubconnect.EcomHubConnect.Repo.OrderedProductsRepository;
import com.ecomhubconnect.EcomHubConnect.Repo.StoreRepository;
import com.ecomhubconnect.EcomHubConnect.Repo.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WoocommerceService {
	
	private final WebClient webClient;

    // Let Spring inject the WebClient
    public WoocommerceService(WebClient webClient) {
        this.webClient = webClient;
    }
	
	private static final Logger logger = LoggerFactory.getLogger(WoocommerceService.class);

	@Autowired
	private StoreRepository storesRepository;

	@Autowired
	private UserRepository usersRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderedProductsRepository orderedProductsRepository;

	public String addstore(String consumerKey, String domain, String secretConsumerKey, String storename) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {

			Users user = usersRepository.findByEmail(authentication.getName())
					.orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
			;
			if (user != null) {
				Stores store = new Stores();
				store.setDomain(domain);
				store.setConsumerKey(consumerKey);
				store.setConsumerSecretKey(secretConsumerKey);
				store.setStorename(storename);
				store.setUser(user);

				storesRepository.save(store);

				return "Success";
			} else {

				return "";
			}

		}

		return null;
	}

	public void syncOrders(int storeId) {
		
		logger.info("Came into SyncOrders");
	    

		Optional<Stores> storeOptional = storesRepository.findById(storeId);

		System.out.println(storeOptional);
		Stores store = storeOptional.orElse(null);

		if (store == null) {
			return;
		}

		Optional<Orders> latestOrderOptional = orderRepository.findFirstByStoreOrderByLastmodifiedDateDesc(store);
		System.out.println(latestOrderOptional);
		String lastModifiedDateStr = null;
		LocalDateTime lastModifiedDate = null;
		if (latestOrderOptional.isPresent()) {
			Orders latestOrder = latestOrderOptional.get();

			if (latestOrder.getLastmodifiedDate() != null) {

				lastModifiedDate = latestOrder.getLastmodifiedDate().toLocalDateTime();
			}
		}
		

		String url = "/hello";
//		WebClient webClient = WebClient.builder().baseUrl(url)
//				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//				.build();

		String requestBody = lastModifiedDate != null
				? "{\"consumerKey\":\"" + store.getConsumerKey() + "\",\"domain\":\"" + store.getDomain()
						+ "\",\"secretConsumerKey\":\"" + store.getConsumerSecretKey() + "\",\"lastModifiedDate\":\""
						+ lastModifiedDate.toString() + "\"}"
				: "{\"consumerKey\":\"" + store.getConsumerKey() + "\",\"domain\":\"" + store.getDomain()
						+ "\",\"secretConsumerKey\":\"" + store.getConsumerSecretKey()
						+ "\",\"lastModifiedDate\":\"Not Found\"}";

		String jsonResponse = webClient.post().uri(url).body(BodyInserters.fromValue(requestBody)).retrieve()
				.bodyToMono(String.class).block();

		System.out.println(jsonResponse);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(jsonResponse);
			JsonNode orders = jsonNode.get("reply");

			if (orders != null && orders.isArray()) {
				for (JsonNode orderNode : orders) {
					int orderId = orderNode.get("id").asInt();

					LocalDateTime ModifieddateTime = LocalDateTime.parse(orderNode.get("date_modified").asText(),
							formatter);
					Timestamp lastmodifiedtimestamp = Timestamp.valueOf(ModifieddateTime);

					Orders existingOrder = orderRepository.findByOrderidAndStore(orderId, store).orElse(null);

					if (existingOrder != null) {
						System.out.println(orderId);
						if (existingOrder.getLastmodifiedDate() != null) {
							System.out.println(orderId);
							if (lastmodifiedtimestamp.compareTo(existingOrder.getLastmodifiedDate()) > 0) {
								System.out.println(orderId);

								LocalDateTime NewCreateddateTime = LocalDateTime
										.parse(orderNode.get("date_created").asText(), formatter);
								Timestamp newcreatetimestamp = Timestamp.valueOf(NewCreateddateTime);

								LocalDateTime NewModifieddateTime = LocalDateTime
										.parse(orderNode.get("date_modified").asText(), formatter);
								Timestamp newlastmodifiedtimestamp = Timestamp.valueOf(NewModifieddateTime);

								existingOrder.setCustomerName(orderNode.get("billing").get("first_name").asText());
								existingOrder.setEmail(orderNode.get("billing").get("email").asText());
								existingOrder.setPhone(orderNode.get("billing").get("phone").asText());
								existingOrder.setAddress(orderNode.get("billing").get("address_1").asText());
								existingOrder.setCity(orderNode.get("billing").get("city").asText());
								existingOrder.setState(orderNode.get("billing").get("state").asText());
								existingOrder.setCountry(orderNode.get("billing").get("country").asText());
								existingOrder.setPostcode(orderNode.get("billing").get("postcode").asText());
								existingOrder.setTotal(orderNode.get("total").asText());
								existingOrder.setStatus(orderNode.get("status").asText());
								existingOrder.setCurrency(orderNode.get("currency").asText());
								existingOrder.setPaymentMethod(orderNode.get("payment_method").asText());
								existingOrder.setProduct(orderNode.get("line_items").get(0).get("name").asText());
								existingOrder.setOrderCreatedDate(newcreatetimestamp);
								existingOrder.setLastmodifiedDate(newlastmodifiedtimestamp);
								// Update other fields similarly

								orderRepository.save(existingOrder);
							}

						}
					} else {
						Orders order = new Orders();

						LocalDateTime CreateddateTime = LocalDateTime.parse(orderNode.get("date_created").asText(),
								formatter);
						Timestamp createtimestamp = Timestamp.valueOf(CreateddateTime);

						LocalDateTime toStoreModifieddateTime = LocalDateTime
								.parse(orderNode.get("date_modified").asText(), formatter);
						Timestamp tostorelastmodifiedtimestamp = Timestamp.valueOf(toStoreModifieddateTime);

						order.setOrderid(orderNode.get("id").asInt());
						order.setStore(store);
						order.setCustomerName(orderNode.get("billing").get("first_name").asText());
						order.setEmail(orderNode.get("billing").get("email").asText());
						order.setPhone(orderNode.get("billing").get("phone").asText());
						order.setAddress(orderNode.get("billing").get("address_1").asText());
						order.setCity(orderNode.get("billing").get("city").asText());
						order.setState(orderNode.get("billing").get("state").asText());
						order.setCountry(orderNode.get("billing").get("country").asText());
						order.setPostcode(orderNode.get("billing").get("postcode").asText());
						order.setTotal(orderNode.get("total").asText());
						order.setStatus(orderNode.get("status").asText());
						order.setCurrency(orderNode.get("currency").asText());
						order.setPaymentMethod(orderNode.get("payment_method").asText());
						order.setProduct(orderNode.get("line_items").get(0).get("name").asText());
						order.setOrderCreatedDate(createtimestamp);
						order.setLastmodifiedDate(tostorelastmodifiedtimestamp);

						orderRepository.save(order);

						JsonNode products = orderNode.get("line_items");

						if (products != null && products.isArray()) {

							for (JsonNode productNode : products) {
								OrderedProducts product = new OrderedProducts();
								int productId = productNode.get("product_id").asInt();
								product.setOrder(order);
								product.setOrderCreatedDate(order.getOrderCreatedDate());
								product.setOrderStatus(order.getStatus());
								product.setProductid(productId);
								product.setProductName(productNode.get("name").asText());
								product.setStore(store);
								product.setPrice(productNode.get("price").asInt());
								product.setQuantity(productNode.get("quantity").asInt());
								orderedProductsRepository.save(product);
								orderedProductsRepository.save(product);
							}
						}
					}

				}

			} else {
				System.out.println("The 'reply' field either doesn't exist or is not an array.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
	}

}
