package ru.gb.timesheet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.timesheet.model.Role;
import java.util.Optional;
public interface RoleRepository extends JpaRepository<Role, Long>  {

    Optional<Role> findById(Long roleId);
}
