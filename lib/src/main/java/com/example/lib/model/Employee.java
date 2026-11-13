package com.example.lib.model;

import com.example.lib.exception.InvalidSalaryException;
import java.util.Objects;

public abstract class Employee {
    private int id;
    private String name;
    private double baseSalary;
    private Position position;
    private EmployeeStatus status;
    private int departmentId;
    private int managerId; // 0, если нет руководителя (топ-менеджер)
    private int experienceYears;

    public Employee(int id, String name, double baseSalary, Position position,
                    int departmentId, int managerId, int experienceYears) {
        setId(id);
        setName(name);
        setBaseSalary(baseSalary);
        setPosition(position);
        setStatus(EmployeeStatus.ACTIVE);
        setDepartmentId(departmentId);
        setManagerId(managerId);
        setExperienceYears(experienceYears);
    }

    public abstract double calculateGrossSalary();

    public abstract String getSpecificDataForCsv();

    public double calculateTax() {
        return calculateGrossSalary() * 0.13;
    }

    public double calculateNetSalary() {
        return (calculateGrossSalary() - calculateTax()) * status.getPayFactor();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID сотрудника должен быть положительным числом.");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя сотрудника не может быть пустым.");
        }
        this.name = name.trim();
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        if (baseSalary < 0) {
            throw new InvalidSalaryException("Базовая ставка не может быть отрицательной: " + baseSalary);
        }
        this.baseSalary = baseSalary;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Должность не может быть null.");
        }
        this.position = position;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Статус сотрудника не может быть null.");
        }
        this.status = status;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        if (departmentId <= 0) {
            throw new IllegalArgumentException("ID отдела должен быть положительным.");
        }
        this.departmentId = departmentId;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        if (managerId < 0) {
            throw new IllegalArgumentException("ID руководителя не может быть отрицательным.");
        }
        this.managerId = managerId;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        if (experienceYears < 0) {
            throw new IllegalArgumentException("Стаж не может быть отрицательным.");
        }
        this.experienceYears = experienceYears;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("ID: %d | %s (%s) | Имя: %s | Отдел: %d | Стаж: %d г. | На руки: %.2f",
                id, position.getTitle(), status.getDescription(), name, departmentId, experienceYears, calculateNetSalary());
    }
}
