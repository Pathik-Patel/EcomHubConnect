package com.ecomhubconnect.EcomHubConnect.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

//import com.ecomhubconnect.EcomHubConnect.Entity.User;
import com.ecomhubconnect.EcomHubConnect.Entity.Users;

public interface UserRepository extends JpaRepository<Users, Integer>{

	
	public Users findByEmail(String emaill);
	
	
}
