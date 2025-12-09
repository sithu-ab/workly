package app.demo.workly.service;

import app.demo.workly.domain.model.project.Project;
import app.demo.workly.domain.repository.ProjectRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepo projectRepo;

    @Transactional
    public List<Project> findAll() {
        return this.projectRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Project> findById(Integer id) {
        return this.projectRepo.findById(id);
    }
}
