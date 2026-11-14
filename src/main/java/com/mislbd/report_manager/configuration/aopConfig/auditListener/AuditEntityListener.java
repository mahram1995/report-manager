package com.mislbd.report_manager.configuration.aopConfig.auditListener;

import com.mislbd.report_manager.configuration.aopConfig.domain.Command;
import com.mislbd.report_manager.configuration.aopConfig.entity.BaseEntity;
import com.mislbd.report_manager.configuration.aopConfig.service.CommonService;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


public class AuditEntityListener {
    private final CommonService commonService;

    public AuditEntityListener(CommonService commonService) {
        this.commonService = commonService;
    }

    @PrePersist
    public void setCreatedInfo(Object entity) {
        if (entity instanceof BaseEntity base) {
            Command command=AuditorContextHolder.getCommand();
            if(command!=null){
                if (base.getCreatedBy() == null) {
                    base.setCreatedBy(AuditorContextHolder.getCommand().getInitiator());
                }
                if (base.getCreateDate() == null) {
                    base.setCreateDate(AuditorContextHolder.getCommand().getInitiatingTime().toLocalDate());
                }
                if (base.getCreateDateTime() == null) {
                    base.setCreateDateTime(AuditorContextHolder.getCommand().getInitiatingTime());
                }
                if (base.getCreatedTerminal() == null) {
                    base.setCreatedTerminal(AuditorContextHolder.getCommand().getInitiatorTerminal());
                }
                if (base.getCreateAgent() == null) {
                    base.setCreateAgent(AuditorContextHolder.getCommand().getInitiatorClient());
                }
            }else{
                base.setCreatedBy(commonService.getCurrentUsername());
                base.setCreateDate(LocalDate.now());
                base.setCreateDateTime(LocalDateTime.now());
                base.setCreatedTerminal(commonService.getTerminal());
                base.setCreateAgent(commonService.getUserAgent());
            }
        }
        setVerifyData(entity);
    }

    @PreUpdate
    public void setUpdatedInfo(Object entity) {
        if (entity instanceof BaseEntity base) {
            Command command=AuditorContextHolder.getCommand();
            if(command!=null){
                base.setUpdateBy(AuditorContextHolder.getCommand().getInitiator());
                base.setUpdateDate(AuditorContextHolder.getCommand().getInitiatingTime().toLocalDate());
                base.setUpdateDateTime(AuditorContextHolder.getCommand().getInitiatingTime());
                base.setUpdateTerminal(AuditorContextHolder.getCommand().getInitiatorTerminal());
                base.setUpdateAgent(AuditorContextHolder.getCommand().getInitiatorClient());
            }else{
                base.setUpdateBy(commonService.getCurrentUsername());
                base.setUpdateDate(LocalDate.now());
                base.setUpdateDateTime(LocalDateTime.now());
                base.setUpdateTerminal(commonService.getTerminal());
                base.setUpdateAgent(commonService.getUserAgent());
            }

        }
        setVerifyData(entity);
    }


    public void setVerifyData(Object entity) {
        if (entity instanceof BaseEntity base) {
            Command command=AuditorContextHolder.getCommand();
            if(command!=null){
                base.setVerifyBy(command.getVerifier());
                base.setVerifyTerminal(commonService.getTerminal());
                base.setVerifyDate(LocalDate.now());
                base.setVerifyDateTime(LocalDateTime.now());
                base.setVerifyAgent(getUserAgent());
            }else{
                base.setVerifyBy(commonService.getCurrentUsername());
                base.setVerifyTerminal(commonService.getTerminal());
                base.setVerifyDate(LocalDate.now());
                base.setVerifyDateTime(LocalDateTime.now());
                base.setVerifyAgent(getUserAgent());
            }

        }
    }

    private String getUserAgent() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes()).getRequest();
            return request.getHeader("User-Agent");
        } catch (Exception e) {
            return "unknown";
        }
    }





}
