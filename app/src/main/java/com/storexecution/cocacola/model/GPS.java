package com.storexecution.cocacola.model;

/**
 * Created by Koceila on 21/12/2016.
 */

public class GPS {
    private double latitude;
    private double longitude;
    private double accurency;
    private boolean isMock;


    public GPS() {
        latitude = 0.0;
        longitude = 0.0;
        accurency = 0.0;
        isMock = false;
    }

    public GPS(double latitude, double longitude, double accurency, boolean isMock) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accurency = accurency;
        this.isMock = isMock;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAccurency() {
        return accurency;
    }

    public void setAccurency(double accurency) {
        this.accurency = accurency;
    }

    public boolean isMock() {
        return isMock;
    }

    public void setMock(boolean mock) {
        isMock = mock;
    }
}
