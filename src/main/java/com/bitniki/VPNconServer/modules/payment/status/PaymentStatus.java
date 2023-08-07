package com.bitniki.VPNconServer.modules.payment.status;

public enum PaymentStatus {
    IN_PROCESS("in_process"),
    COMPLETED("completed"),
    FAILURE("error");

    private final String statusPageName;

    PaymentStatus(String statusPageName) {
        this.statusPageName = statusPageName;
    }

    public String getStatusPageName() {
        return statusPageName;
    }
}
