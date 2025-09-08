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
    private String label;
    private String styleClass;
    private String maximumLength;
    private String minimumLength;
    private String regularExpression;
    private String dataType;
    private Boolean singleData;
    private Boolean multipleSelection;
    private Boolean mandatory;
    private Integer orderNo;
    private Boolean isServiceEndpoint;
    private String serviceEndpointName;
    private String labelOfServiceEndpoint;
    private String valueOfServiceEndpoint;
    private Boolean isConditionallyAppearance;
    private String fieldGroup;
    // ✅ Foreign key column in UDF_UDFS
    @Column(name = "udfProfileId")
    private Long udfProfileId;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "userDefinedFieldId", referencedColumnName = "id")
    private List<UdfFieldAppearanceLogicEntity> fieldAppearanceLogics;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "userDefinedFieldId", referencedColumnName = "id")
    private List<UdfDomainDataEntity> userDefinedFieldDomainDataList;





}
