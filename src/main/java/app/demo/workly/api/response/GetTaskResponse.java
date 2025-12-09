package app.demo.workly.api.response;

import app.demo.workly.domain.model.task.Task;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.jspecify.annotations.NonNull;

@Builder
public class GetTaskResponse {
    @JsonProperty("id")
    @NonNull
    private Integer id;

    @JsonProperty("projectId")
    @NonNull
    private Integer projectId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private String status;

    public static GetTaskResponse from(Task task) {
        return GetTaskResponse.builder()
                .id(task.getId())
                .projectId(task.getProjectId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .build();
    }
}
