package br.com.uboard.core.service;

import br.com.uboard.client.GitlabClientController;
import br.com.uboard.core.model.operations.ReceiveMergeRequestEventForm;
import br.com.uboard.core.model.transport.IssueDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

/**
 * Service responsible for collecting and storing activities that will make up the release notes for
 * the next service Tag.
 *
 * @author Ronyeri Marinho
 * @since 1.0
 */

@Service
public class CreateReleaseNoteItemService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateReleaseNoteItemService.class);

    private final GitlabClientController gitlabClientController;
    private final String token;

    public CreateReleaseNoteItemService(GitlabClientController gitlabClientController,
                                        @Value("${external.gitlab.api.token}") String token) {
        this.gitlabClientController = gitlabClientController;
        this.token = token;
    }

    public void create(ReceiveMergeRequestEventForm payload) {
        Set<String> ids = payload.objectAttributes().getIssueIdentifiersFromDescription();
        LOGGER.debug("There are {} issues to release notes building...", ids.size());

        String tokenAsBearer = String.format("Bearer %s", this.token);
        for (String id : ids) {
            ResponseEntity<IssueDTO> response = this.gitlabClientController.getSingleProjectIssue(
                    tokenAsBearer, payload.project().id().toString(), id
            );
            if (response.getStatusCode().is2xxSuccessful() && !Objects.isNull(response.getBody())) {
                IssueDTO issueDTO = response.getBody();
                LOGGER.info(issueDTO.toString());
            }
        }
    }
}
