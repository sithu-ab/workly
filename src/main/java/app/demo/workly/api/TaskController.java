package app.demo.workly.api;

import app.demo.workly.api.request.CreateTaskRequest;
import app.demo.workly.api.request.UpdateTaskRequest;
import app.demo.workly.api.response.GetTaskResponse;
import app.demo.workly.domain.model.task.Task;
import app.demo.workly.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    /**
     * Retrieves all tasks from the system.
     *
     * @return a ResponseEntity containing a non-null list of task responses
     */
    @GetMapping("/tasks")
    public ResponseEntity<@NonNull List<GetTaskResponse>> getAllTasks() {
        log.info("getAllTasks");
        List<Task> tasks = taskService.findAll();

        return ResponseEntity.ok(tasks.stream().map(GetTaskResponse::from).toList());
    }

    /**
     * Retrieves all tasks associated with a specific project.
     *
     * @param projectId the ID of the project for which tasks are to be retrieved
     * @return a ResponseEntity containing a list of task responses associated with the specified project
     */
    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<@NonNull List<GetTaskResponse>> getTasksByProject(@PathVariable int projectId) {
        log.info("getTasksByProject projectId={}", projectId);
        List<Task> tasks = taskService.findAllByProject(projectId);

        return ResponseEntity.ok(tasks.stream().map(GetTaskResponse::from).toList());
    }

    /**
     * Creates a new task associated with a specific project.
     *
     * @param projectId the ID of the project to which the task belongs
     * @param request the object containing the title, description, and status of the task to be created
     * @return a ResponseEntity containing the details of the created task
     */
    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<GetTaskResponse> createTask(@PathVariable int projectId, @RequestBody CreateTaskRequest request) {
        log.info("createTask projectId={} title={}", projectId, request.getTitle());

        Task created = taskService.create(
                projectId,
                request.getTitle(),
                request.getDescription(),
                request.getStatus() != null ? request.getStatus() : "TODO"
        );
        GetTaskResponse body = GetTaskResponse.from(created);

        return ResponseEntity.created(URI.create("/tasks/" + created.getId())).body(body);
    }

    /**
     * Retrieves a specific task based on its ID.
     *
     * @param id the unique identifier of the task to be retrieved
     * @return a ResponseEntity containing the task details if found, or a ResponseEntity with no content if not found
     */
    @GetMapping("/tasks/{id}")
    public ResponseEntity<GetTaskResponse> getTask(@PathVariable int id) {
        log.info("getTask id={}", id);

        Optional<Task> task = taskService.findById(id);

        return task
                .map(entity -> ResponseEntity.ok(GetTaskResponse.from(entity)))
                .orElseGet(() -> ResponseEntity.notFound().build()); // same as orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing task with new information provided in the update request.
     *
     * @param id the unique identifier of the task to be updated
     * @param request the object containing the updated title, description, and status of the task
     * @return a ResponseEntity containing the updated task details if the task was successfully updated,
     *         or a ResponseEntity with no content if the task was not found
     */
    @PatchMapping("/tasks/{id}")
    public ResponseEntity<GetTaskResponse> updateTask(@PathVariable int id, @RequestBody UpdateTaskRequest request) {
        log.info("updateTask id={}", id);

        Optional<Task> updated = taskService.update(
                id,
                request.getTitle(),
                request.getDescription(),
                request.getStatus()
        );

        return updated
                .map(task -> ResponseEntity.ok(GetTaskResponse.from(task)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes a task identified by its unique ID. The task is soft deleted,
     * meaning it is marked as deleted but not permanently removed from the system.
     *
     * @param id the unique identifier of the task to be deleted
     * @return a ResponseEntity with no content if the deletion is successful,
     *         or a ResponseEntity with a not-found status if the task does not exist
     */
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable int id) {
        log.info("deleteTask id={}", id);
        boolean deleted = taskService.softDelete(id);
        if (!deleted) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }
}
