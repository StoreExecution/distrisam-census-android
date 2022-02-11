package com.storexecution.cocacola.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RTMSalepoint extends RealmObject {
    @PrimaryKey
    int id;
    String salepoint_name;
    String owner_name;
    String owner_phone;
    String affected_date;
    String adress;
    int wilaya_id;
    String commune;
    String image;
    double latitude;
    double longitude;
    int affected_user_id;
    @SerializedName("orderindex")
    int order;
    int done;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSalepoint_name() {
        return salepoint_name;
    }

    public void setSalepoint_name(String salepoint_name) {
        this.salepoint_name = salepoint_name;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_phone() {
        return owner_phone;
    }

    public void setOwner_phone(String owner_phone) {
        this.owner_phone = owner_phone;
    }

    public String getAffected_date() {
        return affected_date;
    }

    public void setAffected_date(String affected_date) {
        this.affected_date = affected_date;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public int getWilaya_id() {
        return wilaya_id;
    }

    public void setWilaya_id(int wilaya_id) {
        this.wilaya_id = wilaya_id;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public int getAffected_user_id() {
        return affected_user_id;
    }

    public void setAffected_user_id(int affected_user_id) {
        this.affected_user_id = affected_user_id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isDone() {
        return done >0 ;
    }

    public void setDone(int done) {
        this.done = done;
    }
}
