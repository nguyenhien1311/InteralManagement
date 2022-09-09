package com.example.demo.constant;

public enum OffType {
    NO_SALARY("Nghỉ không lương"), BY_YEAR("Nghỉ phép năm");

    private final String title;

    public String getTitle() {
        return title;
    }

    OffType(String s) {
        title = s;
    }
}
