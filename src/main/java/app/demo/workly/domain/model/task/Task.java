package app.demo.workly.domain.model.task;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Task {
    Integer id;
    Integer projectId;
    String title;
    String description;
    String status;
}
