package com.mislbd.report_manager.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "RPT_DEPARTMENT")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rpt_department_seq")
    @SequenceGenerator(name = "rpt_department_seq", sequenceName = "RPT_DEPT_SEQ", allocationSize = 1)
    private Long departmentId;


    @Column(length = 100, nullable = false, unique = true)
    private String name;
}
