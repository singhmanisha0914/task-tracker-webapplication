package com.manisha.springboot.webapp.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("name")
public class LoginController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private AuthenticationService authenticationService;
	
	// Use construction injection to instantiate authenticationService object. Right click -> source -> generate constructors using fields -> select authenticationService ->finish
	public LoginController(AuthenticationService authenticationService) {
		super();
		this.authenticationService = authenticationService;
	}
	
	@RequestMapping(value="/", method=RequestMethod.GET)  //if we donot specify method, it accepts both GET and POST
	public String goToLoginPage(ModelMap model) {
		//logger.debug("Request parameter is: {}", name);
		model.put("name", "Manisha");
		return "welcome";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)  //if we donot specify method, it accepts both GET and POST
	public String goToWelcomePage(@RequestParam String name, @RequestParam String password, ModelMap model) {
		if (authenticationService.authentication(name, password)) {
			model.put("name", name);
			model.put("password", password);
			
			//Authentication: if credentials are valid, redirect to welcome page and if not redirect to login page
			return "welcome";
		}
		
		// Credentials were incorrect. Pass this message from the Controller to the View using model.
		model.put("errorMessage", "Invalid Credentials! Please try again.");
		return "login";
	}

}
