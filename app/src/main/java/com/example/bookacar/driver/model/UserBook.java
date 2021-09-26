package com.example.bookacar.driver.model;

public class UserBook {
    private String id;
    private String locationStart;
    private String locationEnd;
    private String phoneNumber;

    public UserBook(String id, String locationStart, String locationEnd, String phoneNumber) {
        this.id = id;
        this.locationStart = locationStart;
        this.locationEnd = locationEnd;
        this.phoneNumber = phoneNumber;
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
}
