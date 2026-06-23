package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.configuration.aopConfig.domain.CommandResponse;
import com.mislbd.report_manager.domain.admin.BankDomain;
import com.mislbd.report_manager.entity.admin.BankEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface BankService {
    public CommandResponse<?> saveFinancialInstitute(BankDomain data);
    public CommandResponse<?> updateFinancialInstitute(BankDomain data);
    public Object  getFinancialInstitute(String bankName, boolean asPage, Pageable pageable);
    public BankDomain getFinancialInstituteById(Long bankId);

}
