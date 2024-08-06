package ru.gb.timesheet.service;

import org.springframework.stereotype.Service;
import ru.gb.aspect.logging.Logging;
import ru.gb.timesheet.TimesheetRestApplication;
import ru.gb.timesheet.model.Employee;
import ru.gb.timesheet.model.Project;
import ru.gb.timesheet.model.ProjectEmploy;
import ru.gb.timesheet.model.Timesheet;
import ru.gb.timesheet.repository.ProjectEmployRepository;
import ru.gb.timesheet.repository.ProjectRepository;
import ru.gb.timesheet.repository.TimesheetRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TimesheetRepository timesheetRepository;
    private final ProjectEmployRepository projectEmployRepository;

    public ProjectService(ProjectRepository projectRepository, TimesheetRepository timesheetRepository, ProjectEmployRepository projectEmployRepository) {
        this.projectRepository = projectRepository;
        this.timesheetRepository = timesheetRepository;
        this.projectEmployRepository = projectEmployRepository;
    }

    @Logging
    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public List<ProjectEmploy> listProjectEmploy() {
        projectEmployRepository.fulProjectEmploy(projectEmployRepository, timesheetRepository);
        return projectEmployRepository.listProjectEmploy();
    }


    public Project create(Project project) {
        return projectRepository.save(project);
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    public List<Timesheet> getTimesheets(Long id) {
        if (projectRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("Project with id = " + id + " does not exists");
        }

        return timesheetRepository.findByProjectId(id);
    }
}

