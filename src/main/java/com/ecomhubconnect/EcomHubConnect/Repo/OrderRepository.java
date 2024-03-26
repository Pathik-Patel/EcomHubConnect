package com.ecomhubconnect.EcomHubConnect.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecomhubconnect.EcomHubConnect.Entity.Orders;
import com.ecomhubconnect.EcomHubConnect.Entity.Stores;
import com.ecomhubconnect.EcomHubConnect.Entity.Users;

public interface OrderRepository extends JpaRepository<Orders, Integer>{

	Optional<Orders> findFirstByStoreOrderByLastmodifiedDateDesc(Stores store);

	Optional<Orders> findByOrderidAndStore(int orderId, Stores store);
	
	List<Orders> findByStore(Stores store);

	
	
}
