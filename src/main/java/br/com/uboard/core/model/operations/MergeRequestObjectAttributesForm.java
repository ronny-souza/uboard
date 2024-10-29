package br.com.uboard.core.model.operations;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Representation of the merge request payload received from the webhook for Gitlab merge request events.
 *
 * @author Ronyeri Marinho
 * @since 1.0
 */

@Schema(description = "Merge request attributes")
public record MergeRequestObjectAttributesForm(
        @NotBlank
        @Schema(
                description = "Merge request description containing issue identifier reference",
                example = "#200"
        )
        String description
) {

    public Set<String> getIssueIdentifiersFromDescription() {
        Set<String> ids = new HashSet<>();
        if (StringUtils.isEmpty(this.description())) {
            return ids;
        }

        Pattern pattern = Pattern.compile("#\\d+");
        Matcher matcher = pattern.matcher(this.description());

        while (matcher.find()) {
            ids.add(matcher.group().replace("#", ""));
        }

        return ids;
    }
}
