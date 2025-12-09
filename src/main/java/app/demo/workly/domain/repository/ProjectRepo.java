package app.demo.workly.domain.repository;

import app.demo.workly.domain.model.project.Project;
import java.util.List;
import java.util.Optional;

public interface ProjectRepo {
    List<Project> findAll();
    Optional<Project> findById(int id);
}
