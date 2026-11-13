package com.example.lib.storage;

import com.example.lib.exception.InvalidDataException;
import com.example.lib.model.*;

import java.io.*;

public class FileStorage {
    private static final String DELIMITER = ";";
    private final Logger logger;

    public FileStorage(Logger logger) {
        this.logger = logger;
    }

    public void saveEmployeesToCsv(String filePath, Employee[] employees) throws InvalidDataException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("ID;POSITION;NAME;BASE_SALARY;STATUS;DEPARTMENT_ID;MANAGER_ID;EXPERIENCE;SPECIFIC_DATA");
            for (int i = 0; i < employees.length; i++) {
                Employee e = employees[i];
                String line = String.join(DELIMITER,
                        String.valueOf(e.getId()),
                        e.getPosition().name(),
                        e.getName().replace(DELIMITER, ","),
                        String.valueOf(e.getBaseSalary()),
                        e.getStatus().name(),
                        String.valueOf(e.getDepartmentId()),
                        String.valueOf(e.getManagerId()),
                        String.valueOf(e.getExperienceYears()),
                        e.getSpecificDataForCsv()
                );
                writer.println(line);
            }
            logger.info("Сохранено сотрудников в файл " + filePath + ": " + employees.length);
        } catch (IOException e) {
            logger.error("Ошибка записи в файл " + filePath + ": " + e.getMessage());
            throw new InvalidDataException("Ошибка сохранения данных в файл: " + e.getMessage());
        }
    }

    public DynamicArrayStorage loadEmployeesFromCsv(String filePath) throws InvalidDataException {
        File file = new File(filePath);
        if (!file.exists()) {
            logger.warn("Файл не найден: " + filePath);
            throw new InvalidDataException("Файл не найден: " + filePath);
        }

        DynamicArrayStorage loaded = new DynamicArrayStorage(16);
        int lineNumber = 0;
        int corruptedCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1 && line.startsWith("ID;")) {
                    continue; // пропускаем заголовок таблицы
                }
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    Employee emp = parseEmployeeLine(line);
                    loaded.add(emp);
                } catch (Exception ex) {
                    corruptedCount++;
                    logger.warn("Повреждённая строка #" + lineNumber + " в файле " + filePath + ": " + ex.getMessage());
                }
            }
            logger.info("Загружено сотрудников из " + filePath + ": " + loaded.size() + " (пропущено битых строк: " + corruptedCount + ")");
        } catch (IOException e) {
            logger.error("Ошибка чтения файла " + filePath + ": " + e.getMessage());
            throw new InvalidDataException("Ошибка чтения файла: " + e.getMessage());
        }

        return loaded;
    }

    private Employee parseEmployeeLine(String line) throws InvalidDataException {
        String[] parts = line.split(DELIMITER);
        if (parts.length < 9) {
            throw new InvalidDataException("Недостаточно столбцов (ожидалось 9, получено " + parts.length + ")");
        }

        int id = Integer.parseInt(parts[0].trim());
        Position position = Position.valueOf(parts[1].trim());
        String name = parts[2].trim();
        double baseSalary = Double.parseDouble(parts[3].trim());
        EmployeeStatus status = EmployeeStatus.valueOf(parts[4].trim());
        int departmentId = Integer.parseInt(parts[5].trim());
        int managerId = Integer.parseInt(parts[6].trim());
        int experience = Integer.parseInt(parts[7].trim());
        String specific = parts[8].trim();

        Employee emp;
        switch (position) {
            case INTERN:
                int mentorScore = Integer.parseInt(specific);
                emp = new Intern(id, name, baseSalary, departmentId, managerId, experience, mentorScore);
                break;
            case DEVELOPER:
                String[] devParts = specific.split(":");
                if (devParts.length < 2) {
                    throw new InvalidDataException("Неверный формат данных разработчика");
                }
                int tasks = Integer.parseInt(devParts[0]);
                int overtime = Integer.parseInt(devParts[1]);
                emp = new Developer(id, name, baseSalary, departmentId, managerId, experience, tasks, overtime);
                break;
            case SALESMAN:
                String[] salesParts = specific.split(":");
                if (salesParts.length < 2) {
                    throw new InvalidDataException("Неверный формат данных менеджера по продажам");
                }
                double salesVolume = Double.parseDouble(salesParts[0]);
                double rate = Double.parseDouble(salesParts[1]);
                emp = new Salesman(id, name, baseSalary, departmentId, managerId, experience, salesVolume, rate);
                break;
            case MANAGER:
                double bonus = Double.parseDouble(specific);
                emp = new Manager(id, name, baseSalary, departmentId, managerId, experience, bonus);
                break;
            default:
                throw new InvalidDataException("Неизвестная должность: " + position);
        }

        emp.setStatus(status);
        return emp;
    }
}
