package app.demo.workly.service;

import app.demo.workly.domain.model.task.Task;
import app.demo.workly.domain.repository.TaskRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepo taskRepo;

    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return taskRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Task> findAllByProject(int projectId) {
        return taskRepo.findAllByProject(projectId);
    }

    @Transactional(readOnly = true)
    public Optional<Task> findById(int id) {
        return taskRepo.findById(id);
    }

    @Transactional
    public Task create(int projectId, String name, String description, String status) {
        return taskRepo.create(projectId, name, description, status);
    }

    @Transactional
    public Optional<Task> update(int id, String name, String description, String status) {
        return taskRepo.update(id, name, description, status);
    }

    @Transactional
    public boolean softDelete(int id) {
        return taskRepo.softDelete(id);
    }
}
