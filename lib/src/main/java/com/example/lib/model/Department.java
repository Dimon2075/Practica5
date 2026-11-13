package com.example.lib.model;

import java.util.Objects;

public class Department {
    private int id;
    private String name;
    private int headId; // ID руководителя отдела

    public Department(int id, String name, int headId) {
        setId(id);
        setName(name);
        setHeadId(headId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID отдела должен быть положительным.");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название отдела не может быть пустым.");
        }
        this.name = name.trim();
    }

    public int getHeadId() {
        return headId;
    }

    public void setHeadId(int headId) {
        if (headId < 0) {
            throw new IllegalArgumentException("ID руководителя не может быть отрицательным.");
        }
        this.headId = headId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Отдел #%d: %s (ID руководителя: %d)", id, name, headId);
    }
}
