package com.storexecution.cocacola.model;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class ConcurrentFridge implements RealmModel {

    String brand;
    String mobile_id;

    int type;

    int external;

    public ConcurrentFridge() {
    }

    public ConcurrentFridge(String brand, int type) {
        this.brand = brand;
        this.type = type;
    }

    public ConcurrentFridge(String brand, int type, int external) {
        this.brand = brand;
        this.type = type;
        this.external = external;
    }

    public ConcurrentFridge(String mobile_id,String brand, int type, int external) {
        this.brand = brand;
        this.mobile_id = mobile_id;
        this.type = type;
        this.external = external;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getExternal() {
        return external;
    }

    public void setExternal(int external) {
        this.external = external;
    }

    public String getMobile_id() {
        return mobile_id;
    }

    public void setMobile_id(String mobile_id) {
        this.mobile_id = mobile_id;
    }
}
