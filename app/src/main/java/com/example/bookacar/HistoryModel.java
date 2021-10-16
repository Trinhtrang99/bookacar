package com.example.bookacar;

public class HistoryModel {
    String date;
    String locationStart;
    String locationEnd;
    String cost;

    public HistoryModel(String date, String locationStart, String locationEnd, String cost) {
        this.date = date;
        this.locationStart = locationStart;
        this.locationEnd = locationEnd;
        this.cost = cost;
    }
}
