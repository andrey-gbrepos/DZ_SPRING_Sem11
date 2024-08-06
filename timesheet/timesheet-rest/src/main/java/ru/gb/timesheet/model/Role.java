package ru.gb.timesheet.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Optional;

@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;
}
