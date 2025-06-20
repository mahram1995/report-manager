package com.mislbd.report_manager.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Entity
@Table(name = "RPT_EMPLOYEE")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="EMPLOYEE_ID_GENERATOR")
    @SequenceGenerator(name = "EMPLOYEE_ID_GENERATOR", sequenceName = "RPT_EMPLOYEE_SEQUENCE",
    allocationSize = 1)
    private Long employeeId;

    @Column(length = 100,  nullable = false,  unique = true)
    private String firstName;

    private String lastName;
    @Nonnull
    private String email;

    @ManyToOne
    @JoinColumn(
            name = "department_id",
            foreignKey = @ForeignKey(name = "FK_EMPLOYEE_DEPARTMENT")
    )
    Department department;
    @Transient
    private Long age;

    private String phone;
    private String address;
    private Date dateOfBirth;
    @Lob
    private String remarks;

}
