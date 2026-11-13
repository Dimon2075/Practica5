package com.example.lib.service;

import com.example.lib.exception.InvalidDataException;
import com.example.lib.exception.VacationConflictException;
import com.example.lib.model.*;
import com.example.lib.storage.DynamicArrayStorage;
import com.example.lib.storage.FileStorage;
import com.example.lib.storage.Logger;

import java.time.LocalDate;

public class CompanyService {
    private final DynamicArrayStorage employeeStorage;
    private final DynamicArrayStorage departmentStorage;
    private final DynamicArrayStorage timesheetStorage;
    private final DynamicArrayStorage vacationStorage;
    private final Logger logger;
    private final FileStorage fileStorage;
    private final PayrollService payrollService;

    public CompanyService() {
        this.employeeStorage = new DynamicArrayStorage(16);
        this.departmentStorage = new DynamicArrayStorage(8);
        this.timesheetStorage = new DynamicArrayStorage(16);
        this.vacationStorage = new DynamicArrayStorage(16);
        this.logger = new Logger();
        this.fileStorage = new FileStorage(logger);
        this.payrollService = new PayrollService();
        initDemoData();
    }

    // Инициализация демонстрационных данных (4 отдела и 16 сотрудников)
    private void initDemoData() {
        try {
            addDepartment(new Department(1, "Руководство", 1));
            addDepartment(new Department(2, "Разработка ПО", 2));
            addDepartment(new Department(3, "Отдел продаж", 3));
            addDepartment(new Department(4, "Аналитика и QA", 4));

            // 1. Генеральный директор (managerId = 0)
            addEmployee(new Manager(1, "Соколов Алексей", 250000, 1, 0, 10, 80000));

            // 2-4. Руководители отделов (подчиняются ID 1)
            addEmployee(new Manager(2, "Морозова Елена", 180000, 2, 1, 7, 50000));
            addEmployee(new Manager(3, "Волков Дмитрий", 170000, 3, 1, 6, 45000));
            addEmployee(new Manager(4, "Лебедева Ольга", 165000, 4, 1, 5, 40000));

            // 5-9. Разработчики (подчиняются ID 2 и ID 4)
            addEmployee(new Developer(5, "Петров Иван", 130000, 2, 2, 4, 12, 5));
            addEmployee(new Developer(6, "Смирнова Анна", 140000, 2, 2, 5, 15, 2));
            addEmployee(new Developer(7, "Кузнецов Максим", 115000, 2, 2, 2, 8, 10));
            addEmployee(new Developer(8, "Новикова Дарья", 125000, 4, 4, 3, 10, 0));
            addEmployee(new Developer(9, "Федоров Артем", 110000, 4, 4, 2, 7, 4));

            // 10-13. Менеджеры по продажам (подчиняются ID 3)
            addEmployee(new Salesman(10, "Козлов Сергей", 90000, 3, 3, 4, 1200000, 0.05));
            addEmployee(new Salesman(11, "Попова Мария", 95000, 3, 3, 5, 1500000, 0.06));
            addEmployee(new Salesman(12, "Васильев Никита", 85000, 3, 3, 2, 800000, 0.04));
            addEmployee(new Salesman(13, "Зайцева Виктория", 88000, 3, 3, 3, 950000, 0.05));

            // 14-16. Стажёры (подчиняются разным руководителям)
            addEmployee(new Intern(14, "Орлов Денис", 45000, 2, 2, 0, 5));
            addEmployee(new Intern(15, "Белова Ксения", 42000, 3, 3, 1, 4));
            addEmployee(new Intern(16, "Гусев Роман", 40000, 4, 4, 0, 3));

            // Демо-записи в табель и отпуска
            addTimesheet(new Timesheet(5, LocalDate.now().minusDays(1), 8, 2));
            addTimesheet(new Timesheet(10, LocalDate.now().minusDays(1), 8, 0));
            requestVacation(1, 11, LocalDate.now().plusDays(5), LocalDate.now().plusDays(19));

            logger.info("Демонстрационные данные успешно загружены (16 сотрудников, 4 отдела).");
        } catch (Exception e) {
            logger.error("Ошибка инициализации демо-данных: " + e.getMessage());
        }
    }

