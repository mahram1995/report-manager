package com.mislbd.report_manager.listener;

import com.mislbd.report_manager.command.CreateNewUserCommand;
import com.mislbd.report_manager.configuration.annotation.*;
import com.mislbd.report_manager.entity.admin.UserEntity;
import com.mislbd.report_manager.service.admin.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ApprovalFlowTaskListener(operation = "CreateNewUserCommand")
public class UserRegistrationListener {
    private final AuthService authService;

    public UserRegistrationListener(AuthService authService) {
        this.authService = authService;
    }
    @OnStart
    public ResponseEntity<?> doOnStart(UserEntity payload) {
        System.out.println("Listener is call: Command is Start");
        return null;

    }

    @OnApprove
    public ResponseEntity<?> doOnApprove(UserEntity payload) {
        System.out.println("Listener is call: user is saved");
        return null;

    }

    @OnCorrection
    public void doOnCorrection(UserEntity payload) {
        System.out.println("✏️ Sent back for correction: " + payload.getUserName());
    }

    @OnRejection
    public ResponseEntity<?> doOnReject(UserEntity payload) {
        return ResponseEntity.ok().body(Map.of("message", "Task Rejected successfully"));
    }
}
