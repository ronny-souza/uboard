package br.com.uboard.services;

import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.uboard.exceptions.UserNotFoundException;
import br.com.uboard.model.User;
import br.com.uboard.model.transport.UserDTO;
import br.com.uboard.repository.UserRepository;

@Service
public class UserSessionService {

	private UserRepository userRepository;

	public UserSessionService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public UserDTO getUserInSession() throws UserNotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String username = authentication.getName();

			Optional<User> optionalUser = this.userRepository.findByUsername(username);
			if (optionalUser.isPresent()) {
				return new UserDTO(optionalUser.get());
			}

			throw new UserNotFoundException("User is not found");
		}

		throw new UserNotFoundException("User is not authenticated");
	}

}
