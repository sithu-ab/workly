package app.demo.workly.domain.repository;

import app.demo.workly.domain.model.project.Project;
import java.util.List;

public interface ProjectRepo {
    List<Project> getProjects();
}
