package com.example.lib.model;

public enum EmployeeStatus {
    ACTIVE("Работает", true, 1.0),
    VACATION("В отпуске", false, 0.8),
    SICK_LEAVE("На больничном", false, 0.6),
    DISMISSED("Уволен", false, 0.0);

    private final String description;
    private final boolean availableForWork;
    private final double payFactor;

    EmployeeStatus(String description, boolean availableForWork, double payFactor) {
        this.description = description;
        this.availableForWork = availableForWork;
        this.payFactor = payFactor;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAvailableForWork() {
        return availableForWork;
    }

    public double getPayFactor() {
        return payFactor;
    }
}
