package com.numbergenerator.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.numbergenerator.beans.StatusResponse;
import com.numbergenerator.beans.TaskRequest;
import com.numbergenerator.beans.TaskResponse;
import com.numbergenerator.service.TaskService;

@RestController
public class TaskGeneratorController {

	private final Logger logger = LoggerFactory.getLogger(TaskGeneratorController.class);
	
	@Autowired
	TaskService taskService;
	
	@PostMapping(value="api/generate")
	public ResponseEntity<TaskResponse> generateTask(@RequestBody TaskRequest request) throws IOException{
		logger.info("Request to generate a task using goal: "+request.getGoal()+ " and step "+ request.getStep());
		return taskService.generateTask(request);
	}
	
	@GetMapping(value="api/tasks/{UUID}/status")
	public ResponseEntity<StatusResponse> getTaskStatus(@PathVariable("UUID") String UUID) throws IOException{
		logger.info("Request to get status of a task"+ UUID);
		return taskService.getTaskStatus(UUID);
	}
	
	@GetMapping(value="api/tasks/{UUID}")
	public ResponseEntity<StatusResponse> getCompletedTasks(@RequestParam(value="action") String action,@PathVariable("UUID") String UUID) throws IOException{
		logger.info("Request to get completed task from file:");
		return taskService.getCompletedTask(UUID,action);
	}
	
}
