package com.example.lib.service;

import com.example.lib.model.*;
import java.time.LocalDate;

public class PayrollService {

    // Собственная реализация сортировки (пузырьком) с использованием EmployeeComparator
    public Employee[] sortEmployees(Employee[] source, EmployeeComparator comparator) {
        Employee[] copy = new Employee[source.length];
        for (int i = 0; i < source.length; i++) {
            copy[i] = source[i];
        }

        int n = copy.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(copy[j], copy[j + 1]) > 0) {
                    Employee temp = copy[j];
                    copy[j] = copy[j + 1];
                    copy[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
        return copy;
    }

    // Полиморфный расчёт общего фонда оплаты труда (на руки)
    public double calculateTotalNetPayroll(Employee[] employees) {
        double total = 0;
        for (int i = 0; i < employees.length; i++) {
            total += employees[i].calculateNetSalary();
        }
        return total;
    }

    // Формирование расчётного листа для сотрудника
    public Payslip generatePayslip(Employee emp) {
        double gross = emp.calculateGrossSalary();
        double tax = emp.calculateTax();
        double net = emp.calculateNetSalary();
        return new Payslip(emp.getId(), emp.getName(), emp.getPosition(), LocalDate.now(), gross, tax, net);
    }

    // ОТЧЁТ 1: Фонд оплаты труда по отделам (группировка и подсчёт)
    public String buildDepartmentPayrollReport(Employee[] employees, Department[] departments) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== ОТЧЁТ №1: ФОНД ОПЛАТЫ ТРУДА ПО ОТДЕЛАМ ===\n");
        sb.append(String.format("%-6s | %-22s | %-10s | %-15s | %-15s\n",
                "ID", "Отдел", "Сотрудн.", "Начислено (Gross)", "К выплате (Net)"));
        sb.append("------------------------------------------------------------------------------\n");

        double grandGross = 0;
        double grandNet = 0;

        for (int i = 0; i < departments.length; i++) {
            Department dept = departments[i];
            int count = 0;
            double deptGross = 0;
            double deptNet = 0;

            for (int j = 0; j < employees.length; j++) {
                if (employees[j].getDepartmentId() == dept.getId()
                        && employees[j].getStatus() != EmployeeStatus.DISMISSED) {
                    count++;
                    deptGross += employees[j].calculateGrossSalary();
                    deptNet += employees[j].calculateNetSalary();
                }
            }

            grandGross += deptGross;
            grandNet += deptNet;

            sb.append(String.format("%-6d | %-22s | %-10d | %17.2f | %15.2f\n",
                    dept.getId(), dept.getName(), count, deptGross, deptNet));
        }

        sb.append("------------------------------------------------------------------------------\n");
        sb.append(String.format("%-44s | %17.2f | %15.2f\n", "ИТОГО ПО КОМПАНИИ:", grandGross, grandNet));
        return sb.toString();
    }

    // ОТЧЁТ 2: Статистика по стажу работы (группировка по категориям стажа)
    public String buildExperienceReport(Employee[] employees) {
        int juniorCount = 0; // < 2 лет
        int middleCount = 0; // 2 - 5 лет
        int seniorCount = 0; // > 5 лет
        int totalYears = 0;
        int activeCount = 0;

        for (int i = 0; i < employees.length; i++) {
            Employee e = employees[i];
            if (e.getStatus() == EmployeeStatus.DISMISSED) continue;

            activeCount++;
            int exp = e.getExperienceYears();
            totalYears += exp;

            if (exp < 2) {
                juniorCount++;
            } else if (exp <= 5) {
                middleCount++;
            } else {
                seniorCount++;
            }
        }

        double avgExp = activeCount == 0 ? 0.0 : (double) totalYears / activeCount;

        StringBuilder sb = new StringBuilder();
        sb.append("\n=== ОТЧЁТ №2: АНАЛИЗ СТАЖА СОТРУДНИКОВ ===\n");
        sb.append(String.format("%-30s | %-12s | %-10s\n", "Категория стажа", "Количество", "Доля (%)"));
        sb.append("----------------------------------------------------------\n");
        sb.append(String.format("%-30s | %-12d | %9.1f%%\n", "Новички (до 2 лет)", juniorCount,
                activeCount == 0 ? 0 : (juniorCount * 100.0 / activeCount)));
        sb.append(String.format("%-30s | %-12d | %9.1f%%\n", "Опытные (от 2 до 5 лет)", middleCount,
                activeCount == 0 ? 0 : (middleCount * 100.0 / activeCount)));
        sb.append(String.format("%-30s | %-12d | %9.1f%%\n", "Ветераны (более 5 лет)", seniorCount,
                activeCount == 0 ? 0 : (seniorCount * 100.0 / activeCount)));
        sb.append("----------------------------------------------------------\n");
        sb.append(String.format("Всего активных сотрудников: %d | Средний стаж: %.1f лет\n", activeCount, avgExp));
        return sb.toString();
    }

    // ОТЧЁТ 3: Рейтинг сотрудников по зарплате с информацией о премиях (полиморфизм + интерфейс BonusEligible)
    public String buildSalaryRankingReport(Employee[] employees) {
        Employee[] sorted = sortEmployees(employees, new SalaryComparator());
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== ОТЧЁТ №3: РЕЙТИНГ СОТРУДНИКОВ ПО ЗАРПЛАТЕ ===\n");
        sb.append(String.format("%-5s | %-4s | %-18s | %-20s | %-12s | %-12s\n",
                "Место", "ID", "Имя", "Должность", "Премия", "На руки"));
        sb.append("------------------------------------------------------------------------------------\n");

        for (int i = 0; i < sorted.length; i++) {
            Employee e = sorted[i];
            double bonus = 0.0;
            if (e instanceof BonusEligible) {
                bonus = ((BonusEligible) e).calculateBonus();
            }
            sb.append(String.format("%-5d | %-4d | %-18s | %-20s | %12.2f | %12.2f\n",
                    (i + 1), e.getId(), e.getName(), e.getPosition().getTitle(), bonus, e.calculateNetSalary()));
        }
        return sb.toString();
    }
}
