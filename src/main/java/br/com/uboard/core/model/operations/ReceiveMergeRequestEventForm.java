package br.com.uboard.core.model.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Representation of the payload received from the webhook for Gitlab merge request events.
 *
 * @author Ronyeri Marinho
 * @since 1.0
 */

@Schema(description = "Representation of the payload received from the webhook for Gitlab merge request events")
public record ReceiveMergeRequestEventForm(
        @JsonProperty("object_attributes")
        @NotNull
        @Valid
        @Schema(description = "Merge request attributes")
        MergeRequestObjectAttributesForm objectAttributes,

        @NotNull
        @Valid
        @Schema(description = "Attributes of the project targeted by the merge request")
        GitlabEventProjectForm project
) {
}
