package com.mislbd.report_manager.entity.admin;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "userExceptionalReport")
public class UserExceptionalReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userExceptionalReport_seq_gen")
    @SequenceGenerator(name = "userExceptionalReport_seq_gen", sequenceName = "userExceptionalReportSeq", allocationSize = 1)
    private Long id;

    private Long userId;
    private String reportCode;
    private Boolean isExclude;
    private Boolean isInclude;
}
