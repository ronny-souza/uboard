package br.com.uboard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.uboard.model.AccountConfirmation;

@Repository
public interface AccountConfirmationRepository extends JpaRepository<AccountConfirmation, Long> {

	public Optional<AccountConfirmation> findByCodeAndUserEmail(String code, String email);
}
