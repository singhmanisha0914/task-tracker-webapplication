package com.manisha.springboot.webapp.withJPA;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manisha.springboot.webapp.taskTracker.TaskTracker;

@Repository
public interface TaskTrackerRepository extends JpaRepository<TaskTracker, Integer>{
	
	List<TaskTracker> findByUsername(String username);

}
