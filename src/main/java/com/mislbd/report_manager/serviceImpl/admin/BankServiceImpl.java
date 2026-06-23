package com.mislbd.report_manager.serviceImpl.admin;

import com.mislbd.report_manager.configuration.aopConfig.domain.CommandResponse;
import com.mislbd.report_manager.domain.admin.BankDomain;
import com.mislbd.report_manager.mapper.admin.BankMapper;
import com.mislbd.report_manager.repository.admin.BankRepository;
import com.mislbd.report_manager.service.admin.BankService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;
    private final BankMapper bankMapper;

    public BankServiceImpl(BankRepository bankRepository, BankMapper bankMapper) {
        this.bankRepository = bankRepository;
        this.bankMapper = bankMapper;
    }

    @Override
    public CommandResponse<?> saveFinancialInstitute(BankDomain data) {


        return new CommandResponse<>(bankRepository.save(bankMapper.domainToEntity(data)));
    }

    @Override
    public CommandResponse<?> updateFinancialInstitute(BankDomain data) {
        return new CommandResponse<>(bankRepository.save(bankMapper.domainToEntity(data)));
    }

    @Override
    public Object getFinancialInstitute(String bankName,
                                        boolean asPage,
                                        Pageable pageable) {

        bankName = bankName == null ? "" : bankName.trim();

        if (asPage) {
            return bankRepository
                    .findByBankNameContainingIgnoreCase(bankName, pageable)
                    .map(BankMapper::entityToDto);
        }

        return bankRepository
                .findByBankNameContainingIgnoreCase(bankName)
                .stream()
                .map(BankMapper::entityToDto)
                .toList();
    }

    @Override
    public BankDomain getFinancialInstituteById(Long bankId) {
        return bankRepository.findById(bankId)
                .map(BankMapper::entityToDto)
                .orElse(null);
    }
}
