package com.example.lib.model;

import com.example.lib.exception.InvalidSalaryException;

public class Manager extends Employee implements BonusEligible {
    private double projectBonus;

    public Manager(int id, String name, double baseSalary, int departmentId,
                   int managerId, int experienceYears, double projectBonus) {
        super(id, name, baseSalary, Position.MANAGER, departmentId, managerId, experienceYears);
        setProjectBonus(projectBonus);
    }

    public double getProjectBonus() {
        return projectBonus;
    }

    public void setProjectBonus(double projectBonus) {
        if (projectBonus < 0) {
            throw new InvalidSalaryException("Проектный бонус не может быть отрицательным.");
        }
        this.projectBonus = projectBonus;
    }

    @Override
    public double calculateBonus() {
        return projectBonus;
    }

    @Override
    public String getBonusDescription() {
        return "Фиксированный бонус за управление проектами: " + projectBonus;
    }

    @Override
    public double calculateGrossSalary() {
        return (getBaseSalary() * getPosition().getBaseCoefficient()) + calculateBonus();
    }

    @Override
    public String getSpecificDataForCsv() {
        return String.valueOf(projectBonus);
    }

    @Override
    public String toString() {
        return super.toString() + " [Бонус руководителя: " + String.format("%.2f", projectBonus) + "]";
    }
}
