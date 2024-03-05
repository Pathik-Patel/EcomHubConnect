package com.ecomhubconnect.EcomHubConnect.Entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Stores {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int storeid;
	
	private String domain;
	private String consumerKey;
	private String consumerSecretKey;
	
	
	@ManyToOne
    private Users user;
	
	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Orders> orders;

	
}
