package ru.gb.timesheet.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@Entity
@Table(name = "project_employ")
public class ProjectEmploy {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @EqualsAndHashCode.Include
    private Long id;

    private Long projectId;
   // private String pojectName;
    private Long employeeId;
  //  private String employName;
}
