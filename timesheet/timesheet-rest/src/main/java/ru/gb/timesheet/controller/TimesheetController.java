package ru.gb.timesheet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.timesheet.model.Project;
import ru.gb.timesheet.model.Timesheet;
import ru.gb.timesheet.service.TimesheetService;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/timesheets")
@Tag(name = "timesheets", description = "API для работы с таймшит")
public class TimesheetController {

    // GET - получить - не содержит тела
    // POST - create
    // PUT - изменение
    // PATCH - изменение
    // DELETE - удаление

    // @GetMapping("/timesheets/{id}") // получить конкретную запись по идентификатору
    // @DeleteMapping("/timesheets/{id}") // удалить конкретную запись по идентификатору
    // @PutMapping("/timesheets/{id}") // обновить конкретную запись по идентификатору

    private final TimesheetService service;

    public TimesheetController(TimesheetService service) {
        this.service = service;
    }
    @Operation(summary = "Get timesheet", description = "Получить таймшит по его идентификатору")
    @GetMapping("/{id}") // получить все
    public ResponseEntity<Timesheet> get(@PathVariable @Parameter(description = "Идентификатор таймшита") Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // /timesheets
    // /timesheets?createdAtBefore=2024-07-09
    // /timesheets?createdAtAfter=2024-07-15
    // /timesheets?createdAtAfter=2024-07-15&createdAtBefore=2024-06-05
    @GetMapping
    @Operation(summary = "Get timesheets", description = "Получить список таймшитов")
    public ResponseEntity<List<Timesheet>> getAll(
            @RequestParam(required = false) @Parameter(description = "Объект LocalDate (необязательный параметр) для получения списка таймшитов созданных до указанной даты") LocalDate createdAtBefore,
            @RequestParam(required = false) @Parameter(description = "Объект LocalDate (необязательный параметр) для получения списка таймшитов созданных после указанной даты")LocalDate createdAtAfter
    ) {
        return ResponseEntity.ok(service.findAll(createdAtBefore, createdAtAfter));
    }

    // client -> [spring-server -> ... -> TimesheetController
    //                          -> exceptionHandler(e)
    // client <- [spring-server <- ...

    @PostMapping
    @Operation(summary = "Create timesheet", description = "Создать таймшит")// создание нового ресурса
    public ResponseEntity<Timesheet> create(@RequestBody @Parameter(description = "Объект типа Timesheet") Timesheet timesheet) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(timesheet).get());
    }

    @Operation(summary = "Change timesheet", description = "Изменить таймшит по его идентификатору")
    @PutMapping("/{id}")
    public ResponseEntity<Timesheet> put(@PathVariable @Parameter(description = "Идентификатор таймшита") Long id,
                                         @RequestBody @Parameter(description = "Объект типа Timesheet") Timesheet timesheet) {
        return service.changeById(id, timesheet)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete timesheet", description = "Удалить таймшит")
    public ResponseEntity<Void> delete(@PathVariable @Parameter(description = "Идентификатор таймшит") Long id) {
        service.delete(id);

        // 204 No Content
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.notFound().build();
    }

}

