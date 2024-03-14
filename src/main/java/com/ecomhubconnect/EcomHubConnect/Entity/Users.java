package com.ecomhubconnect.EcomHubConnect.Entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
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
    
	
	 // Define the one-to-many relationship with Stores
	@ToString.Exclude
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Stores> stores;
	
}
