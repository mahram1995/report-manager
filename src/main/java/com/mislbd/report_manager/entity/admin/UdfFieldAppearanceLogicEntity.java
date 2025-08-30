package com.mislbd.report_manager.entity.admin;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "UDF_FIELD_APPEARANCE_LOGICS")
@Getter
@Setter
public class UdfFieldAppearanceLogicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "udf_field_logic_seq")
    @SequenceGenerator(name = "udf_field_logic_seq", sequenceName = "SEQ_UDF_FIELD_LOGIC", allocationSize = 1)
    private Long id;

    private Long dependentFieldId;
    private String logicType;
    private String value;
    private String paramKeyword;


}

