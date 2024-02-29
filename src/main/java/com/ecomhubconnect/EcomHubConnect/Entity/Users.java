package com.ecomhubconnect.EcomHubConnect.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Users{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userid;
	private String firstname;
	private String lastname;
	private String email;
	private String mobile;
	private String password;
	private String active;
	private String role;
	
}
