package br.com.uboard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.uboard.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	public Optional<User> findByUsername(String username);

	public Optional<User> findByEmail(String username);

	public Optional<User> findByUboardIdentifierAndEnabledTrue(String uboardIdentifier);

	public Optional<User> findByGitlabIdentifierAndEnabledTrue(Long gitlabIdentifier);

	public List<User> findByEnabledTrue();
}
