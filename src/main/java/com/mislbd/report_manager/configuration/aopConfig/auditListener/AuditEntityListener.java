package com.mislbd.report_manager.configuration.aopConfig.auditListener;

import com.mislbd.report_manager.configuration.aopConfig.entity.BaseEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public class AuditEntityListener {

    @PrePersist
    public void setCreatedInfo(Object entity) {
        if (entity instanceof BaseEntity base) {
            if (base.getCreatedBy() == null) {
                base.setCreatedBy(AuditorContextHolder.getCurrentUser());
            }
            if (base.getCreateDate() == null) {
                base.setCreateDate(LocalDate.now());
            }
            if (base.getCreatedTerminal() == null) {
                base.setCreatedTerminal(getClientIP());
            }
            if (base.getCreateAgent() == null) {
                base.setCreateAgent(getUserAgent());
            }
        }
    }

    @PreUpdate
    public void setUpdatedInfo(Object entity) {
        if (entity instanceof BaseEntity base) {
            base.setUpdateBy(AuditorContextHolder.getCurrentUser());
            base.setUpdateDate(LocalDate.now());
            base.setUpdateTerminal(getClientIP());
            base.setUpdateAgent(getUserAgent());
        }
    }



    private String getClientIP() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes()).getRequest();
            return request.getRemoteAddr();
        } catch (Exception e) {
            return "unknown";
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
