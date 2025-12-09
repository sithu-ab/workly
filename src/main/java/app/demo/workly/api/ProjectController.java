package app.demo.workly.api;

import app.demo.workly.api.response.GetProjectResponse;
import app.demo.workly.domain.model.project.Project;
import app.demo.workly.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);

    @GetMapping
    public ResponseEntity<@NonNull List<GetProjectResponse>> getProjects() {
        List<Project> projects = projectService.getProjects();
        log.info("getProjects");

        return ResponseEntity.ok(projects.stream().map(GetProjectResponse::from).toList());
    }
}
