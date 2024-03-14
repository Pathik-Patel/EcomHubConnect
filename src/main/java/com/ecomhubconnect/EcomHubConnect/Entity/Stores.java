package com.ecomhubconnect.EcomHubConnect.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
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
@Entity
public class Stores {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int storeid;
	
	private String domain;
	private String consumerKey;
	private String consumerSecretKey;
	
	
	@ToString.Exclude
	@ManyToOne
	@JsonIgnore
    private Users user;
	
	
	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Orders> orders;

	
}
