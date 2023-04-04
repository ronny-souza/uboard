package br.com.uboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.uboard.model.Project;
import br.com.uboard.model.User;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

	public List<Project> findByUser(User user);
}
