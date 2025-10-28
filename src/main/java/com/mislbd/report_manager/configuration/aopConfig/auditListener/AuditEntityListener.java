package com.mislbd.report_manager.configuration.aopConfig.auditListener;

import com.mislbd.report_manager.configuration.aopConfig.entity.BaseEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class AuditEntityListener {


    @PrePersist
    public void setCreatedInfo(Object entity) {
        if (entity instanceof BaseEntity base) {
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
        }
        setVerifyData(entity);
    }

    @PreUpdate
    public void setUpdatedInfo(Object entity) {
        if (entity instanceof BaseEntity base) {
            base.setUpdateBy(AuditorContextHolder.getCommand().getInitiator());
            base.setUpdateDate(AuditorContextHolder.getCommand().getInitiatingTime().toLocalDate());
            base.setUpdateDateTime(AuditorContextHolder.getCommand().getInitiatingTime());
            base.setUpdateTerminal(AuditorContextHolder.getCommand().getInitiatorTerminal());
            base.setUpdateAgent(AuditorContextHolder.getCommand().getInitiatorClient());
        }
        setVerifyData(entity);
    }


    public void setVerifyData(Object entity) {
        if (entity instanceof BaseEntity base) {
            String verifier=AuditorContextHolder.getCommand().getVerifier();
            if(verifier!=null){
                base.setVerifyBy(AuditorContextHolder.getCommand().getVerifier());
                base.setVerifyDate(LocalDate.now());
                base.setVerifyDateTime(LocalDateTime.now());
                base.setVerifyTerminal(AuditorContextHolder.getCommand().getVerifierTerminal());
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
