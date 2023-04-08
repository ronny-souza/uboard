package br.com.uboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.uboard.model.Grouping;
import br.com.uboard.model.User;

@Repository
public interface GroupingRepository extends JpaRepository<Grouping, Long> {

	public List<Grouping> findByUser(User user);
}
