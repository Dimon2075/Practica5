package com.example.lib.ui;

import com.example.lib.exception.InvalidDataException;
import com.example.lib.exception.InvalidSalaryException;
import com.example.lib.exception.VacationConflictException;
import com.example.lib.model.*;
import com.example.lib.service.CompanyService;
import com.example.lib.service.ExperienceComparator;
import com.example.lib.service.SalaryComparator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleMenu {
    private final CompanyService companyService;
    private final Scanner scanner;

    public ConsoleMenu(CompanyService companyService) {
        this.companyService = companyService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n==========================================");
            System.out.println("   СИСТЕМА УПРАВЛЕНИЯ КАДРАМИ И ЗАРПЛАТОЙ   ");
            System.out.println("==========================================");
            System.out.println("1. Управление сотрудниками (подменю)");
            System.out.println("2. Табель, отпуска и структура (подменю)");
            System.out.println("3. Зарплата и аналитические отчёты (подменю)");
            System.out.println("4. Файлы и журнал действий (подменю)");
            System.out.println("0. Выход");
            System.out.print("Выберите раздел: ");

            int choice = readInt();
            switch (choice) {
                case 1:
                    employeesSubmenu();
                    break;
                case 2:
                    hrSubmenu();
                    break;
                case 3:
                    reportsSubmenu();
                    break;
                case 4:
                    filesAndLogsSubmenu();
                    break;
                case 0:
                    System.out.println("Работа программы завершена.");
                    return;
                default:
                    System.out.println("Неверный пункт меню. Повторите ввод.");
            }
        }
    }

    // ПОДМЕНЮ 1: Сотрудники
    private void employeesSubmenu() {
        while (true) {
            System.out.println("\n--- ПОДМЕНЮ: СОТРУДНИКИ ---");
            System.out.println("1. Показать всех сотрудников");
            System.out.println("2. Сортировать список сотрудников (по ЗП / по стажу)");
            System.out.println("3. Принять нового сотрудника");
            System.out.println("4. Найти сотрудника по ID");
            System.out.println("5. Изменить имя и базовую ставку");
            System.out.println("6. Перевести в другой отдел");
            System.out.println("7. Повысить в должности");
            System.out.println("8. Уволить сотрудника");
            System.out.println("0. Назад в главное меню");
            System.out.print("Ваш выбор: ");

            int choice = readInt();
            try {
                switch (choice) {
                    case 1:
                        printEmployees(companyService.getAllEmployees());
                        break;
                    case 2:
                        sortEmployeesMenu();
                        break;
                    case 3:
                        addEmployeeMenu();
                        break;
                    case 4:
                        findEmployeeMenu();
                        break;
                    case 5:
                        updateEmployeeMenu();
                        break;
                    case 6:
                        transferEmployeeMenu();
                        break;
                    case 7:
                        promoteEmployeeMenu();
                        break;
                    case 8:
                        dismissEmployeeMenu();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Неверный пункт подменю.");
                }
            } catch (InvalidDataException | InvalidSalaryException | IllegalArgumentException e) {
                System.out.println("[ОШИБКА]: " + e.getMessage());
            }
        }
    }

    // ПОДМЕНЮ 2: Табель, отпуска, отделы и дерево подчиненности
    private void hrSubmenu() {
        while (true) {
            System.out.println("\n--- ПОДМЕНЮ: ТАБЕЛЬ, ОТПУСКА И СТРУКТУРА ---");
            System.out.println("1. Показать все отделы");
            System.out.println("2. Добавить запись в табель");
            System.out.println("3. Показать журнал табеля");
            System.out.println("4. Оформить отпуск (контроль лимита 30% отдела)");
            System.out.println("5. Показать график отпусков");
            System.out.println("6. Вывести дерево подчинённости (рекурсия)");
            System.out.println("0. Назад в главное меню");
            System.out.print("Ваш выбор: ");

            int choice = readInt();
            try {
                switch (choice) {
                    case 1:
                        Department[] depts = companyService.getAllDepartments();
                        for (int i = 0; i < depts.length; i++) {
                            System.out.println(depts[i]);
                        }
                        break;
                    case 2:
                        addTimesheetMenu();
                        break;
                    case 3:
                        Timesheet[] sheets = companyService.getAllTimesheets();
                        if (sheets.length == 0) System.out.println("Табель пуст.");
                        for (int i = 0; i < sheets.length; i++) {
                            System.out.println(sheets[i]);
                        }
                        break;
                    case 4:
                        requestVacationMenu();
                        break;
                    case 5:
                        VacationRequest[] vacations = companyService.getAllVacations();
                        if (vacations.length == 0) System.out.println("Записей об отпусках нет.");
                        for (int i = 0; i < vacations.length; i++) {
                            System.out.println(vacations[i]);
                        }
                        break;
                    case 6:
                        System.out.println(companyService.buildOrgChartTree());
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Неверный пункт подменю.");
                }
            } catch (InvalidDataException | VacationConflictException | IllegalArgumentException e) {
                System.out.println("[ОШИБКА]: " + e.getMessage());
            }
        }
    }

    // ПОДМЕНЮ 3: Зарплата и аналитика
    private void reportsSubmenu() {
        while (true) {
            System.out.println("\n--- ПОДМЕНЮ: ЗАРПЛАТА И ОТЧЁТЫ ---");
            System.out.println("1. Сформировать расчётный лист (Payslip) по ID");
            System.out.println("2. Сформировать расчётные листы для всех сотрудников");
            System.out.println("3. Отчёт №1: Фонд оплаты труда по отделам");
            System.out.println("4. Отчёт №2: Анализ стажа сотрудников");
            System.out.println("5. Отчёт №3: Рейтинг сотрудников по зарплате");
            System.out.println("0. Назад в главное меню");
            System.out.print("Ваш выбор: ");

            int choice = readInt();
            switch (choice) {
                case 1:
                    System.out.print("Введите ID сотрудника: ");
                    int id = readInt();
                    Employee emp = companyService.findEmployeeById(id);
                    if (emp == null) {
                        System.out.println("Сотрудник не найден.");
                    } else {
                        System.out.println(companyService.getPayrollService().generatePayslip(emp));
                    }
                    break;
                case 2:
                    Employee[] all = companyService.getAllEmployees();
                    for (int i = 0; i < all.length; i++) {
                        System.out.println(companyService.getPayrollService().generatePayslip(all[i]));
                    }
                    System.out.printf("Общий фонд к выплате (Net): %.2f\n",
                            companyService.getPayrollService().calculateTotalNetPayroll(all));
                    break;
                case 3:
                    System.out.println(companyService.getPayrollService().buildDepartmentPayrollReport(
                            companyService.getAllEmployees(), companyService.getAllDepartments()));
                    break;
                case 4:
                    System.out.println(companyService.getPayrollService().buildExperienceReport(
                            companyService.getAllEmployees()));
                    break;
                case 5:
                    System.out.println(companyService.getPayrollService().buildSalaryRankingReport(
                            companyService.getAllEmployees()));
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Неверный пункт подменю.");
            }
        }
    }

    // ПОДМЕНЮ 4: Файлы и логгер
    private void filesAndLogsSubmenu() {
        while (true) {
            System.out.println("\n--- ПОДМЕНЮ: ФАЙЛЫ И ЖУРНАЛ ---");
            System.out.println("1. Сохранить сотрудников в CSV-файл");
            System.out.println("2. Загрузить сотрудников из CSV-файла");
            System.out.println("3. Показать журнал действий (из памяти)");
            System.out.println("0. Назад в главное меню");
            System.out.print("Ваш выбор: ");

            int choice = readInt();
            try {
                switch (choice) {
                    case 1:
                        System.out.print("Введите имя файла (например, employees.csv): ");
                        String savePath = readNonEmptyString();
                        companyService.saveToFile(savePath);
                        System.out.println("Данные успешно сохранены в " + savePath);
                        break;
                    case 2:
                        System.out.print("Введите имя файла для загрузки (например, employees.csv): ");
                        String loadPath = readNonEmptyString();
                        companyService.loadFromFile(loadPath);
                        System.out.println("Данные успешно загружены из " + loadPath);
                        break;
                    case 3:
                        String[] logs = companyService.getLogger().getAllLogs();
                        for (int i = 0; i < logs.length; i++) {
                            System.out.println(logs[i]);
                        }
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Неверный пункт подменю.");
                }
            } catch (InvalidDataException e) {
                System.out.println("[ОШИБКА ФАЙЛА]: " + e.getMessage());
            }
        }
    }
    private void printEmployees(Employee[] list) {
        if (list.length == 0) {
            System.out.println("Список сотрудников пуст.");
            return;
        }
        for (int i = 0; i < list.length; i++) {
            System.out.println(list[i]);
        }
    }
    private void sortEmployeesMenu() {
        System.out.println("Выберите критерий сортировки: 1 - По итоговой зарплате, 2 - По стажу работы");
        int type = readInt();
        Employee[] all = companyService.getAllEmployees();
        if (type == 1) {
            SalaryComparator comp = new SalaryComparator();
            System.out.println("Сортировка: " + comp.getSortCriteriaName());
            printEmployees(companyService.getPayrollService().sortEmployees(all, comp));
        } else if (type == 2) {
            ExperienceComparator comp = new ExperienceComparator();
            System.out.println("Сортировка: " + comp.getSortCriteriaName());
            printEmployees(companyService.getPayrollService().sortEmployees(all, comp));
        } else {
            System.out.println("Неверный критерий сортировки.");
        }
    }
    private void addEmployeeMenu() throws InvalidDataException {
        System.out.print("Введите ID нового сотрудника: ");
        int id = readInt();
        System.out.print("Введите ФИО: ");
        String name = readNonEmptyString();
        System.out.print("Введите базовую ставку: ");
        double baseSalary = readDouble();
        System.out.print("Введите ID отдела (1-4): ");
        int deptId = readInt();
        System.out.print("Введите ID руководителя (0 если нет): ");
        int managerId = readInt();
        System.out.print("Введите стаж (полных лет): ");
        int exp = readInt();
        System.out.println("Должность: 1 - Стажёр, 2 - Разработчик, 3 - Менеджер по продажам, 4 - Руководитель");
        int pos = readInt();
        Employee emp;
        if (pos == 1) {
            System.out.print("Введите оценку ментора (1-5): ");
            int score = readInt();
            emp = new Intern(id, name, baseSalary, deptId, managerId, exp, score);
        } else if (pos == 2) {
            System.out.print("Введите кол-во выполненных задач: ");
            int tasks = readInt();
            System.out.print("Введите часы переработки: ");
            int overtime = readInt();
            emp = new Developer(id, name, baseSalary, deptId, managerId, exp, tasks, overtime);
        } else if (pos == 3) {
            System.out.print("Введите объём продаж: ");
            double sales = readDouble();
            System.out.print("Введите ставку комиссии (например, 0.05): ");
            double rate = readDouble();
            emp = new Salesman(id, name, baseSalary, deptId, managerId, exp, sales, rate);
        } else if (pos == 4) {
            System.out.print("Введите проектный бонус: ");
            double bonus = readDouble();
            emp = new Manager(id, name, baseSalary, deptId, managerId, exp, bonus);
        } else {
            System.out.println("Неверный код должности.");
            return;
        }
        companyService.addEmployee(emp);
        System.out.println("Сотрудник успешно принят на работу!");
    }
    private void findEmployeeMenu() {
        System.out.print("Введите ID сотрудника для поиска: ");
        int id = readInt();
        Employee emp = companyService.findEmployeeById(id);
        if (emp != null) {
            System.out.println("Найден: " + emp);
        } else {
            System.out.println("Сотрудник с ID " + id + " не найден.");
        }
    }
    private void updateEmployeeMenu() throws InvalidDataException {
        System.out.print("Введите ID сотрудника: ");
        int id = readInt();
        System.out.print("Введите новое ФИО: ");
        String name = readNonEmptyString();
        System.out.print("Введите новую базовую ставку: ");
        double salary = readDouble();
        companyService.updateEmployee(id, name, salary);
        System.out.println("Данные успешно обновлены.");
    }
    private void transferEmployeeMenu() throws InvalidDataException {
        System.out.print("Введите ID сотрудника: ");
        int id = readInt();
        System.out.print("Введите ID нового отдела: ");
        int deptId = readInt();
        System.out.print("Введите ID нового руководителя (0 если нет): ");
        int managerId = readInt();
        companyService.transferEmployee(id, deptId, managerId);
        System.out.println("Сотрудник успешно переведен.");
    }
    private void promoteEmployeeMenu() throws InvalidDataException {
        System.out.print("Введите ID сотрудника для повышения: ");
        int id = readInt();
        Employee emp = companyService.findEmployeeById(id);
        if (emp == null) {
            System.out.println("Сотрудник не найден.");
            return;
        }
        System.out.print("Введите новую базовую ставку: ");
        double newSalary = readDouble();
        if (emp.getPosition() == Position.INTERN) {
            System.out.print("Повышение до Разработчика. Введите стартовое кол-во задач: ");
            int tasks = readInt();
            companyService.promoteEmployee(id, newSalary, tasks, 0);
            System.out.println("Стажёр повышен до Разработчика!");
        } else if (emp.getPosition() == Position.DEVELOPER || emp.getPosition() == Position.SALESMAN) {
            System.out.print("Повышение до Руководителя. Введите проектный бонус: ");
            double bonus = readDouble();
            companyService.promoteEmployee(id, newSalary, bonus, 0);
            System.out.println("Сотрудник повышен до Руководителя!");
        } else {
            System.out.println("Руководителя невозможно повысить выше.");
        }
    }
    private void dismissEmployeeMenu() throws InvalidDataException {
        System.out.print("Введите ID сотрудника для увольнения: ");
        int id = readInt();
        companyService.dismissEmployee(id);
        System.out.println("Сотрудник переведен в статус 'Уволен'.");
    }
    private void addTimesheetMenu() throws InvalidDataException {
        System.out.print("Введите ID сотрудника: ");
        int empId = readInt();
        LocalDate date = readDate("Введите дату (ГГГГ-ММ-ДД): ");
        System.out.print("Введите основные отработанные часы (0-24): ");
        int hours = readInt();
        System.out.print("Введите часы переработки (0-12): ");
        int overtime = readInt();
        companyService.addTimesheet(new Timesheet(empId, date, hours, overtime));
        System.out.println("Запись добавлена в табель.");
    }
    private void requestVacationMenu() throws InvalidDataException {
        System.out.print("Введите номер заявки на отпуск: ");
        int reqId = readInt();
        System.out.print("Введите ID сотрудника: ");
        int empId = readInt();
        LocalDate start = readDate("Введите дату начала отпуска (ГГГГ-ММ-ДД): ");
        LocalDate end = readDate("Введите дату окончания отпуска (ГГГГ-ММ-ДД): ");
        companyService.requestVacation(reqId, empId, start, end);
        System.out.println("Отпуск успешно согласован и оформлен!");
    }
    // Безопасные методы ввода (программа не падает при неверном вводе)
    private int readInt() {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Ошибка! Введите целое число: ");
            }
        }
    }
    private double readDouble() {
        while (true) {
            String input = scanner.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Ошибка! Введите число: ");
            }
        }
    }
    private String readNonEmptyString() {
        while (true) {
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.print("Строка не должна быть пустой. Повторите ввод: ");
        }
    }
    private LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Ошибка формата даты! Используйте ГГГГ-ММ-ДД (например, 2026-11-20).");
            }
        }
    }
}