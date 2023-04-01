package br.com.uboard.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.uboard.exceptions.SynchronizeUserException;
import br.com.uboard.exceptions.UserAlreadyExistsException;
import br.com.uboard.model.transport.CredentialsDTO;
import br.com.uboard.model.transport.UserDTO;
import br.com.uboard.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<UserDTO> create(@RequestBody UserDTO userDTO) throws UserAlreadyExistsException {
		this.userService.create(userDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/sync")
	public ResponseEntity<UserDTO> synchronizeUser(@RequestBody CredentialsDTO credentialsDTO)
			throws SynchronizeUserException {
		UserDTO response = this.userService.synchronizeUser(credentialsDTO);
		return ResponseEntity.ok(response);
	}
}
