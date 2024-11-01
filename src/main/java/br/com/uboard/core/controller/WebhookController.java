package br.com.uboard.core.controller;

import br.com.uboard.core.model.operations.ReceiveMergeRequestEventForm;
import br.com.uboard.core.model.operations.ReceiveTagEventForm;
import br.com.uboard.core.service.CreateIssueService;
import br.com.uboard.core.service.CreateTagReleaseNotesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for operations related to Gitlab Webhooks operations.
 *
 * @author Ronyeri Marinho
 * @since 1.0
 */

@RestController
@RequestMapping("/webhooks")
@Tag(
        name = "Webhooks Controller",
        description = "Controller for managing all Gitlab webhook operations."
)
public class WebhookController {

    private final CreateIssueService createIssueService;
    private final CreateTagReleaseNotesService createTagReleaseNotesService;

    public WebhookController(CreateIssueService createIssueService,
                             CreateTagReleaseNotesService createTagReleaseNotesService) {
        this.createIssueService = createIssueService;
        this.createTagReleaseNotesService = createTagReleaseNotesService;
    }

    @Operation(summary = "Listener for merge request operations, responsible for storing the issues that will make up the release notes.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "The merge request has been received and the issue has been persisted if it does not already exist."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "One or more fields in the request body are invalid.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    }
            ),
    })
    @PostMapping("/merge-request")
    public ResponseEntity<Void> receiveMergeRequestEvent(@RequestBody @Valid ReceiveMergeRequestEventForm payload) {
        this.createIssueService.create(payload);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Listener for tag creation operations, responsible for retrieving stored issues and creating release notes for the tag.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "The tag has been received and release notes have been created."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "One or more fields in the request body are invalid.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    }
            ),
    })
    @PostMapping("/tag")
    public ResponseEntity<Void> receiveTagEvent(@RequestBody @Valid ReceiveTagEventForm payload) {
        this.createTagReleaseNotesService.create(payload);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
