package com.manisha.springboot.webapp.withJPA;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manisha.springboot.webapp.taskTracker.TaskTracker;
import com.manisha.springboot.webapp.taskTracker.TaskTrackerService;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("name") //we will use this session data in listTasks.jsp
public class TaskTrackerControllerJpa {
	
	private TaskTrackerServiceJpa service;
	
	//Autowirig taskTrackerService using constructor injection
	public TaskTrackerControllerJpa(TaskTrackerServiceJpa service) {
		super();
		this.service = service;
	}


	@RequestMapping("/list-tasks")
	public String listAllTasks(ModelMap model) {
		String username = getLoggedInUsername(model);
		List<TaskTracker> tasks = service.findByUsername(username);
		model.addAttribute("tasks", tasks);
		return "listTasks";
	}
	
	@RequestMapping(value="/add-task", method=RequestMethod.GET)
	public String showNewTaskPage(ModelMap model) {
		String username = getLoggedInUsername(model);
		//creating a default tastTracker object
		TaskTracker taskTracker = new TaskTracker(0, username, "Default desc", LocalDate.now().plusYears(1), false);
		model.put("taskTracker", taskTracker);
		return "task";
	}


	private String getLoggedinUsername(ModelMap model) {
		return (String)model.get("name");
	}
	
	@RequestMapping(value="/add-task", method=RequestMethod.POST)
	//@Valid is ensure that taskTracker Bean is validated before the binding happens
	public String addNewTask(ModelMap model, @Valid TaskTracker taskTracker, BindingResult result) {
		//if the string length is less than 10 characters for description then do not display /list-tasks instead fallback to task.jsp
		if(result.hasErrors()) {
			return "task";
		}
		String username = getLoggedInUsername(model);
		taskTracker.setUsername(username);
		service.addTask(taskTracker);
		return "redirect:list-tasks"; //redirect:<name of the url>
	}
	
	@RequestMapping("/delete-task")
	public String deleteTask(@RequestParam int id,ModelMap model) {
		//Delete task with the specific id and return the new task list
		service.deleteTaskById(id);
		return "redirect:/list-tasks";
	}
	
	@RequestMapping(value="/update-task", method=RequestMethod.GET)
	public String showUpdateTaskPage(@RequestParam int id,ModelMap model) {
		//Get the task object with the specific id
		TaskTracker taskTracker = service.findById(id);
		model.addAttribute("taskTracker", taskTracker);
		return "task";
	}
	
	@RequestMapping(value="/update-task", method=RequestMethod.POST)
	public String updateTask(ModelMap model, @Valid TaskTracker taskTracker, BindingResult result) {
		if(result.hasErrors()) {
			return "task";
		}
		String username= getLoggedInUsername(model);
		taskTracker.setUsername(username);
		service.updateTask(taskTracker);
		return "redirect:list-tasks";
	}


	private String getLoggedInUsername(ModelMap model) {
		Authentication authentication =
				SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}
}
