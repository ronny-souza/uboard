package br.com.uboard.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.uboard.model.transport.CredentialsDTO;
import br.com.uboard.model.transport.UserDTO;
import br.com.uboard.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/sync")
	public ResponseEntity<UserDTO> synchronizeUser(@RequestBody CredentialsDTO credentialsDTO) {
		try {
			UserDTO response = this.userService.synchronizeUser(credentialsDTO);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}
