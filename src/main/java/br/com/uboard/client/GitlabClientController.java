package br.com.uboard.client;

import br.com.uboard.core.model.operations.CreateGitlabTagReleaseForm;
import br.com.uboard.core.model.transport.IssueDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Client for HTTP requests to the Gitlab API
 *
 * @author Ronyeri Marinho
 * @since 1.0
 */

@FeignClient(url = "${external.gitlab.api.url}", name = "gitlab-client-controller")
public interface GitlabClientController {

    @GetMapping("/projects/{projectId}/issues/{id}")
    ResponseEntity<IssueDTO> getSingleProjectIssue(@RequestHeader("Authorization") String authorization,
                                                   @PathVariable("projectId") String projectId,
                                                   @PathVariable("id") String id);

    @PostMapping("/projects/{projectId}/releases")
    ResponseEntity<Void> createTagRelease(@RequestHeader("Authorization") String authorization,
                                          @PathVariable("projectId") String projectId,
                                          @RequestBody CreateGitlabTagReleaseForm body);
}
