package br.com.uboard.core.repository;

import br.com.uboard.core.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByProjectId(Long projectId);

    boolean existsByIssueIdAndProjectId(Long issueId, Long projectId);
}