    public void addDepartment(Department dept) throws InvalidDataException {
        if (findDepartmentById(dept.getId()) != null) {
            throw new InvalidDataException("Отдел с ID " + dept.getId() + " уже существует.");
        }
        departmentStorage.add(dept);
        logger.info("Добавлен отдел: " + dept.getName());
    }

    public Department findDepartmentById(int id) {
        for (int i = 0; i < departmentStorage.size(); i++) {
            Department d = (Department) departmentStorage.get(i);
            if (d.getId() == id) {
                return d;
            }
        }
        return null;
    }

    public Department[] getAllDepartments() {
        Department[] result = new Department[departmentStorage.size()];
        for (int i = 0; i < departmentStorage.size(); i++) {
            result[i] = (Department) departmentStorage.get(i);
        }
        return result;
    }

    public void addEmployee(Employee emp) throws InvalidDataException {
        if (emp == null) {
            throw new InvalidDataException("Сотрудник не может быть null.");
        }
        if (findEmployeeById(emp.getId()) != null) {
            throw new InvalidDataException("Сотрудник с ID " + emp.getId() + " уже существует.");
        }
        if (findDepartmentById(emp.getDepartmentId()) == null) {
            throw new InvalidDataException("Отдел с ID " + emp.getDepartmentId() + " не найден.");
        }
        if (emp.getManagerId() != 0 && findEmployeeById(emp.getManagerId()) == null) {
            throw new InvalidDataException("Руководитель с ID " + emp.getManagerId() + " не найден.");
        }
        employeeStorage.add(emp);
        logger.info("Принят на работу сотрудник: " + emp.getName() + " (ID: " + emp.getId() + ")");
    }

