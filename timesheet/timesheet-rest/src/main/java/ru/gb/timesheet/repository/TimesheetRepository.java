package ru.gb.timesheet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.gb.timesheet.model.Project;
import ru.gb.timesheet.model.ProjectEmploy;
import ru.gb.timesheet.model.Timesheet;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimesheetRepository extends JpaRepository<Timesheet, Long>
        /* , NamedEntityRepository<Timesheet, Long> */ {

    // select * from timesheet where project_id = $1
    // Note: сломается, если в БД результат выдает больше одного значения
    // List<Timesheet> findByProjectId(Long projectId);

//  default List<Timesheet> findByCreatedAtBetweenUnsafe(LocalDate min, LocalDate max) {
//    if (min == null && max == null) {
//      return findAll();
//    } else if (min == null) {
//      return findByCreatedAtLessThan(max);
//    }
//  }

    // select * from timesheet where created_at > $1 and created_at < $2
    List<Timesheet> findByCreatedAtBetween(LocalDate min, LocalDate max);

    // select * from timesheet where project_id = $1
    // order by created_at desc
    // jql - java query language
    @Query("select t from Timesheet t where t.projectId = :projectId order by t.createdAt desc")
    List<Timesheet> findByProjectId(Long projectId);

    @Query("select t from Timesheet t where t.employeeId = :employeeId")
    List<Timesheet> findByEmployeeId(Long employeeId);


    //@Query(nativeQuery = true, value = "select timesheet.project_id projectId, timesheet.employee_id employeeId from timesheet")
    @Query("select distinct t.projectId, t.employeeId from ProjectEmploy t")
    List<ProjectEmploy> listProjectEmploy();


    @Query(value = "select t from Timesheet t where t.id = (select max(id) from  Timesheet)")
    Timesheet getLastRecord();


    default void insert(TimesheetRepository timesheetRepository, Timesheet timesheet) {
      timesheetRepository.save(timesheet);
    }


    void deleteById(Long timesheetId);

   // Optional<Timesheet> changeById(Long id, Timesheet timesheet);


//    @Modifying
//    @Query("insert into Timesheet t (t.projectId, t.employeeId, t.minutes, t.createdAt) values (projectId, employeeId, minutes, createdAt)")
//    void insert(Long projectId,
//                Long employeeId,
//                Integer minutes,
//                LocalDate createdAt);

//    @Modifying
//    @Query(value = "insert into timesheet t (t.projectId, t.employeeId, t.minutes, t.createdAt) values (:projectId, :employeeId, :minutes, :createdAt)", nativeQuery = true)
//    void insert(@Param("projectId") Long projectId,
//                     @Param("employeeId") Long employeeId,
//                     @Param("minutes") Integer minutes,
//                     @Param("createdAt") LocalDate createdAt);
//INSERT INTO users (login, pass) values('TestUser', '123456')
//  @Query(nativeQuery = true, value = "select * from timesheet where project_id = :projectId")
//  List<Long> findIdsByProjectId(Long projectId);

//  @Query(nativeQuery = true, value = "update timesheet set active = false where project_id = :projectId")
//  @Modifying
//  void deactivateTimesheetsWithProjectId(Long projectId);


//  @Query("select t.id from Timesheet t where t.projectId = :projectId order by t.createdAt desc")
//  List<Long> findIdsByProjectId(Long projectId);

// select * from timesheet where project_id = $1
// Note: сломается, если в БД результат выдает больше одного значения
// Optional<Timesheet> findByProjectId(Long projectId);

// select * from timesheet where project_id = $1
// order by created_at desc
// List<Timesheet> findByProjectIdOrderByCreatedAtDesc(Long projectId);

// select * from timesheet where project_id = $1 or minutes = $2
// List<Timesheet> findByProjectIdOrMinutes(Long projectId, Integer minutes);

// ... where project_name like '%projectNameLike%'
// List<Timesheet> findByProjectNameLike(String projectNameLike);

}
