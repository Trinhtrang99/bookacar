package com.example.bookacar.driver.model;

public class UserBook {
    private String id;
    private String name;
    private String locationStart;
    private String locationEnd;
    private String phoneNumber;
    private String totalMoney;
    private String typeBook;

    public UserBook(String id, String name, String locationStart, String locationEnd, String phoneNumber, String totalMoney) {
        this.id = id;
        this.name = name;
        this.locationStart = locationStart;
        this.locationEnd = locationEnd;
        this.phoneNumber = phoneNumber;
        this.totalMoney = totalMoney;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocationStart() {
        return locationStart;
    }

    public void setLocationStart(String locationStart) {
        this.locationStart = locationStart;
    }

    public String getLocationEnd() {
        return locationEnd;
    }

    public void setLocationEnd(String locationEnd) {
        this.locationEnd = locationEnd;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeBook() {
        return typeBook;
    }

    public void setTypeBook(String typeBook) {
        this.typeBook = typeBook;
    }
}
