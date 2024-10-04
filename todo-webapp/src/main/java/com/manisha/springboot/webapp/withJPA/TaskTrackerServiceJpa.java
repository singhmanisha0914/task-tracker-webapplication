package com.manisha.springboot.webapp.withJPA;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import com.manisha.springboot.webapp.taskTracker.TaskTracker;

import jakarta.validation.Valid;

@Service
public class TaskTrackerServiceJpa {
	
	private TaskTrackerRepository repo; 
	
	//Autowiring repo using constructor injection
	public TaskTrackerServiceJpa(TaskTrackerRepository repo) {
		super();
		this.repo = repo;
	}
	
	//Define a method to return the tasks corresponding to a username
	public List<TaskTracker> findByUsername(String username){
		return repo.findByUsername(username);
	}
	
	//Define a method to add a new task to the table
	public void addTask(TaskTracker newTask) {
//		int taskCount = (int) repo.count();
//		TaskTracker newTask = new TaskTracker(++taskCount, username, description,targetDate, done);
		repo.save(newTask);
	}
	
	//Define a method to delete a task by id from the table
	public void deleteTaskById(int id) {
		repo.deleteById(id);
	}
	
	//Define a method to find a task Bean by id from the table
	public TaskTracker findById(int id) {
		return repo.findById(id).get();
	}

	public void updateTask(@Valid TaskTracker taskTracker) {
		repo.save(taskTracker);
	}
	
}
