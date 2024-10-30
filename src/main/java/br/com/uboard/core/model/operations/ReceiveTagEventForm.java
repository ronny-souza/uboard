package br.com.uboard.core.model.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Representation of the payload received from the webhook for Gitlab new tag event.
 *
 * @author Ronyeri Marinho
 * @since 1.0
 */

@Schema(description = "Representation of the payload received from the webhook for Gitlab new tag event")
public record ReceiveTagEventForm(
        @JsonProperty("event_name")
        @NotBlank
        @Schema(description = "Event held in tag", example = "tag_push")
        String event,

        @NotBlank
        @Schema(description = "Tag information", example = "refs/tags/1.0-ALFA01")
        String ref,

        @NotNull
        @Valid
        @Schema(description = "Attributes of the project targeted by the tag")
        GitlabEventProjectForm project
) {

    public String getTagName() {
        String tagName = this.ref();
        int lastSlashIndex = tagName.lastIndexOf("/");
        if (lastSlashIndex != -1 && lastSlashIndex < tagName.length() - 1) {
            return tagName.substring(lastSlashIndex + 1);
        }
        return tagName;
    }
}
