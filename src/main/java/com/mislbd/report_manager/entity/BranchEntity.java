package com.mislbd.report_manager.entity;


import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity(name = "branch")
public class BranchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "branch_id_generator")
    @SequenceGenerator(
            name = "branch_id_generator",
            allocationSize = 1,
            sequenceName = "branch_sequence")
    private int id;
    private String name;
    private String address;
    private String branchId;
    private String bankId;
    private String routingNumber;
    private boolean isActive;
    private boolean isHeadOffice;
    private boolean isOnline;
    private String status;
    private String email;
    private String mobileNumber;
    private String phoneNumber;
    private String addressLine;
    private String adCode;
    private String swiftCode;
    private String upazillaCode;
    private String districtCode;
    private String divisionCode;
    private String countryCode;
    private String postCode;
    private String houseNo;
    private String roadNo;
    private String villageName;
    public BranchEntity()  {
    }


}

