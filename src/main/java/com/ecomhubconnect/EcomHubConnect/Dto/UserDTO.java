package com.ecomhubconnect.EcomHubConnect.Dto;

public class UserDTO {
	private int userid;
	private String firstname;
	private String lastname;
	private String email;
	private String mobile;
	private String password;
	private String active;
	
	
	public int getUserid() {
		return userid;
	}
	public UserDTO() {
		
	}
	public UserDTO(int userid, String firstname, String lastname, String email, String mobile, String password,
			String active) {
		super();
		this.userid = userid;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.mobile = mobile;
		this.password = password;
		this.active = active;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	
	
	
}
