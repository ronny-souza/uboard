package br.com.uboard.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.uboard.RandomUtils;
import br.com.uboard.exceptions.SynchronizeUserException;
import br.com.uboard.exceptions.UserAlreadyExistsException;
import br.com.uboard.model.User;
import br.com.uboard.model.enums.MailTypeEnum;
import br.com.uboard.model.transport.CredentialsDTO;
import br.com.uboard.model.transport.MailDTO;
import br.com.uboard.model.transport.UserDTO;
import br.com.uboard.rabbitmq.MessageService;
import br.com.uboard.rabbitmq.RabbitQueues;
import br.com.uboard.repository.UserRepository;

@Service
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Value("${gitlab-provider}")
	private String address;

	private WebClientRest webClient;

	private UserRepository userRepository;

	private PasswordEncoder passwordEncoder;

	private MessageService messageService;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MessageService messageService) {
		this.webClient = new WebClientRest();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.messageService = messageService;
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
		LOGGER.debug("User registered successfully on database. Sending account confirmation e-mail...");

		this.sendAccountConfirmationCodeByEmail(userDTO);
	}

	private void sendAccountConfirmationCodeByEmail(UserDTO userDTO) {
		MailDTO mailDTO = new MailDTO();
		mailDTO.setTo(userDTO.getEmail());
		mailDTO.setMailType(MailTypeEnum.CONFIRM_ACCOUNT);
		mailDTO.setSubject("Confirmação de Conta");
		Map<String, Object> properties = new HashMap<>();
		properties.put("username", userDTO.getName());
		properties.put("confirmationCode", RandomUtils.getInstance().generateRandomCode());
		mailDTO.setProperties(properties);

		this.messageService.enqueue(RabbitQueues.SEND_MAIL, mailDTO);

	}

	private void userAlreadyExists(String email) throws UserAlreadyExistsException {
		Optional<User> optionalUser = this.userRepository.findByEmail(email);

		if (optionalUser.isPresent()) {
			throw new UserAlreadyExistsException("User with e-mail: " + email + " already exists");
		}
	}

}
