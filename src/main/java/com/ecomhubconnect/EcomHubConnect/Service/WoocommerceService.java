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

			Users user = usersRepository.findByEmail(authentication.getName())
					.orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
			;
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

	public void syncOrders(int storeId) {

		Optional<Stores> storeOptional = storesRepository.findById(storeId);

		System.out.println(storeOptional);
		Stores store = storeOptional.orElse(null);

		if (store == null) {
			return;
		}

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
		String url = "http://127.0.0.1:8000/hello";
		WebClient webClient = WebClient.builder().baseUrl(url)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

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

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(jsonResponse);
			JsonNode orders = jsonNode.get("reply");

			if (orders != null && orders.isArray()) {
				for (JsonNode orderNode : orders) {
					int orderId = orderNode.get("id").asInt();

					LocalDateTime orderModifiedDate = LocalDateTime.parse(orderNode.get("date_modified").asText());

					Orders existingOrder = orderRepository.findByOrderidAndStore(orderId, store).orElse(null);

					if (existingOrder != null) {
						System.out.println(orderId);
						if (existingOrder.getLastmodifiedDate() != null) {
							System.out.println(orderId);
							if (orderModifiedDate.isAfter(LocalDateTime.parse(existingOrder.getLastmodifiedDate()))) {
								System.out.println(orderId);

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
								existingOrder.setOrderCreatedDate(orderNode.get("date_created").asText());
								existingOrder.setLastmodifiedDate(orderNode.get("date_modified").asText());
								// Update other fields similarly

								orderRepository.save(existingOrder);
							}

						}
					} else {
						Orders order = new Orders();
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
						order.setOrderCreatedDate(orderNode.get("date_created").asText());
						order.setLastmodifiedDate(orderNode.get("date_modified").asText());

						orderRepository.save(order);

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
