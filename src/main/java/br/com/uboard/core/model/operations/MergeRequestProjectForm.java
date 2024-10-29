package br.com.uboard.core.model.operations;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Representation of the project payload received from the webhook for Gitlab merge request events.
 *
 * @author Ronyeri Marinho
 * @since 1.0
 */

@Schema(description = "Attributes of the project targeted by the merge request")
public record MergeRequestProjectForm(
        @NotNull
        @Schema(description = "Project identifier", example = "1234")
        Long id
) {
}
