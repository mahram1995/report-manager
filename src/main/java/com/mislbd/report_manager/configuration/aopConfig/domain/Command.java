package com.mislbd.report_manager.configuration.aopConfig.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public abstract class Command<P> implements Serializable {
    private String processId;
    public String source;
    /** @deprecated */
    @Deprecated
    public String executedBy;
    private boolean isApprovalFlowRequired;
    private LocalDateTime initiatingTime;
    private String initiator;
    private Long initiatorBranch;
    private String initiatorTerminal;
    private String verifier;
    private Long verifierBranch;
    private String verifierTerminal;
    private P payload;
    private Map<String, String> arguments;

    public Command() {
        this.isApprovalFlowRequired = Boolean.FALSE;
    }

    public Command(P payload) {
        this.isApprovalFlowRequired = Boolean.FALSE;
        this.payload = payload;
        this.processId = UUID.randomUUID().toString();
    }

    public Command(P payload, Map<String, String> arguments) {
        this.isApprovalFlowRequired = Boolean.FALSE;
        this.payload = payload;
        this.processId = UUID.randomUUID().toString();
        this.arguments = arguments;
    }

    public LocalDateTime getInitiatingTime() {
        return this.initiatingTime;
    }

    public void setInitiatingTime(LocalDateTime initiatingTime) {
        this.initiatingTime = initiatingTime;
    }

    /** @deprecated */
    @Deprecated
    public String getExecutedBy() {
        return this.executedBy;
    }

    /** @deprecated */
    @Deprecated
    public void setExecutedBy(String executedBy) {
        this.executedBy = executedBy;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public P getPayload() {
        return this.payload;
    }

    public void setPayload(P payload) {
        this.payload = payload;
    }

    public String getProcessId() {
        return this.processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Map<String, String> getArguments() {
        return this.arguments;
    }

    public void setArguments(Map<String, String> arguments) {
        this.arguments = arguments;
    }

    public String getInitiator() {
        return this.initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public Long getInitiatorBranch() {
        return this.initiatorBranch;
    }

    public void setInitiatorBranch(Long initiatorBranch) {
        this.initiatorBranch = initiatorBranch;
    }

    public String getInitiatorTerminal() {
        return this.initiatorTerminal;
    }

    public void setInitiatorTerminal(String initiatorTerminal) {
        this.initiatorTerminal = initiatorTerminal;
    }

    public boolean isApprovalFlowRequired() {
        return this.isApprovalFlowRequired;
    }

    public void setApprovalFlowRequired(boolean approvalFlowRequired) {
        this.isApprovalFlowRequired = approvalFlowRequired;
    }

    public String getVerifier() {
        return this.verifier;
    }

    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }

    public Long getVerifierBranch() {
        return this.verifierBranch;
    }

    public void setVerifierBranch(Long verifierBranch) {
        this.verifierBranch = verifierBranch;
    }

    public String getVerifierTerminal() {
        return this.verifierTerminal;
    }

    public void setVerifierTerminal(String verifierTerminal) {
        this.verifierTerminal = verifierTerminal;
    }
}
