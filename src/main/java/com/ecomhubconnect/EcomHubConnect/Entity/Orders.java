package com.ecomhubconnect.EcomHubConnect.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
public class Orders {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int orderid;
	private int storeid;
	private String customerName;
	private String email;
	private String phone;
	private String address;
	private String city;
	private String state;
	private String country;
	private String postcode;
	private String total;
	private String status;
	private String currency;
	private String paymentMethod;
	private String product;
	private String orderCreatedDate;
	private String lastmodifiedDate;
	
	@ToString.Exclude
	@ManyToOne
	@JsonIgnore
    private Stores store;
}
