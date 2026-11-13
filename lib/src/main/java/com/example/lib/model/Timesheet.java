package com.example.lib.model;

import java.time.LocalDate;

public class Timesheet {
    private int employeeId;
    private LocalDate date;
    private int hoursWorked;
    private int overtimeHours;

    public Timesheet(int employeeId, LocalDate date, int hoursWorked, int overtimeHours) {
        setEmployeeId(employeeId);
        setDate(date);
        setHoursWorked(hoursWorked);
        setOvertimeHours(overtimeHours);
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        if (employeeId <= 0) {
            throw new IllegalArgumentException("ID сотрудника должен быть положительным.");
        }
        this.employeeId = employeeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Дата табеля не может быть null.");
        }
        this.date = date;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(int hoursWorked) {
        if (hoursWorked < 0 || hoursWorked > 24) {
            throw new IllegalArgumentException("Отработанные часы за день должны быть от 0 до 24.");
        }
        this.hoursWorked = hoursWorked;
    }

    public int getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(int overtimeHours) {
        if (overtimeHours < 0 || overtimeHours > 12) {
            throw new IllegalArgumentException("Переработка за день должна быть от 0 до 12 часов.");
        }
        this.overtimeHours = overtimeHours;
    }

    @Override
    public String toString() {
        return String.format("Табель [Сотрудник ID: %d | Дата: %s | Основные часы: %d | Переработка: %d]",
                employeeId, date, hoursWorked, overtimeHours);
    }
}
