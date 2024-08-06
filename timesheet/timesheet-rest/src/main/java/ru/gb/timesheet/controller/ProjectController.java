package ru.gb.timesheet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.timesheet.model.Project;
import ru.gb.timesheet.model.ProjectEmploy;
import ru.gb.timesheet.model.Timesheet;
import ru.gb.timesheet.service.ProjectService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/projects")
@Tag(name = "projects", description = "API для работы с проектами")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @Operation(summary = "Get project", description = "Получить проект по его идентификатору")
    @GetMapping("/{id}")
    public ResponseEntity<Project> get(@PathVariable @Parameter(description = "Идентификатор проекта") Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @Operation(summary = "Get timesheets", description = "Получить все таймшиты по проекту")
    @GetMapping("/{id}/timesheets")
    public ResponseEntity<List<Timesheet>> getTimesheets(@PathVariable @Parameter(description = "Идентификатор проекта") Long id) {
        try {
            return ResponseEntity.ok(service.getTimesheets(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get employees", description = "Получить список сотрудников")
    @GetMapping("/employees")
    public ResponseEntity<List<ProjectEmploy>> listProjectEmploy() {
        try {
            return ResponseEntity.ok(service.listProjectEmploy());
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping
    @Operation(summary = "Get projects", description = "Получить список проектов")
    public ResponseEntity<List<Project>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Create project", description = "Создать проект")
    @PostMapping
    public ResponseEntity<Project> create(@RequestBody @Parameter(description = "Объект типа Project") Project project) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(project));
    }

    @Operation(summary = "Delete project", description = "Удалить проект по его идентификатору")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Parameter(description = "Идентификатор проекта") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}

