package com.example.lib.service;

import com.example.lib.model.Employee;
import com.example.lib.model.EmployeeComparator;

public class SalaryComparator implements EmployeeComparator {
    @Override
    public int compare(Employee e1, Employee e2) {
        return Double.compare(e2.calculateNetSalary(), e1.calculateNetSalary()); // по убыванию
    }

    @Override
    public String getSortCriteriaName() {
        return "По итоговой зарплате (от большей к меньшей)";
    }
}
