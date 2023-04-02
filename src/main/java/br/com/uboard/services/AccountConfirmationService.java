package br.com.uboard.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.uboard.exceptions.ConfirmationCodeExpiredException;
import br.com.uboard.exceptions.ConfirmationCodeNotFoundException;
import br.com.uboard.model.AccountConfirmation;
import br.com.uboard.model.transport.AccountConfirmationDTO;
import br.com.uboard.repository.AccountConfirmationRepository;

@Service
public class AccountConfirmationService {

	private AccountConfirmationRepository accountConfirmationRepository;

	public AccountConfirmationService(AccountConfirmationRepository accountConfirmationRepository) {
		this.accountConfirmationRepository = accountConfirmationRepository;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public AccountConfirmation validate(AccountConfirmationDTO accountConfirmationDTO)
			throws ConfirmationCodeNotFoundException, ConfirmationCodeExpiredException {
		Optional<AccountConfirmation> optionalConfirmation = this.accountConfirmationRepository
				.findByCodeAndUserEmail(accountConfirmationDTO.getCode(), accountConfirmationDTO.getEmail());

		if (optionalConfirmation.isEmpty()) {
			throw new ConfirmationCodeNotFoundException("The code provided is not found for this email");
		}

		AccountConfirmation accountConfirmation = optionalConfirmation.get();
		if (LocalDateTime.now().isAfter(accountConfirmation.getExpirationDate())) {
			this.accountConfirmationRepository.delete(accountConfirmation);
			throw new ConfirmationCodeExpiredException("The code provided is expired");
		}

		return accountConfirmation;
	}
}
