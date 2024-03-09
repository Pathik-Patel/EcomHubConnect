package com.ecomhubconnect.EcomHubConnect.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
	private int userid;
	private String firstname;
	private String lastname;
	private String email;
	private String mobile;
	private String password;
	private String active;
	
}
