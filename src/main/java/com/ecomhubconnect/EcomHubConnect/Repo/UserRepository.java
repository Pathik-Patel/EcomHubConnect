package com.ecomhubconnect.EcomHubConnect.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

//import com.ecomhubconnect.EcomHubConnect.Entity.User;
import com.ecomhubconnect.EcomHubConnect.Entity.Users;

public interface UserRepository extends JpaRepository<Users, Integer>{

	
	Optional<Users> findByEmail(String emaill);
	
	
}
