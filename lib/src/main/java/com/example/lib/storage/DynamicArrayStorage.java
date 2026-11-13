package com.example.lib.storage;

import java.util.Arrays;

public class DynamicArrayStorage {
    private Object[] items;
    private int size;

    public DynamicArrayStorage() {
        this.items = new Object[4];
        this.size = 0;
    }

    public DynamicArrayStorage(int initialCapacity) {
        if (initialCapacity <= 0) {
            initialCapacity = 4;
        }
        this.items = new Object[initialCapacity];
        this.size = 0;
    }

    private void ensureCapacity() {
        if (size == items.length) {
            items = Arrays.copyOf(items, items.length * 2);
        }
    }

    public void add(Object item) {
        if (item == null) {
            throw new IllegalArgumentException("Нельзя добавить null в хранилище.");
        }
        ensureCapacity();
        items[size++] = item;
    }

    public Object get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс вне границ хранилища: " + index);
        }
        return items[index];
    }

    public void set(int index, Object item) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс вне границ хранилища: " + index);
        }
        if (item == null) {
            throw new IllegalArgumentException("Элемент не может быть null.");
        }
        items[index] = item;
    }

    public void removeAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс вне границ хранилища: " + index);
        }
        for (int i = index; i < size - 1; i++) {
            items[i] = items[i + 1];
        }
        items[size - 1] = null;
        size--;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            items[i] = null;
        }
        size = 0;
    }

    public Object[] toArray() {
        return Arrays.copyOf(items, size);
    }
}
