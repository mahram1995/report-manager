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
@ApprovalFlowTaskListener(operation = "MODIFICATION_USER")
public class UserModificationListener {
    private final AuthService authService;

    public UserModificationListener(AuthService authService) {
        this.authService = authService;
    }


    @OnApprove
    public ResponseEntity<?> doOnApproveTransaction(UserEntity payload) {
        return null;

    }

    @OnCorrection
    public void doOnCorrectionTransaction(UserEntity payload) {
        System.out.println("✏️ Sent back for correction: " + payload.getUserName());
    }

    @OnRejection
    public ResponseEntity<?> doOnRejectTransaction(UserEntity payload) {
        return ResponseEntity.ok().body(Map.of("message", "Task Rejected successfully"));
    }
}
