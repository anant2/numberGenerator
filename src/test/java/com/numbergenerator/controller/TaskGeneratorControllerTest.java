package com.numbergenerator.controller;


import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.numbergenerator.beans.TaskRequest;
import com.numbergenerator.beans.TaskResponse;
import com.numbergenerator.service.TaskService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskGeneratorControllerTest {

	@Autowired
	TaskGeneratorController taskGeneratorController;
	
	@Mock
	TaskService taskService;
	TaskRequest request;
	TaskResponse taskResponse;
	ResponseEntity<TaskResponse> response;
	
	@Before
	public void setUp() {
		request = new TaskRequest();
		request.setGoal("20");
		request.setStep("5");
		taskResponse = new TaskResponse();
		taskResponse.setTask("agadksd-dnsdnkd-sdck-askjs");
		
	}
	@Test
	public void testGenerateTask() throws IOException {
		Mockito.when(taskService.generateTask(ArgumentMatchers.any())).thenReturn(response);
		ResponseEntity<TaskResponse> response= taskGeneratorController.generateTask(request);
		System.out.println(response.getBody().getTask());
	}
}
