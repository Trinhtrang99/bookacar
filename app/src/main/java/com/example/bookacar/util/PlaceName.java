package com.example.bookacar.util;

public class PlaceName {
    String name;
    Double v;
    Double v2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getV() {
        return v;
    }

    public void setV(Double v) {
        this.v = v;
    }

    public Double getV2() {
        return v2;
    }

    public void setV2(Double v2) {
        this.v2 = v2;
    }

    public PlaceName(String name, Double v, Double v2) {
        this.name = name;
        this.v = v;
        this.v2 = v2;
    }
}
