package br.com.uboard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.uboard.model.Grouping;
import br.com.uboard.model.Milestone;
import br.com.uboard.model.Project;
import br.com.uboard.model.User;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, Long> {

	public List<Milestone> findByUser(User user);

	public List<Milestone> findByUserAndGroup(User user, Grouping grouping);
	
	public List<Milestone> findByUserAndProject(User user, Project project);

	public Optional<Milestone> findByGitlabIdentifier(Long gitlabIdentifier);
}
