package com.mislbd.report_manager.configuration.aopConfig.serviceImpl;

import com.mislbd.report_manager.configuration.aopConfig.auditListener.AuditorContextHolder;
import com.mislbd.report_manager.configuration.aopConfig.service.CommonService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class CommonServiceImpl implements CommonService {
    @Override
    public String getCurrentUsername() {

        return AuditorContextHolder.getCurrentUser();
    }

    @Override
    public String getUserAgent() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes()).getRequest();
            return request.getHeader("User-Agent");
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getTerminal() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes()).getRequest();
            return request.getHeader("terminalIp");
        } catch (Exception e) {
            return null;
        }
    }
}
