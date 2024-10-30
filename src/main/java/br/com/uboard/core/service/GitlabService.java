package br.com.uboard.core.service;

import br.com.uboard.client.GitlabClientController;
import br.com.uboard.core.model.operations.CreateGitlabTagReleaseForm;
import br.com.uboard.core.model.transport.IssueDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GitlabService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitlabService.class);

    private final GitlabClientController gitlabClientController;
    private final String token;

    public GitlabService(GitlabClientController gitlabClientController,
                         @Value("${external.gitlab.api.token}") String token) {
        this.gitlabClientController = gitlabClientController;
        this.token = token;
    }

    public IssueDTO getSingleProjectIssue(String projectId, String id) {
        LOGGER.info("Starting search for issue in project {} with id {}", projectId, id);
        String tokenAsBearer = this.getTokenAsBearer();
        ResponseEntity<IssueDTO> response = this.gitlabClientController.getSingleProjectIssue(
                tokenAsBearer, projectId, id
        );

        if (response.getStatusCode().is2xxSuccessful() && !Objects.isNull(response.getBody())) {
            return response.getBody();
        }

        return null;
    }

    public void createTagRelease(String projectId, CreateGitlabTagReleaseForm form) {
        LOGGER.info("Starting release creation for the tag {}", form.tagName());
        String tokenAsBearer = this.getTokenAsBearer();
        ResponseEntity<Void> response = this.gitlabClientController.createTagRelease(
                tokenAsBearer, projectId, form
        );

        LOGGER.info(
                "Creating release for tag {} returned status {}",
                form.tagName(),
                response.getStatusCode().value()
        );
    }

    private String getTokenAsBearer() {
        return String.format("Bearer %s", this.token);
    }
}
