package com.numbergenerator.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.numbergenerator.beans.StatusResponse;
import com.numbergenerator.beans.TaskRequest;
import com.numbergenerator.beans.TaskResponse;

@Service
public class TaskService {

	private final Logger logger = LoggerFactory.getLogger(TaskService.class);
	
	public ResponseEntity<TaskResponse> generateTask(TaskRequest request) {
		logger.info("Inside generateTask Service");
		TaskResponse response = new TaskResponse();
		//initialize UUID
		UUID uuid = UUID.randomUUID();
		List<String> output = new ArrayList<>();
		String fileName = "C:/generate/"+uuid+"_output.txt";
		try {
			Path path = Paths.get(fileName);
			Integer goal = Integer.parseInt(request.getGoal());
			Integer step = Integer.parseInt(request.getStep());
			Integer temp = goal;
			output.add(temp.toString());
			while(goal > 0) {
				goal -= step;
				output.add(goal.toString());
			}
			String result = output.stream().map(String::toUpperCase).collect(Collectors.joining(","));
			Files.write(path, result.getBytes());
		}catch(Exception e) {
			logger.error("Error in executing generateTask Service",ResponseEntity.notFound());
		}
		logger.info("Task generated "+uuid.toString());
		response.setTask(uuid.toString());
		return ResponseEntity.accepted().body(response);
	}

	public ResponseEntity<StatusResponse> getTaskStatus(String uUID) throws IOException {
		StatusResponse response = new StatusResponse();
		logger.info("Inside getTaskStatus Service");
		Stream<Path> paths = Files.walk(Paths.get("C:/generate"));
		String result = null;
		try {
			result = paths.filter(Files::isRegularFile).map(path->{
				return path.getFileName().toString().contains(uUID)? "SUCCESS" : "IN_PROGRESS";
			}).collect(Collectors.joining());
			if(result != null && !result.isEmpty()) {
				result = result.contains("IN_PROGRESS") ? "IN_PROGRESS" : "SUCCESS";
			}else {
				result = "ERROR";
				response.setResult(result);
			}
		}catch(Exception e) {
			logger.error("Error in task");
			result = "ERROR";
			response.setResult(result);
		}
		response.setResult(result);
		return ResponseEntity.accepted().body(response);
	}

	public ResponseEntity<StatusResponse> getCompletedTask(String uUID, String action) throws IOException {
		StatusResponse response = new StatusResponse();
		logger.info("Inside getCompletedTasks Service");
		Stream<Path> paths = Files.walk(Paths.get("C:/generate"));
		String result = paths.filter(Files::isRegularFile).map(path->{
			String output = "";
			if(path.getFileName().toString().contains(uUID)) {
				try {
					output = Files.readAllLines(path).stream().map(String::toUpperCase).collect(Collectors.joining());
				}catch(IOException e) {
					logger.error("Error in reading from file"+e.getMessage());					
				}
			}
			return output;
		}).collect(Collectors.joining());
		logger.info("Output as read from file: "+result);
		response.setResult(result);
		return ResponseEntity.accepted().body(response);
	}

}
