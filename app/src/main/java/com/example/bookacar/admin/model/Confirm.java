package com.example.bookacar.admin.model;

public class Confirm {
    private String id;
    private String image;
    private String name;
    private String password;
    private String phoneNumber;
    private Boolean isConfirm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getConfirm() {
        return isConfirm;
    }

    public void setConfirm(Boolean confirm) {
        isConfirm = confirm;
    }

    public Confirm(String id, String image, String name, String password, String phoneNumber, Boolean isConfirm) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isConfirm = isConfirm;
    }
}
