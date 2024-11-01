package br.com.uboard.core.model;

import br.com.uboard.core.model.transport.IssueDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long issueId;

    @Column(nullable = false)
    private Long projectIssueId;

    @Column(nullable = false)
    private Long projectId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;

    public Issue() {

    }

    public Issue(IssueDTO issueDTO) {
        this.issueId = issueDTO.iid();
        this.projectIssueId = issueDTO.id();
        this.projectId = issueDTO.projectId();
        this.title = issueDTO.title();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Long getProjectId() {
        return projectId;
    }

    public Long getProjectIssueId() {
        return projectIssueId;
    }

    public Long getIssueId() {
        return issueId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Issue issue = (Issue) o;
        return Objects.equals(id, issue.id) && Objects.equals(issueId, issue.issueId) && Objects.equals(projectIssueId, issue.projectIssueId) && Objects.equals(projectId, issue.projectId) && Objects.equals(title, issue.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, issueId, projectIssueId, projectId, title);
    }
}
