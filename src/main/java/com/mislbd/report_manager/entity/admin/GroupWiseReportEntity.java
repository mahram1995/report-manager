package com.mislbd.report_manager.entity.admin;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "groupWiseReport")
public class GroupWiseReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "groupWiseReport_seq_gen")
    @SequenceGenerator(name = "groupWiseReport_seq_gen", sequenceName = "groupWiseReportSeq", allocationSize = 1)
    private Long id;

    private Long groupId;
    private String reportCode;
}
