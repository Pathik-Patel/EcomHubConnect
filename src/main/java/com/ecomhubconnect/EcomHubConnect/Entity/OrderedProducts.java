package com.ecomhubconnect.EcomHubConnect.Entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
public class OrderedProducts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int quantity;
	private int price;
	private int productid;
	private String productName;
	private String orderStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
    private Timestamp orderCreatedDate;

	
	@ToString.Exclude
	@ManyToOne
	@JsonIgnore
    private Stores store;
	
	@ToString.Exclude
	@ManyToOne
	@JsonIgnore
    private Orders order;
	
}
