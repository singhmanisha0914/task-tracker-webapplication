package com.manisha.springboot.webapp.taskTracker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

@Service
public class TaskTrackerService {
	// create a list of static TaskTracker objects to test the functionality
	private static List<TaskTracker> tasks = new ArrayList<>();
	
	private static int taskCount = 0;
	static {
		tasks.add(new TaskTracker(++taskCount, "in28minutes","Learn AWS Practitioner", 
							LocalDate.now().plusYears(1), false ));
		tasks.add(new TaskTracker(++taskCount, "in28minutes","Learn DevOps", 
				LocalDate.now().plusYears(2), false ));
		tasks.add(new TaskTracker(++taskCount, "in28minutes","Learn Full Stack Development", 
				LocalDate.now().plusYears(3), false ));
	}
	
	//Define a method to return the tasks specific to a user
	public List<TaskTracker> findByUsername(String username){
		return tasks;
	}
	
	//Define a method to add a new task to the table
	public void addTask(String username, String description, LocalDate targetDate, boolean done) {
		TaskTracker newTask = new TaskTracker(++taskCount, username, description,targetDate, done);
		tasks.add(newTask);
	}
	
	//Define a method to delete a task by id from the table
	public void deleteTaskById(int id) {
		//<Name of the bean> -> <the predicate>
		//taskTracker -> taskTracker.getId() == id
		//for every bean in the tasks list, execute this condition and if condition meets then remove that Bean.
		//predicate is basically a condition. Simplest way to define a predicate is by using lambda function
		Predicate<? super TaskTracker> predicate = taskTracker -> taskTracker.getId() == id;
		tasks.removeIf(predicate);
	}
	
	//Define a method to find a task Bean by id from the table
	public TaskTracker findById(int id) {
		Predicate<? super TaskTracker> predicate = taskTracker -> taskTracker.getId() == id;
		//create a stream of tasks | filter it using the predicate defined earlier | from the filtered list find the first match | once the element is found, get it
		TaskTracker taskTracker = tasks.stream().filter(predicate).findFirst().get();
		return taskTracker;
	}

	public void updateTask(@Valid TaskTracker taskTracker) {
		//Instead of updating existing taskTracker object, we will delete the previous entry and add the updated taskTracker Object. Not the efficient way but easier way of updating an object.
		deleteTaskById(taskTracker.getId());
		tasks.add(taskTracker);
		
	}
	
}
