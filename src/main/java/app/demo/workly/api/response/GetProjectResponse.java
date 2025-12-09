package app.demo.workly.api.response;

import app.demo.workly.domain.model.project.Project;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.jspecify.annotations.NonNull;

@Builder
public class GetProjectResponse {
    @JsonProperty("id")
    @NonNull
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    public static GetProjectResponse from(Project project) {
        return GetProjectResponse.builder()
            .id(project.getId())
            .name(project.getName())
            .description(project.getDescription())
            .build()
            ;
    }
}
