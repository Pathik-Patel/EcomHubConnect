package com.ecomhubconnect.EcomHubConnect.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecomhubconnect.EcomHubConnect.Entity.OrderedProducts;
import com.ecomhubconnect.EcomHubConnect.Entity.Orders;

public interface OrderedProductsRepository extends JpaRepository<OrderedProducts, Integer>{
	List<OrderedProducts> findByOrderIdIn(List<Integer> orderIds);
}
 