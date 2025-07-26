package com.mislbd.report_manager.listener;

import com.mislbd.report_manager.configuration.annotation.ApprovalFlowTaskListener;
import com.mislbd.report_manager.configuration.annotation.OnApprove;
import com.mislbd.report_manager.configuration.annotation.OnCorrection;
import com.mislbd.report_manager.configuration.annotation.OnRejection;
import com.mislbd.report_manager.entity.admin.UserEntity;
import org.springframework.stereotype.Component;

@Component
@ApprovalFlowTaskListener(operation = "CREATE_NEW_USER")
public class UserRegistrationListener {

    @OnApprove
    public void doOnApproveTransaction(UserEntity payload) {
        System.out.println("✅ Approved user: " + payload.getUserName());
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
