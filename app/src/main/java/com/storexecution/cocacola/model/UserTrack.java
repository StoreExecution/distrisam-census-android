package com.storexecution.cocacola.model;

import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserTrack extends RealmObject {


    @PrimaryKey
    int userId;
    @SerializedName("name")
    String username;
    double latitude;
    double longitude;
    @SerializedName("last_sync_datetime")
    String time;
    int posAccept;
    int posRefuse;
    int posClosed;
    @SerializedName("wilaya_id")
    int wilayaId;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPosAccept() {
        return posAccept;
    }

    public void setPosAccept(int posAccept) {
        this.posAccept = posAccept;
    }

    public int getPosRefuse() {
        return posRefuse;
    }

    public void setPosRefuse(int posRefuse) {
        this.posRefuse = posRefuse;
    }

    public int getPosClosed() {
        return posClosed;
    }

    public void setPosClosed(int posClosed) {
        this.posClosed = posClosed;
    }
}
