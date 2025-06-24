package com.mislbd.report_manager.entity;

import jakarta.persistence.*;

@Entity
@Table (name = "RPT_DEPARTMENT")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_seq")
    @SequenceGenerator(name = "department_seq", sequenceName = "DEPARTMENT_SEQ", allocationSize = 1)
    private Integer departmentId;


    @Column(length = 100, nullable = false, unique = true)
    private String name;

    public Department(Integer departmentId, String name) {
        this.departmentId = departmentId;
        this.name = name;
    }


    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department() {
    }
}
