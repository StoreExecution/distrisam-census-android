package com.storexecution.cocacola.model;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Fridge implements RealmModel {


    String salepointMobileId;
    String MobileId;
    int fridgeOwner;
    int attributionYear;
    int fridgeModel;
    String FridgeSerial;
    int fridgeState;
    int breakDownType;
    String photoFridge;
    String barCode;
    String fridgeTemp;
    int isOn;
    int abused;
    int external;
    double completed;
    boolean synced;

    public Fridge() {


        salepointMobileId = "";
        fridgeOwner = 0;
        attributionYear = 0;
        fridgeModel = 0;
        FridgeSerial = "";
        fridgeState = 0;
        breakDownType = 0;
        photoFridge = "";
        barCode = "";
        fridgeTemp = "";
        isOn = -1;
        abused = -1;
        external = -1;
        completed = 0;

        synced = false;
    }


    public int getFridgeOwner() {
        return fridgeOwner;
    }

    public void setFridgeOwner(int fridgeOwner) {
        this.fridgeOwner = fridgeOwner;
    }

    public int getAttributionYear() {
        return attributionYear;
    }

    public void setAttributionYear(int attributionYear) {
        this.attributionYear = attributionYear;
    }

    public int getFridgeModel() {
        return fridgeModel;
    }

    public void setFridgeModel(int fridgeModel) {
        this.fridgeModel = fridgeModel;
    }

    public String getFridgeSerial() {
        return FridgeSerial;
    }

    public void setFridgeSerial(String fridgeSerial) {
        FridgeSerial = fridgeSerial;
    }

    public int getFridgeState() {
        return fridgeState;
    }

    public void setFridgeState(int fridgeState) {
        this.fridgeState = fridgeState;
    }

    public int getBreakDownType() {
        return breakDownType;
    }

    public void setBreakDownType(int breakDownType) {
        this.breakDownType = breakDownType;
    }

    public String getPhotoFridge() {
        return photoFridge;
    }

    public void setPhotoFridge(String photoFridge) {
        this.photoFridge = photoFridge;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getFridgeTemp() {
        return fridgeTemp;
    }

    public void setFridgeTemp(String fridgeTemp) {
        this.fridgeTemp = fridgeTemp;
    }

    public int getIsOn() {
        return isOn;
    }

    public void setIsOn(int isOn) {
        this.isOn = isOn;
    }

    public int getAbused() {
        return abused;
    }

    public void setAbused(int abused) {
        this.abused = abused;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public String getSalepointMobileId() {
        return salepointMobileId;
    }

    public void setSalepointMobileId(String salepointMobileId) {
        this.salepointMobileId = salepointMobileId;
    }

    public String getMobileId() {
        return MobileId;
    }

    public void setMobileId(String mobileId) {
        MobileId = mobileId;
    }


    public int getExternal() {
        return external;
    }

    public void setExternal(int external) {
        this.external = external;
    }

    public double getCompleted() {
        return completed;
    }

    public void setCompleted(double completed) {
        this.completed = completed;
    }


}
