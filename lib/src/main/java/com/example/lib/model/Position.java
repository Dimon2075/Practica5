package com.example.lib.model;

public enum Position {
    INTERN("Стажёр", 1, 1.0),
    DEVELOPER("Разработчик", 2, 1.15),
    SALESMAN("Менеджер по продажам", 2, 1.10),
    MANAGER("Руководитель", 3, 1.30);

    private final String title;
    private final int gradeLevel;
    private final double baseCoefficient;

    Position(String title, int gradeLevel, double baseCoefficient) {
        this.title = title;
        this.gradeLevel = gradeLevel;
        this.baseCoefficient = baseCoefficient;
    }

    public String getTitle() {
        return title;
    }

    public int getGradeLevel() {
        return gradeLevel;
    }

    public double getBaseCoefficient() {
        return baseCoefficient;
    }

    public boolean canPromoteTo(Position target) {
        return target != null && target.gradeLevel == this.gradeLevel + 1;
    }
}
