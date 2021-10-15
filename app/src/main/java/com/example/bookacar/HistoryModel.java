package com.example.bookacar;

public class HistoryModel {
    String date;
    String descrip;
    String cost;

    public String getDate() {
        return date;
    }

    public String getDescrip() {
        return descrip;
    }

    public String getCost() {
        return cost;
    }

    public HistoryModel(String date, String descrip, String cost) {
        this.date = date;
        this.descrip = descrip;
        this.cost = cost;
    }
}
