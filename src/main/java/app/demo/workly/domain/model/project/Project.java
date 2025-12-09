package app.demo.workly.domain.model.project;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Project {
    Integer id;
    String name;
    String description;
}
