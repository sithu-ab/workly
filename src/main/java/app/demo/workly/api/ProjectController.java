package app.demo.workly.api;

import app.demo.workly.api.response.GetProjectResponse;
import app.demo.workly.api.response.GetTaskResponse;
import app.demo.workly.domain.model.project.Project;
import app.demo.workly.domain.model.task.Task;
import app.demo.workly.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);

    @GetMapping
    public ResponseEntity<@NonNull List<GetProjectResponse>> getProjects() {
        log.info("getProjects");

        List<Project> projects = projectService.findAll();

        return ResponseEntity.ok(projects.stream().map(GetProjectResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetProjectResponse> getProjectById(@PathVariable int id) {
        log.info("getProjectById id={}", id);

        Optional<Project> project = projectService.findById(id);

        return project
                .map(prj -> ResponseEntity.ok(GetProjectResponse.from(prj)))
                .orElse(ResponseEntity.notFound().build());
    }
}
