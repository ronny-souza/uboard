package br.com.uboard.core.controller;

import br.com.uboard.core.model.operations.ReceiveMergeRequestEventForm;
import br.com.uboard.core.service.CreateReleaseNoteItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    private final CreateReleaseNoteItemService createReleaseNoteItemService;

    public WebhookController(CreateReleaseNoteItemService createReleaseNoteItemService) {
        this.createReleaseNoteItemService = createReleaseNoteItemService;
    }

    @PostMapping("/merge-request")
    public ResponseEntity<Void> receiveMergeRequestsEvent(@RequestBody @Valid ReceiveMergeRequestEventForm payload) {
        this.createReleaseNoteItemService.create(payload);
        return ResponseEntity.noContent().build();
    }
}
