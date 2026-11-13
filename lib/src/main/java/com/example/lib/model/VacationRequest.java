package com.example.lib.model;

import java.time.LocalDate;

public class VacationRequest {
    private int id;
    private int employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean approved;

    public VacationRequest(int id, int employeeId, LocalDate startDate, LocalDate endDate, boolean approved) {
        setId(id);
        setEmployeeId(employeeId);
        setDates(startDate, endDate);
        this.approved = approved;
    }

    public int getId() { return id; }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID заявки на отпуск должен быть положительным.");
        }
        this.id = id;
    }

    public int getEmployeeId() { return employeeId; }

    public void setEmployeeId(int employeeId) {
        if (employeeId <= 0) {
            throw new IllegalArgumentException("ID сотрудника должен быть положительным.");
        }
        this.employeeId = employeeId;
    }

    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }

    public void setDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Даты отпуска не могут быть null.");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Дата окончания отпуска не может быть раньше даты начала.");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }

    public boolean overlapsWith(LocalDate date) {
        return (date.isEqual(startDate) || date.isAfter(startDate))
                && (date.isEqual(endDate) || date.isBefore(endDate));
    }

    @Override
    public String toString() {
        return String.format("Отпуск #%d | Сотрудник ID: %d | Период: %s — %s | Статус: %s",
                id, employeeId, startDate, endDate, approved ? "Одобрен" : "На рассмотрении");
    }
}
