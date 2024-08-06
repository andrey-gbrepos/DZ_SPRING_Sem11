package ru.gb.timesheet.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.timesheet.model.Employee;
import ru.gb.timesheet.model.Timesheet;
import ru.gb.timesheet.service.EmployeeService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/employees")
@Tag(name = "employees", description = "API для работы с сотрудниками")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }


    @Operation(summary = "Get employee", description = "Получить данные о сотруднике")
    @GetMapping("/{id}") // получить все
    public ResponseEntity<Employee> get(@PathVariable  @Parameter(description = "Идентификатор сотрудника") Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @Operation(summary = "Get timesheets", description = "Получить все таймшиты сотрудника")
    @GetMapping("/{id}/timesheets")
    public ResponseEntity<List<Timesheet>> getTimesheets(@PathVariable @Parameter(description = "Идентификатор сотрудника") Long id) {
        try {
            return ResponseEntity.ok(service.getTimesheets(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Get employees", description = "Получить список сотрудников")
    @GetMapping
    public ResponseEntity<List<Employee>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.notFound().build();
    }

}
