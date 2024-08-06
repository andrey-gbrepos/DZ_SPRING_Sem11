package ru.gb.timesheet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import ru.gb.aspect.logging.LoggingAutoConfiguration;
import ru.gb.timesheet.model.*;
import ru.gb.timesheet.repository.*;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@EnableDiscoveryClient
@SpringBootApplication
public class TimesheetRestApplication {
    /**
     * spring-data - ...
     * spring-data-jdbc - зависимость, которая предоставляет удобные преднастроенные инструемнты
     * для работы c реляционными БД
     * spring-data-jpa - зависимость, которая предоставляет удобные преднастроенные инструемнты
     * для работы с JPA
     * spring-data-jpa
     * /
     * /
     * jpa   <------------- hibernate (ecliselink ...)
     * <p>
     * spring-data-mongo - завимость, которая предоставляет инструменты для работы с mongo
     * spring-data-aws
     */

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(TimesheetRestApplication.class, args);

//		JdbcTemplate jdbcTemplate = ctx.getBean(JdbcTemplate.class);
//		jdbcTemplate.execute("delete from project");
        RoleRepository roleRepository = ctx.getBean(RoleRepository.class);
        Role roleAdm = new Role();
        roleAdm.setName("admin");

        Role roleUser = new Role();
        roleUser.setName("user");

        Role roleRest = new Role();
        roleRest.setName("rest");

        roleAdm = roleRepository.save(roleAdm);
        roleUser = roleRepository.save(roleUser);
        roleRest = roleRepository.save(roleRest);


        UserRepository userRepository = ctx.getBean(UserRepository.class);

        User admin = new User();
        admin.setLogin("admin");
        admin.setPassword("$2a$12$Ifl4wcihZtxr2Wex75NdDOTaaBF66G5nizmAoQHUyyWJyU2ktRQ12"); //admin

        User user = new User();
        user.setLogin("user");
        user.setPassword("$2a$12$yVdEqlPu2ragCTQVcPOvouEkZrQDuxHnO8KZcEi6G20QzGrfwjJYC");//user

        User rest = new User();
        rest.setLogin("rest");
        rest.setPassword("$2a$12$mdy.FqvrvNg4AbrhggV1xu47lNjmtE6dEjUCsZwXNjTMgjXLmJhj."); // rest

        User anonymous = new User();
        anonymous.setLogin("anon");
        anonymous.setPassword("$2a$12$1idbfD2fAJHDq/1jDQH/4OPuY6lvPHCNNw7amLqiEusNP77GvJRKK"); // anon

        admin = userRepository.save(admin);
        user = userRepository.save(user);
        rest = userRepository.save(rest);
        anonymous = userRepository.save(anonymous);

        UserRoleRepository userRoleRepository = ctx.getBean(UserRoleRepository.class);
        UserRole adminAdminRole = new UserRole();
        adminAdminRole.setUserId(admin.getId());
        adminAdminRole.setRoleId(roleAdm.getId());
        userRoleRepository.save(adminAdminRole);

//		UserRole adminUserRole = new UserRole();
//		adminUserRole.setUserId(admin.getId());
//		adminUserRole.setRoleId(roleUser.getId());
//		userRoleRepository.save(adminUserRole);

        UserRole userUserRole = new UserRole();
        userUserRole.setUserId(user.getId());
        userUserRole.setRoleId(roleUser.getId());
        userRoleRepository.save(userUserRole);

        UserRole restRestRole = new UserRole();
        restRestRole.setUserId(rest.getId());
        restRestRole.setRoleId(roleRest.getId());
        userRoleRepository.save(restRestRole);

        ProjectRepository projectRepo = ctx.getBean(ProjectRepository.class);

        for (int i = 1; i <= 5; i++) {
            Project project = new Project();
            project.setName("Project #" + i);
            projectRepo.save(project);
        }

        EmployeeRepository employeeRepository = ctx.getBean(EmployeeRepository.class);
        String[] employeeNames = {"Viktoria", "Andrey", "Danil", "Maria", "Roman", "Igor", "Veronica"};
        for (int i = 0; i < employeeNames.length; i++) {
            Employee employee = new Employee();
            employee.setName(employeeNames[i]);
            employeeRepository.save(employee);
        }

        TimesheetRepository timesheetRepo = ctx.getBean(TimesheetRepository.class);

        LocalDate createdAt = LocalDate.now();
        for (int i = 1; i <= 15; i++) {
            createdAt = createdAt.plusDays(1);

            Timesheet timesheet = new Timesheet();
            timesheet.setEmployeeId(ThreadLocalRandom.current().nextLong(1, 8));
            timesheet.setProjectId(ThreadLocalRandom.current().nextLong(1, 6));
            timesheet.setCreatedAt(createdAt);
            timesheet.setMinutes(ThreadLocalRandom.current().nextInt(100, 1000));

            timesheetRepo.save(timesheet);
        }
    }
}