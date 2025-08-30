package com.mislbd.report_manager.entity.admin;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "UDF_UDFS")
@Getter
@Setter
public class UdfUdfsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "udf_udfs_seq")
    @SequenceGenerator(name = "udf_udfs_seq", sequenceName = "SEQ_UDF_UDFS", allocationSize = 1)
    private Long id;

    private String name;
    private String styleClass;
    private Integer maximumLength;
    private Integer minimumLength;
    private String regularExpression;
    private String dataType;
    private Boolean singleData;
    private Boolean multipleSelection;
    private Boolean mandatory;
    private Integer orderNo;
    private String label;
    private Boolean conditionallyAppearance;
    private String fieldGroup;



}
