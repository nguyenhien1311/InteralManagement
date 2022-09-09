package com.example.demo.constant;

public enum Reasons {
    PERSONAL("Bận việc cá nhân"),
    TIRED("Bị mệt"),
    PREGNANT("Có bầu"),
    CHILD("Con nhỏ"),
    MOTO_BROKEN("Hỏng xe"),
    OTHER("Other"),
    OT_LATE("OT muộn"),
    TRAFFIC_JAM("Tắc đường");

    private final String title;

    Reasons(String s) {
        title = s;
    }


    public String getTitle() {
        return title;
    }

}
