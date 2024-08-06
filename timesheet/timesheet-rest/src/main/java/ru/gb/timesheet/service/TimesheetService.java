package ru.gb.timesheet.service;

import org.springframework.stereotype.Service;
import ru.gb.aspect.logging.Logging;
import ru.gb.timesheet.aspect.Timer;
import ru.gb.timesheet.model.Timesheet;
import ru.gb.timesheet.repository.ProjectRepository;
import ru.gb.timesheet.repository.TimesheetRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
public class TimesheetService {

    private final TimesheetRepository timesheetRepository;
    private final ProjectRepository projectRepository;

    public TimesheetService(TimesheetRepository repository, ProjectRepository projectRepository) {
        this.timesheetRepository = repository;
        this.projectRepository = projectRepository;
    }

    @Timer
    @Logging
    public Optional<Timesheet> findById(Long id) {
        return timesheetRepository.findById(id);
    }

    public List<Timesheet> findAll() {
        return findAll(null, null);
    }

    @Logging
    public List<Timesheet> findAll(LocalDate createdAtBefore, LocalDate createdAtAfter) {
        // FIXME: Вернуть фильтрацию

        return timesheetRepository.findAll();
    }

    public Optional<Timesheet> create(Timesheet timesheet) {
        return Optional.of(timesheetRepository.save(timesheet));
    }

    public Optional<Timesheet> changeById(Long id, Timesheet timesheet) {
        Optional<Timesheet> timesheetOpt = timesheetRepository.findById(id);
        if (timesheetOpt.isPresent()) {
            timesheetOpt.get().setEmployeeId(timesheet.getEmployeeId());
            timesheetOpt.get().setProjectId(timesheet.getProjectId());
            timesheetOpt.get().setMinutes(timesheet.getMinutes());
            timesheetOpt.get().setCreatedAt(timesheet.getCreatedAt());
        }
        return timesheetOpt;
    }

    @Timer
    public void delete(Long id) {
        timesheetRepository.deleteById(id);
    }

}

