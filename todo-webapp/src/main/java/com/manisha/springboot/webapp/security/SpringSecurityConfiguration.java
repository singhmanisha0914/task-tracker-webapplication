package com.manisha.springboot.webapp.security;

import java.util.function.Function;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//This class would contain configuration for number of beans. We will directly create beans here and return them back.
@Configuration
public class SpringSecurityConfiguration {
	
	/*Typically whenever we want to store username and password we make use of LDAP or atleast a database
	For demo project, we will use In Memory configuration
	InMemoryUserDetailsManager
	InMemoryUserDetailsManager(UserDetails... users) --> this constructor we will use
	We will create a UserDetails object and we will build an in-memory UserDetails manager class and return it back
	*/
	@Bean
	public InMemoryUserDetailsManager createUserDetailsManager() {
		
		UserDetails userDetails1 = createNewUser("Durga", "dummy");
		UserDetails userDetails2 = createNewUser("Manisha", "dummydummy");
		
		return new InMemoryUserDetailsManager(userDetails1, userDetails2);
	}

	private UserDetails createNewUser(String username, String password) {
		// lambda function which accepts a String input and returns String as output. It will take input, encode it using passwordEncoder() and use it to configure the userDetails.
		Function<String, String> passwordEncoder 
					= input -> passwordEncoder().encode(input);
		
		//UserDetails is an interface so we will use builder
		UserDetails userDetails = User.builder()
				.passwordEncoder(passwordEncoder)
				.username(username)
				.password(password)
				.roles("USER","ADMIN")
				.build();
		return userDetails;
	}
	
	//withDefaultPasswordEncoder() is deprecated. So we will create a custom encoder
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		//authorize all requests
		httpSecurity.authorizeHttpRequests(
				//define lambda function for authorization
				auth -> auth.anyRequest().authenticated());
		
		//If there is an unauthorized request then show login form
		httpSecurity.formLogin(withDefaults());
		
		httpSecurity.csrf().disable();
		httpSecurity.headers().frameOptions().disable();
		return httpSecurity.build();
	}
	
}
