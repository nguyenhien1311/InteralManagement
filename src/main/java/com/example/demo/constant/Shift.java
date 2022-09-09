package com.example.demo.constant;

public enum Shift {
    MORNING("Sáng"),AFTER_NOON("Chiều");

    private final String title;

    public String getTitle() {
        return title;
    }

    Shift(String title) {
        this.title = title;
    }
}
