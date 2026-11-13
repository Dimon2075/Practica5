package com.example.lib.model;

public interface EmployeeComparator {
    int compare(Employee e1, Employee e2);
    String getSortCriteriaName();
}
