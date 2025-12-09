package app.demo.workly.domain.repository;

import app.demo.workly.domain.model.task.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepo {
    List<Task> findAll();
    List<Task> findAllByProject(int projectId);
    Optional<Task> findById(int id);
    Task create(int projectId, String name, String description, String status);
    Optional<Task> update(int id, String name, String description, String status);
    boolean softDelete(int id);
}
