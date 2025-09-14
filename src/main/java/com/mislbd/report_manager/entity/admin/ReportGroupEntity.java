package com.mislbd.report_manager.entity.admin;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reportGroup")
public class ReportGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reportGroup_seq_gen")
    @SequenceGenerator(name = "reportGroup_seq_gen", sequenceName = "reportGroupSeq", allocationSize = 1)
    private Long id;

    private String groupName;
}
