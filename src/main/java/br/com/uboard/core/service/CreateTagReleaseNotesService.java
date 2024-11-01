package br.com.uboard.core.service;

import br.com.uboard.core.model.Issue;
import br.com.uboard.core.model.operations.CreateGitlabTagReleaseForm;
import br.com.uboard.core.model.operations.ReceiveTagEventForm;
import br.com.uboard.core.repository.IssueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CreateTagReleaseNotesService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateTagReleaseNotesService.class);

    private final IssueRepository issueRepository;
    private final GitlabService gitlabService;

    public CreateTagReleaseNotesService(IssueRepository issueRepository, GitlabService gitlabService) {
        this.issueRepository = issueRepository;
        this.gitlabService = gitlabService;
    }

    @Transactional
    public void create(ReceiveTagEventForm payload) {
        String tagName = payload.extractTagName();
        Long projectId = payload.project().id();

        LOGGER.info("Starting to update the {} tag with the release notes", tagName);
        List<Issue> issues = this.issueRepository.findByProjectId(projectId);
        String releaseNotes = this.generateReleaseNotes(issues);
        CreateGitlabTagReleaseForm gitlabTagReleaseForm = new CreateGitlabTagReleaseForm(
                tagName,
                tagName,
                releaseNotes
        );

        this.gitlabService.createTagRelease(projectId.toString(), gitlabTagReleaseForm);
        this.issueRepository.deleteAllById(issues.stream().map(Issue::getId).toList());
    }

    private String generateReleaseNotes(List<Issue> issues) {
        StringBuilder releaseNotesAsBuilder = new StringBuilder("**FIXES/FEATURES:**\r\n\r\n");
        for (Issue issue : issues) {
            releaseNotesAsBuilder
                    .append("* #")
                    .append(issue.getIssueId())
                    .append(" - ")
                    .append(issue.getTitle())
                    .append(";\r\n");
        }

        return releaseNotesAsBuilder.toString();
    }
}
