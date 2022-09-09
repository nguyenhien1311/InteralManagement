package com.example.demo.constant;

public enum LateSoonType {
    GO_LATE("Đi muộn"), LEAVE_SOON("Về sớm");

    private final String title;

    public String getTitle() {
        return title;
    }

    LateSoonType(String title) {
        this.title = title;
    }
}
