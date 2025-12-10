package app.demo.workly.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskRequest {
    @JsonProperty("title")
    private String title; // nullable -> keep existing when null

    @JsonProperty("description")
    private String description; // nullable -> keep existing when null

    @Pattern(regexp = "(?i)^$|TODO|PROGRESS|REVIEW|DONE", message = "status must be one of 'TODO', 'PROGRESS', 'REVIEW', or 'DONE'")
    @JsonProperty("status")
    private String status; // nullable -> keep existing when null
}
