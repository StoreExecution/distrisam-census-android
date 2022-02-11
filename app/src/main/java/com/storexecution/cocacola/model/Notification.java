package com.storexecution.cocacola.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Notification extends RealmObject {

    @PrimaryKey
    int id;
    @SerializedName("salepoint_id")
    int salepointId;
    @SerializedName("salepoint_mobile_id")
    String mobileId;
    @SerializedName("user_id")
    int userId;
    @SerializedName("status")
    int status;
    @SerializedName("comment")
    String comment;
    @SerializedName("conditions")
    RealmList<ValidationConditon> conditions;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSalepointId() {
        return salepointId;
    }

    public void setSalepointId(int salepointId) {
        this.salepointId = salepointId;
    }

    public String getMobileId() {
        return mobileId;
    }

    public void setMobileId(String mobileId) {
        this.mobileId = mobileId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public RealmList<ValidationConditon> getConditions() {
        return conditions;
    }

    public void setConditions(RealmList<ValidationConditon> conditions) {
        this.conditions = conditions;
    }
}
