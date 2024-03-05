package com.ecomhubconnect.EcomHubConnect.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecomhubconnect.EcomHubConnect.Entity.Orders;
import com.ecomhubconnect.EcomHubConnect.Entity.Users;

public interface OrderRepository extends JpaRepository<Orders, Integer>{
	
}
