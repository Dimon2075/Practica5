package com.example.lib.service;

import com.example.lib.model.Employee;
import com.example.lib.model.EmployeeComparator;

public class ExperienceComparator implements EmployeeComparator {
    @Override
    public int compare(Employee e1, Employee e2) {
        return Integer.compare(e2.getExperienceYears(), e1.getExperienceYears()); // по убыванию
    }

    @Override
    public String getSortCriteriaName() {
        return "По стажу работы (от большего к меньшему)";
    }
}
