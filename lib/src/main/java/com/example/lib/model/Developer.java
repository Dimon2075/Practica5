package com.example.lib.model;

public class Developer extends Employee {
    private int completedTasks;
    private int overtimeHours;

    public Developer(int id, String name, double baseSalary, int departmentId,
                     int managerId, int experienceYears, int completedTasks, int overtimeHours) {
        super(id, name, baseSalary, Position.DEVELOPER, departmentId, managerId, experienceYears);
        setCompletedTasks(completedTasks);
        setOvertimeHours(overtimeHours);
    }

    public int getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(int completedTasks) {
        if (completedTasks < 0) {
            throw new IllegalArgumentException("Количество задач не может быть отрицательным.");
        }
        this.completedTasks = completedTasks;
    }

    public int getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(int overtimeHours) {
        if (overtimeHours < 0) {
            throw new IllegalArgumentException("Часы переработки не могут быть отрицательными.");
        }
        this.overtimeHours = overtimeHours;
    }

    @Override
    public double calculateGrossSalary() {
        double baseWithGrade = getBaseSalary() * getPosition().getBaseCoefficient();
        return baseWithGrade + (completedTasks * 1000.0) + (overtimeHours * 1500.0);
    }

    @Override
    public String getSpecificDataForCsv() {
        return completedTasks + ":" + overtimeHours;
    }

    @Override
    public String toString() {
        return super.toString() + " [Задач: " + completedTasks + ", Переработка: " + overtimeHours + " ч.]";
    }
}
