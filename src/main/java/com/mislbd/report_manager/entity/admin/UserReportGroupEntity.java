package com.mislbd.report_manager.entity.admin;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "userReportGroup")
public class UserReportGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userReportGroup_seq_gen")
    @SequenceGenerator(name = "userReportGroup_seq_gen", sequenceName = "userReportGroupSeq", allocationSize = 1)
    private Long id;

    private Long userId;
    private Long groupId;
}