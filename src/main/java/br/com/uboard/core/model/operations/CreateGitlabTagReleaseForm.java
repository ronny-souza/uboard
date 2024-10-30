package br.com.uboard.core.model.operations;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateGitlabTagReleaseForm(
        String name,

        @JsonProperty("tag_name")
        String tagName,

        String description) {
}
