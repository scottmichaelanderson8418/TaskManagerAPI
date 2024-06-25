package com.taskmanager.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.dto.TaskDto;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.model.MyUser;
import com.taskmanager.repository.MyUserRepository;
import com.taskmanager.security.JwtService;
import com.taskmanager.security.LoginForm;
import com.taskmanager.service.MyUserDetailService;
import com.taskmanager.service.TaskService;

@RequestMapping(value = "/api/taskmanager")
@RestController
public class TaskController {

	// Autowire necessary dependencies
	@Autowired
	private AuthenticationManager authenticationManager;

	// The AuthenticationManager will help us authenticate by username and password

	@Autowired
	private JwtService jwtService;

	// Initialize a logger for the class
	Logger logger = LoggerFactory.getLogger(TaskController.class.getName());

	@Autowired
	private MyUserDetailService myUserDetailService;

	@Autowired
	private MyUserRepository myUserRepository;

	@Autowired
	private TaskService taskService;

	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@PostMapping("/authenticate")
	public String authenticateAndGetToken(@RequestBody LoginForm loginForm) {

		logger.trace("Entered......authenticateAndGetToken() ");
		logger.debug("Entered......authenticateAndGetToken() ");

		// the authenticationManager instance will call the "authenticate()"
		// method and verify the username and password
		// We will get an Authentication result object
		org.springframework.security.core.Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginForm.username(), loginForm.password()));
		// If credentials are authenticated then generate new token
		if (authentication.isAuthenticated()) {

			// The generateToken method requires the UserDetail Class
			// Generate JWT token upon successful authentication

			logger.trace("Exited..... authenticateAndGetToken() ");
			logger.debug("Exited.....  authenticateAndGetToken()");
			return jwtService.generateToken(myUserDetailService.loadUserByUsername(loginForm.username()));
		} else {

			logger.error("Error occurred during authentication process");
			// Throw exception for invalid credentials
			throw new UsernameNotFoundException("Invalid credentials");
		}

	}

	@PostMapping("/task/create")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<TaskDto> createPrint(@RequestBody TaskDto taskDto) {
		System.out.println("/task/create"); // Log the creation request
		return new ResponseEntity<>(taskService.createTask(taskDto), HttpStatus.CREATED);
	}

	@DeleteMapping("/task/delete/{id}")
	public ResponseEntity<String> deletePrintById(@PathVariable("id") int id) {
		taskService.deleteByTaskId(id);
		return new ResponseEntity<>("Successfully deleted print drawing id = " + id, HttpStatus.OK);
	}

	@DeleteMapping("/user/delete/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) throws NotFoundException {
		myUserRepository.deleteById(id);
		return new ResponseEntity<>("User found and deleted", HttpStatus.OK);
	}

	@GetMapping("/getallTasks")
	public ResponseEntity<TaskResponse> getAllPrints(
			@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
		return new ResponseEntity<>(taskService.getAllTasks(pageNo, pageSize), HttpStatus.OK);
	}

	@GetMapping("/admin/getallusers")
	public ResponseEntity<List<MyUser>> getAllUsers() {

		List<MyUser> users = myUserRepository.findAll();

		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@GetMapping("/print/{id}")
	public ResponseEntity<TaskDto> getPrintDetail(@PathVariable("id") int id) {
		return new ResponseEntity<>(taskService.getTaskById(id), HttpStatus.OK);
	}

	@GetMapping("/tasks/findAll/{searchField}")
	public List<TaskDto> getProductsWithSort(@PathVariable("searchField") String field) {
		List<TaskDto> drawings = taskService.findAllTaskWithSorting(field);
		return drawings;
	}

	@GetMapping("/admin/home")
	public String handleAdminHome() {
		return "Welcome to ADMIN home!";
	}

	// Endpoint: User accessible home page
	@GetMapping("/user/home")
	public String handleUserHome() {
		return "Welcome to the user home page :)";
	}

	@GetMapping("/home")
	public String handleWelcome() {
		return "Welcome to the homepage";
	}

	@PutMapping("/print/update/{id}")
	public ResponseEntity<TaskDto> updatePrintDetail(@RequestBody TaskDto taskUpdate, @PathVariable("id") int id) {

		TaskDto response = taskService.updateTask(taskUpdate, id);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
