package br.com.uboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.uboard.model.Grouping;

@Repository
public interface GroupingRepository extends JpaRepository<Grouping, Long> {

}
