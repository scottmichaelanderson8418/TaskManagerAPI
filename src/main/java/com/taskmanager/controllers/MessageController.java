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
import org.springframework.security.core.Authentication;
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

import com.taskmanager.api.application.dto.PrintDrawingDto;
import com.taskmanager.api.application.dto.PrintDrawingResponse;
import com.taskmanager.model.MyUser;
import com.taskmanager.repository.MyUserRepository;
import com.taskmanager.security.JwtService;
import com.taskmanager.security.LoginForm;
import com.taskmanager.service.MyUserDetailService;
import com.taskmanager.service.PrintDrawingService;

@RequestMapping(value = "/api")
@RestController
public class MessageController {

	// Initialize a logger for the class
	Logger logger = LoggerFactory.getLogger(MessageController.class.getName());

	// The AuthenticationManager will help us authenticate by username and password

	// Autowire necessary dependencies
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private MyUserDetailService myUserDetailService;

	// scott
	@Autowired
	private MyUserRepository myUserRepository;

	@Autowired
	private PrintDrawingService printDrawingService;

	public MessageController(PrintDrawingService printDrawingService) {
		this.printDrawingService = printDrawingService;
	}

	@PostMapping("/authenticate")
	public String authenticateAndGetToken(@RequestBody LoginForm loginForm) {

		logger.trace("Entered......authenticateAndGetToken() ");
		logger.debug("Entered......authenticateAndGetToken() ");

		// the authenticationManager instance will call the "authenticate()"
		// method and verify the username and password
		// We will get an Authentication result object
		Authentication authentication = authenticationManager
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

	@PostMapping("/print/create")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<PrintDrawingDto> createPrint(@RequestBody PrintDrawingDto printDrawingDto) {
		System.out.println("/print/create"); // Log the creation request
		return new ResponseEntity<>(printDrawingService.createPrint(printDrawingDto), HttpStatus.CREATED);
	}

	@DeleteMapping("/print/delete/{id}")
	public ResponseEntity<String> deletePrintById(@PathVariable("id") int id) {
		printDrawingService.deleteByPrintId(id);
		return new ResponseEntity<>("Successfully deleted print drawing id = " + id, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) throws NotFoundException {
		myUserRepository.deleteById(id);
		return new ResponseEntity<>("User found and deleted", HttpStatus.OK);
	}

	@GetMapping("/pagination/{pageNo}/{pageSize}")
	public PrintDrawingResponse findByDiameterWithPaginationAndSorting(@PathVariable("pageNo") int pageNo,
			@PathVariable("pageSize") int pageSize, @RequestParam(value = "sortField", required = false) String field,
			@RequestParam(value = "diameterMinValue", required = false) Float diameterMinValue,
			@RequestParam(value = "diameterMaxValue", required = false) Float diameterMaxValue,
			@RequestParam(value = "faceLengthMinValue", required = false) Float faceLengthMinValue,
			@RequestParam(value = "faceLengthMaxValue", required = false) Float faceLengthMaxValue) {

		// Set default values if parameters are not provided
		if (field == null) {
			field = "diameterLow";
		}
		if (diameterMinValue == null) {
			diameterMinValue = 0.0f;
		}
		if (diameterMaxValue == null) {
			diameterMaxValue = 100.0f;
		}
		if (faceLengthMinValue == null) {
			faceLengthMinValue = 0.0f;
		}
		if (faceLengthMaxValue == null) {
			faceLengthMaxValue = 3000.0f;
		}

		// Retrieve print drawings with pagination and sorting
		return printDrawingService.findDiameterWithPaginationAndSorting(pageNo, pageSize, field, diameterMinValue,
				diameterMaxValue, faceLengthMinValue, faceLengthMaxValue);
	}

	@GetMapping("/print")
	public ResponseEntity<PrintDrawingResponse> getAllPrints(
			@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
		return new ResponseEntity<>(printDrawingService.getAllPrints(pageNo, pageSize), HttpStatus.OK);
	}

	@GetMapping("/admin/getallusers")
	public ResponseEntity<List<MyUser>> getAllUsers() {

		List<MyUser> users = myUserRepository.findAll();

		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@GetMapping("/print/{id}")
	public ResponseEntity<PrintDrawingDto> getPrintDetail(@PathVariable("id") int id) {
		return new ResponseEntity<>(printDrawingService.getPrintById(id), HttpStatus.OK);
	}

	@GetMapping("/printDrawings/findAll/{searchField}")
	public List<PrintDrawingDto> getProductsWithSort(@PathVariable("searchField") String field) {
		List<PrintDrawingDto> drawings = printDrawingService.findAllProductsWithSorting(field);
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
	public ResponseEntity<PrintDrawingDto> updatePrintDetail(@RequestBody PrintDrawingDto printDrawingUpdate,
			@PathVariable("id") int id) {

		PrintDrawingDto response = printDrawingService.updatePrint(printDrawingUpdate, id);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
