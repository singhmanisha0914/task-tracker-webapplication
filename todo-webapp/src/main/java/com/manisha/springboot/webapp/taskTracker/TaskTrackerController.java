package com.manisha.springboot.webapp.taskTracker;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import jakarta.validation.Valid;

@Controller
@SessionAttributes("name") //we will use this session data in listTasks.jsp
public class TaskTrackerController {
	
	private TaskTrackerService taskTrackerService;
	
	public TaskTrackerController(TaskTrackerService taskTrackerService) {
		super();
		this.taskTrackerService = taskTrackerService;
	}


	@RequestMapping("/list-tasks")
	public String listAllTasks(ModelMap model) {
		List<TaskTracker> tasks = taskTrackerService.findByUsername("Manisha");
		model.addAttribute("tasks", tasks);
		return "listTasks";
	}
	
	@RequestMapping(value="/add-task", method=RequestMethod.GET)
	public String showNewTaskPage(ModelMap model) {
		String username = (String)model.get("name");
		//creating a default tastTracker object
		TaskTracker taskTracker = new TaskTracker(0, username, "Default desc", LocalDate.now().plusYears(1), false);
		model.put("taskTracker", taskTracker);
		return "task";
	}
	
	@RequestMapping(value="/add-task", method=RequestMethod.POST)
	//@Valid is ensure that taskTracker Bean is validated before the binding happens
	public String addNewTask(ModelMap model, @Valid TaskTracker taskTracker, BindingResult result) {
		//if the string length is less than 10 characters for description then do not display /list-tasks instead fallback to task.jsp
		if(result.hasErrors()) {
			return "task";
		}
		String username = (String)model.get("name");
		//adding the taskTracker object details received from the task.jsp form
		taskTrackerService.addTask(username, taskTracker.getDescription(), taskTracker.getTargetDate(), false);
		//redirect:<name of the url>
		return "redirect:list-tasks";
	}
	
	@RequestMapping("/delete-task")
	public String deleteTask(@RequestParam int id,ModelMap model) {
		//Delete task with the specific id and return the new task list
		taskTrackerService.deleteTaskById(id);
		return "redirect:/list-tasks";
	}
	
	@RequestMapping(value="/update-task", method=RequestMethod.GET)
	public String showUpdateTaskPage(@RequestParam int id,ModelMap model) {
		//Get the task object with the specific id
		TaskTracker taskTracker = taskTrackerService.findById(id);
		model.addAttribute("taskTracker", taskTracker);
		return "task";
	}
	
	@RequestMapping(value="/update-task", method=RequestMethod.POST)
	public String updateTask(ModelMap model, @Valid TaskTracker taskTracker, BindingResult result) {
		if(result.hasErrors()) {
			return "task";
		}
		String username= (String)model.get("name");
		taskTracker.setUsername(username);
		taskTrackerService.updateTask(taskTracker);
		return "redirect:list-tasks";
	}
}
