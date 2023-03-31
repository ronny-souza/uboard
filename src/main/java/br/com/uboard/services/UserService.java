package br.com.uboard.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.uboard.exceptions.SynchronizeUserException;
import br.com.uboard.exceptions.UserAlreadyExistsException;
import br.com.uboard.model.User;
import br.com.uboard.model.transport.CredentialsDTO;
import br.com.uboard.model.transport.UserDTO;
import br.com.uboard.repository.UserRepository;

@Service
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Value("${gitlab-provider}")
	private String address;

	private WebClientRest webClient;

	private UserRepository userRepository;

	private PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.webClient = new WebClientRest();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public UserDTO synchronizeUser(CredentialsDTO credentialsDTO) throws SynchronizeUserException {
		try {
			String url = new StringBuilder().append(this.address).append("/user/sync").toString();
			ResponseEntity<UserDTO> response = this.webClient.post(url, UserDTO.class, credentialsDTO);

			return response.getBody();
		} catch (Exception e) {
			throw new SynchronizeUserException(e.getMessage(), e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void create(UserDTO userDTO) throws UserAlreadyExistsException {
		this.userAlreadyExists(userDTO.getEmail());

		User user = new User(userDTO);
		user.setPassword(this.passwordEncoder.encode(userDTO.getPassword()));
		this.userRepository.save(user);
		LOGGER.debug("User registered successfully on database...");

//		TODO Send confirmation account by e-mail service
	}

	private void userAlreadyExists(String email) throws UserAlreadyExistsException {
		Optional<User> optionalUser = this.userRepository.findByEmail(email);

		if (optionalUser.isPresent()) {
			throw new UserAlreadyExistsException("User with e-mail: " + email + " already exists");
		}
	}

}
