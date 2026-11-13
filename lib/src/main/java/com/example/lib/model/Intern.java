package com.example.lib.model;

public class Intern extends Employee {
    private int mentorScore; // оценка от 1 до 5

    public Intern(int id, String name, double baseSalary, int departmentId,
                  int managerId, int experienceYears, int mentorScore) {
        super(id, name, baseSalary, Position.INTERN, departmentId, managerId, experienceYears);
        setMentorScore(mentorScore);
    }

    public int getMentorScore() {
        return mentorScore;
    }

    public void setMentorScore(int mentorScore) {
        if (mentorScore < 1 || mentorScore > 5) {
            throw new IllegalArgumentException("Оценка ментора должна быть от 1 до 5.");
        }
        this.mentorScore = mentorScore;
    }

    @Override
    public double calculateGrossSalary() {
        return getBaseSalary() * (mentorScore * 0.2);
    }

    @Override
    public String getSpecificDataForCsv() {
        return String.valueOf(mentorScore);
    }

    @Override
    public String toString() {
        return super.toString() + " [Оценка ментора: " + mentorScore + "/5]";
    }
}
