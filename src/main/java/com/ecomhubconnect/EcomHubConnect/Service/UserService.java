package com.ecomhubconnect.EcomHubConnect.Service;

import com.ecomhubconnect.EcomHubConnect.Entity.Users;

public interface UserService {

	public Users saveUser(Users user);

	public void removeSessionMessage();

}