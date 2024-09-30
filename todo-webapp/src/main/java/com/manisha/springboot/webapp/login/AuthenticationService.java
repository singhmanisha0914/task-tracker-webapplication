package com.manisha.springboot.webapp.login;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service //Because it has business logic, to create bean of this class we are using @Service. @Component can also be used but recommended to use @Service. 
public class AuthenticationService {
	
	public boolean authentication(String username, String password) {
		boolean isValidUsername = username.equalsIgnoreCase("Manisha");
		boolean isValidPassword = password.equalsIgnoreCase("dummy");
		return isValidUsername && isValidPassword;
	}
}
