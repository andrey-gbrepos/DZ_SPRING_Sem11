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
public interface ProjectEmployRepository extends JpaRepository<ProjectEmploy, Long> {


    default void fulProjectEmploy(ProjectEmployRepository projectEmployRepository, TimesheetRepository timesheetRepository) {
        for (Timesheet item : timesheetRepository.findAll()) {
            ProjectEmploy projectEmploy = new ProjectEmploy();
            projectEmploy.setProjectId(item.getProjectId());
            projectEmploy.setEmployeeId(item.getEmployeeId());
            projectEmployRepository.save(projectEmploy);
        }
    }

//    @Query("select t  from ProjectEmploy t  order by t.projectId, t.employeeId")
//    List<ProjectEmploy>addEmployName(ProjectEmployRepository prEmplRepo, EmployeeRepository employeeRepo);

    @Query("select t  from ProjectEmploy t  order by t.projectId, t.employeeId")
    List<ProjectEmploy> listProjectEmploy();



}
