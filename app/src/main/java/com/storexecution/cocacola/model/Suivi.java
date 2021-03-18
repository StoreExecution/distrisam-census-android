package com.storexecution.cocacola.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Suivi extends RealmObject {

    @SerializedName("ambassadeur")
    int user;
    int salepoint;
    double latitude;
    double longitude;

    String type;
    @SerializedName("day")
    String jour;
    double accurency;
    boolean ismock;
    @SerializedName("milis")
    long milis;
    @SerializedName("time")
    String heur;
    @SerializedName("mobile_id")
    String mobile_Id;
    boolean synced = false;

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getSalepoint() {
        return salepoint;
    }

    public void setSalepoint(int salepoint) {
        this.salepoint = salepoint;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public String getHeur() {
        return heur;
    }

    public void setHeur(String heur) {
        this.heur = heur;
    }

    public String getMobile_Id() {
        return mobile_Id;
    }

    public void setMobile_Id(String mobile_Id) {
        this.mobile_Id = mobile_Id;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public long getMilis() {
        return milis;
    }

    public void setMilis(long milis) {
        this.milis = milis;
    }

    public double getAccurency() {
        return accurency;
    }

    public void setAccurency(double accurency) {
        this.accurency = accurency;
    }

    public boolean isIsmock() {
        return ismock;
    }

    public void setIsmock(boolean ismock) {
        this.ismock = ismock;
    }
}
