package ru.gb.timesheet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.gb.timesheet.model.Employee;
import ru.gb.timesheet.model.Timesheet;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findAll();
    Optional<Employee> findById(Long employeeId);
}
