package com.mislbd.report_manager.entity.admin;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "UDF_DOMAIN_DATA")
@Getter
@Setter
public class UdfDomainDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "udf_domain_data_seq")
    @SequenceGenerator(name = "udf_domain_data_seq", sequenceName = "SEQ_UDF_DOMAIN_DATA", allocationSize = 1)
    private Long id;
    private String value;
    private String label;
    private Integer orderNo;
    private String dependentData;

}
