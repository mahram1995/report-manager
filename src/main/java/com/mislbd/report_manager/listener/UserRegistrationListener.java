package com.mislbd.report_manager.listener;

import com.mislbd.report_manager.configuration.annotation.ApprovalFlowTaskListener;
import com.mislbd.report_manager.configuration.annotation.OnApprove;
import com.mislbd.report_manager.configuration.annotation.OnCorrection;
import com.mislbd.report_manager.configuration.annotation.OnRejection;
import com.mislbd.report_manager.entity.admin.UserEntity;
import com.mislbd.report_manager.service.admin.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ApprovalFlowTaskListener(operation = "CREATE_NEW_USER")
public class UserRegistrationListener {
    private final AuthService authService;

    public UserRegistrationListener(AuthService authService) {
        this.authService = authService;
    }


    @OnApprove
    public ResponseEntity<?> doOnApproveTransaction(UserEntity payload) {
        return authService.saveUser(payload);

    }

    @OnCorrection
    public void doOnCorrectionTransaction(UserEntity payload) {
        System.out.println("✏️ Sent back for correction: " + payload.getUserName());
    }

    @OnRejection
    public void doOnRejectTransaction(UserEntity payload) {
        System.out.println("❌ Rejected user: " + payload.getUserName());
    }
}
