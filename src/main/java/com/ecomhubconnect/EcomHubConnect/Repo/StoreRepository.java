package com.ecomhubconnect.EcomHubConnect.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecomhubconnect.EcomHubConnect.Entity.Stores;
import com.ecomhubconnect.EcomHubConnect.Entity.Users;

public interface StoreRepository extends JpaRepository<Stores, Integer>{
	List<Stores> findByUser(Users user);
}
