package br.com.uboard.core.model.transport;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IssueDTO(
        Long id,

        Long iid,

        @JsonProperty("project_id")
        Long projectId,

        String title
) {
}
