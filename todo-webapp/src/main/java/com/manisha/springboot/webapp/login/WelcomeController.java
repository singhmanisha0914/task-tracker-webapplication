package com.manisha.springboot.webapp.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("name")
public class WelcomeController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value="/", method=RequestMethod.GET)  //if we donot specify method, it accepts both GET and POST
	public String goToWelcomePage(ModelMap model) {
		//logger.debug("Request parameter is: {}", name);
		model.put("name", getLoggedinUsername());
		return "welcome";
	}

	public String getLoggedinUsername() {
		//get the authenticated current username
		Authentication authentication = 
				SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
		
	}
}
