package com.ecomhubconnect.EcomHubConnect.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecomhubconnect.EcomHubConnect.Entity.Stores;
import com.ecomhubconnect.EcomHubConnect.Entity.Users;

public interface StoreRepository extends JpaRepository<Stores, Integer>{

}
