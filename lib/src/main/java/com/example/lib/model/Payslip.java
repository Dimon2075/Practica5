package com.example.lib.model;

import java.time.LocalDate;

public class Payslip {
    private int employeeId;
    private String employeeName;
    private Position position;
    private LocalDate issueDate;
    private double grossAmount;
    private double taxAmount;
    private double netAmount;

    public Payslip(int employeeId, String employeeName, Position position,
                   LocalDate issueDate, double grossAmount, double taxAmount, double netAmount) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.position = position;
        this.issueDate = issueDate;
        this.grossAmount = grossAmount;
        this.taxAmount = taxAmount;
        this.netAmount = netAmount;
    }

    public int getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public Position getPosition() { return position; }
    public LocalDate getIssueDate() { return issueDate; }
    public double getGrossAmount() { return grossAmount; }
    public double getTaxAmount() { return taxAmount; }
    public double getNetAmount() { return netAmount; }

    @Override
    public String toString() {
        return String.format("РАСЧЁТНЫЙ ЛИСТ [%s] | ID: %d | %s (%s) | Начислено: %.2f | Налог (13%%): %.2f | К выплате: %.2f",
                issueDate, employeeId, employeeName, position.getTitle(), grossAmount, taxAmount, netAmount);
    }
}
