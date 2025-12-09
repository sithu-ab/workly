package app.demo.workly.service;

import app.demo.workly.domain.model.project.Project;
import app.demo.workly.domain.repository.ProjectRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepo projectRepo;

    @Transactional
    public List<Project> getProjects() {
        return this.projectRepo.getProjects();
    }
}
