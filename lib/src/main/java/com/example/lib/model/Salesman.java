package com.example.lib.model;

import com.example.lib.exception.InvalidSalaryException;

public class Salesman extends Employee implements BonusEligible {
    private double salesVolume;
    private double commissionRate; // например, 0.05 (5%)

    public Salesman(int id, String name, double baseSalary, int departmentId,
                    int managerId, int experienceYears, double salesVolume, double commissionRate) {
        super(id, name, baseSalary, Position.SALESMAN, departmentId, managerId, experienceYears);
        setSalesVolume(salesVolume);
        setCommissionRate(commissionRate);
    }

    public double getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(double salesVolume) {
        if (salesVolume < 0) {
            throw new InvalidSalaryException("Объем продаж не может быть отрицательным.");
        }
        this.salesVolume = salesVolume;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(double commissionRate) {
        if (commissionRate < 0 || commissionRate > 1.0) {
            throw new InvalidSalaryException("Процент комиссии должен быть от 0.0 до 1.0.");
        }
        this.commissionRate = commissionRate;
    }

    @Override
    public double calculateBonus() {
        return salesVolume * commissionRate;
    }

    @Override
    public String getBonusDescription() {
        return String.format("Комиссия %.1f%% с продаж %.2f", commissionRate * 100, salesVolume);
    }

    @Override
    public double calculateGrossSalary() {
        return (getBaseSalary() * getPosition().getBaseCoefficient()) + calculateBonus();
    }

    @Override
    public String getSpecificDataForCsv() {
        return salesVolume + ":" + commissionRate;
    }

    @Override
    public String toString() {
        return super.toString() + " [Продажи: " + salesVolume + ", Премия: " + String.format("%.2f", calculateBonus()) + "]";
    }
}
