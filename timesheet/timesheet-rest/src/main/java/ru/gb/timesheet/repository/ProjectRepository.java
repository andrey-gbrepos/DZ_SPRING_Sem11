package ru.gb.timesheet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.gb.timesheet.model.Project;
import ru.gb.timesheet.model.ProjectEmploy;
import ru.gb.timesheet.model.Timesheet;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

//@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

        List<Project> findAll();
//        @Query("select t from Project t")
//        List<Project> findAll();


        Optional<Project> findById(Long projectId);
}
