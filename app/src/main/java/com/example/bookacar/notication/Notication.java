package com.example.bookacar.notication;

public class Notication {
    private String detail;
    private String time;

    public Notication(String detail, String time) {
        this.detail = detail;
        this.time = time;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
