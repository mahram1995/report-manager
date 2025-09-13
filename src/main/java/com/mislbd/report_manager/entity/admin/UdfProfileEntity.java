package com.mislbd.report_manager.entity.admin;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@Table(name = "UDF_PROFILE")
@Getter
@Setter
public class UdfProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "udf_profile_seq")
    @SequenceGenerator(name = "udf_profile_seq", sequenceName = "SEQ_UDF_PROFILE", allocationSize = 1)
    private Long id;
    private String code;
    private String name;
    private String reportFileName;
    private String moduleName;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "udfProfileId", referencedColumnName = "id")
    private List<UdfUdfsEntity> userDefinedFields;
}