    public Employee findEmployeeById(int id) {
        for (int i = 0; i < employeeStorage.size(); i++) {
            Employee e = (Employee) employeeStorage.get(i);
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    public Employee[] getAllEmployees() {
        Employee[] result = new Employee[employeeStorage.size()];
        for (int i = 0; i < employeeStorage.size(); i++) {
            result[i] = (Employee) employeeStorage.get(i);
        }
        return result;
    }

    public void updateEmployee(int id, String newName, double newBaseSalary) throws InvalidDataException {
        Employee emp = findEmployeeById(id);
        if (emp == null) {
            throw new InvalidDataException("Сотрудник с ID " + id + " не найден.");
        }
        emp.setName(newName);
        emp.setBaseSalary(newBaseSalary);
        logger.info("Обновлены данные сотрудника ID " + id);
    }

    public void transferEmployee(int empId, int newDepartmentId, int newManagerId) throws InvalidDataException {
        Employee emp = findEmployeeById(empId);
        if (emp == null) {
            throw new InvalidDataException("Сотрудник с ID " + empId + " не найден.");
        }
        if (findDepartmentById(newDepartmentId) == null) {
            throw new InvalidDataException("Целевой отдел с ID " + newDepartmentId + " не существует.");
        }
        if (newManagerId != 0 && findEmployeeById(newManagerId) == null) {
            throw new InvalidDataException("Новый руководитель с ID " + newManagerId + " не найден.");
        }
        if (newManagerId == empId) {
            throw new InvalidDataException("Сотрудник не может быть руководителем самому себе.");
        }
        emp.setDepartmentId(newDepartmentId);
        emp.setManagerId(newManagerId);
        logger.info("Сотрудник ID " + empId + " переведен в отдел #" + newDepartmentId);
    }

    public void dismissEmployee(int id) throws InvalidDataException {
        Employee emp = findEmployeeById(id);
        if (emp == null) {
            throw new InvalidDataException("Сотрудник с ID " + id + " не найден.");
        }
        emp.setStatus(EmployeeStatus.DISMISSED);
        logger.info("Сотрудник уволен: " + emp.getName() + " (ID: " + id + ")");
    }

    public void deleteEmployeePermanently(int id) throws InvalidDataException {
        for (int i = 0; i < employeeStorage.size(); i++) {
            Employee e = (Employee) employeeStorage.get(i);
            if (e.getId() == id) {
                employeeStorage.removeAt(i);
                logger.info("Запись о сотруднике ID " + id + " удалена из базы.");
                return;
            }
        }
        throw new InvalidDataException("Сотрудник с ID " + id + " не найден для удаления.");
    }

    public void promoteEmployee(int id, double newBaseSalary, double param1, double param2) throws InvalidDataException {
        int index = -1;
        Employee emp = null;
        for (int i = 0; i < employeeStorage.size(); i++) {
            Employee current = (Employee) employeeStorage.get(i);
            if (current.getId() == id) {
                index = i;
                emp = current;
                break;
            }
        }
        if (emp == null) {
            throw new InvalidDataException("Сотрудник с ID " + id + " не найден.");
        }

        Employee promoted;
        if (emp.getPosition() == Position.INTERN) {
            promoted = new Developer(emp.getId(), emp.getName(), newBaseSalary,
                    emp.getDepartmentId(), emp.getManagerId(), emp.getExperienceYears(), (int) param1, (int) param2);
        } else if (emp.getPosition() == Position.DEVELOPER || emp.getPosition() == Position.SALESMAN) {
            promoted = new Manager(emp.getId(), emp.getName(), newBaseSalary,
                    emp.getDepartmentId(), emp.getManagerId(), emp.getExperienceYears(), param1);
        } else {
            throw new InvalidDataException("Руководителя (Manager) нельзя повысить выше.");
        }

        promoted.setStatus(emp.getStatus());
        employeeStorage.set(index, promoted);
        logger.info("Сотрудник " + emp.getName() + " повышен до " + promoted.getPosition().getTitle());
    }

    // Табель рабочего времени
    public void addTimesheet(Timesheet record) throws InvalidDataException {
        Employee emp = findEmployeeById(record.getEmployeeId());
        if (emp == null) {
            throw new InvalidDataException("Сотрудник с ID " + record.getEmployeeId() + " не найден.");
        }
        timesheetStorage.add(record);
        if (emp instanceof Developer && record.getOvertimeHours() > 0) {
            Developer dev = (Developer) emp;
            dev.setOvertimeHours(dev.getOvertimeHours() + record.getOvertimeHours());
        }
        logger.info("Добавлена запись в табель для ID " + record.getEmployeeId() + " за " + record.getDate());
    }
    public Timesheet[] getAllTimesheets() {
        Timesheet[] result = new Timesheet[timesheetStorage.size()];
        for (int i = 0; i < timesheetStorage.size(); i++) {
            result[i] = (Timesheet) timesheetStorage.get(i);
        }
        return result;
    }
    // Оформление отпуска с правилом: одновременно в отпуске не более 30% сотрудников отдела
    public void requestVacation(int requestId, int employeeId, LocalDate start, LocalDate end) throws InvalidDataException {
        Employee emp = findEmployeeById(employeeId);
        if (emp == null) {
            throw new InvalidDataException("Сотрудник с ID " + employeeId + " не найден.");
        }
        if (emp.getStatus() == EmployeeStatus.DISMISSED) {
            throw new InvalidDataException("Уволенный сотрудник не может оформить отпуск.");
        }
        int deptId = emp.getDepartmentId();
        int totalInDept = 0;
        for (int i = 0; i < employeeStorage.size(); i++) {
            Employee current = (Employee) employeeStorage.get(i);
            if (current.getDepartmentId() == deptId && current.getStatus() != EmployeeStatus.DISMISSED) {
                totalInDept++;
            }
        }
// Проверяем каждый день запрашиваемого периода
        LocalDate day = start;
        while (!day.isAfter(end)) {
            int onVacationCount = 1; // учитываем текущего сотрудника
            for (int i = 0; i < vacationStorage.size(); i++) {
                VacationRequest vr = (VacationRequest) vacationStorage.get(i);
                if (vr.isApproved() && vr.overlapsWith(day)) {
                    Employee other = findEmployeeById(vr.getEmployeeId());
                    if (other != null && other.getDepartmentId() == deptId) {
                        onVacationCount++;
                    }
                }
            }
            double ratio = (double) onVacationCount / totalInDept;
            if (ratio > 0.30) {
                logger.warn("Отклонен отпуск для ID " + employeeId + ": превышен лимит 30% в отделе #" + deptId + " на дату " + day);
                throw new VacationConflictException(String.format(
                        "Конфликт отпусков! На дату %s в отделе #%d в отпуске окажется %.0f%% сотрудников (максимум 30%%).",
                        day, deptId, ratio * 100));
            }
            day = day.plusDays(1);
        }
        VacationRequest request = new VacationRequest(requestId, employeeId, start, end, true);
        vacationStorage.add(request);
        if (!start.isAfter(LocalDate.now()) && !end.isBefore(LocalDate.now())) {
            emp.setStatus(EmployeeStatus.VACATION);
        }
        logger.info("Одобрен отпуск #" + requestId + " для сотрудника ID " + employeeId);
    }
    public VacationRequest[] getAllVacations() {
        VacationRequest[] result = new VacationRequest[vacationStorage.size()];
        for (int i = 0; i < vacationStorage.size(); i++) {
            result[i] = (VacationRequest) vacationStorage.get(i);
        }
        return result;
    }
    // УСЛОЖНЕНИЕ (+10 баллов): Рекурсивный вывод организационной структуры подчиненности деревом
    public String buildOrgChartTree() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== ОРГАНИЗАЦИОННАЯ СТРУКТУРА ПОДЧИНЁННОСТИ ===\n");
        Employee[] all = getAllEmployees();
        boolean hasRoots = false;
        for (int i = 0; i < all.length; i++) {
            if (all[i].getManagerId() == 0) {
                hasRoots = true;
                appendSubordinatesRecursive(all, all[i], 0, sb);
            }
        }
        if (!hasRoots) {
            sb.append("Руководители верхнего уровня (managerId = 0) не найдены.\n");
        }
        return sb.toString();
    }
    private void appendSubordinatesRecursive(Employee[] all, Employee current, int level, StringBuilder sb) {
        for (int i = 0; i < level; i++) {
            sb.append("    ");
        }
        String prefix = level == 0 ? "[ТОП] " : "└── ";
        sb.append(prefix)
                .append(current.getName())
                .append(" (ID: ").append(current.getId())
                .append(", ").append(current.getPosition().getTitle())
                .append(", Отдел #").append(current.getDepartmentId())
                .append(")\n");
        for (int i = 0; i < all.length; i++) {
            if (all[i].getManagerId() == current.getId()) {
                appendSubordinatesRecursive(all, all[i], level + 1, sb);
            }
        }
    }
    // Работа с CSV-файлами
    public void saveToFile(String filePath) throws InvalidDataException {
        fileStorage.saveEmployeesToCsv(filePath, getAllEmployees());
    }
    public void loadFromFile(String filePath) throws InvalidDataException {
        DynamicArrayStorage loaded = fileStorage.loadEmployeesFromCsv(filePath);
        employeeStorage.clear();
        for (int i = 0; i < loaded.size(); i++) {
            employeeStorage.add(loaded.get(i));
        }
    }
    public PayrollService getPayrollService() {
        return payrollService;
    }
    public Logger getLogger() {
        return logger;
    }
}