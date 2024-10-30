package br.com.uboard.core.service;

import br.com.uboard.core.model.Issue;
import br.com.uboard.core.model.operations.ReceiveMergeRequestEventForm;
import br.com.uboard.core.model.transport.IssueDTO;
import br.com.uboard.core.repository.IssueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
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
public class CreateIssueService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateIssueService.class);

    private final IssueRepository issueRepository;
    private final GitlabService gitlabService;

    public CreateIssueService(IssueRepository issueRepository, GitlabService gitlabService) {
        this.issueRepository = issueRepository;
        this.gitlabService = gitlabService;
    }


    @Transactional
    public void create(ReceiveMergeRequestEventForm payload) {
        Set<String> ids = payload.objectAttributes().getIssueIdentifiersFromDescription();
        LOGGER.debug("There are {} issues to release notes building...", ids.size());

        Long projectId = payload.project().id();
        Set<Issue> issues = new HashSet<>();
        for (String id : ids) {
            IssueDTO issueDTO = this.gitlabService.getSingleProjectIssue(projectId.toString(), id);
            if (!Objects.isNull(issueDTO)) {
                issues.add(new Issue(issueDTO));
            }
        }
        this.issueRepository.saveAll(issues);
    }
}
