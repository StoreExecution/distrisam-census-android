package com.storexecution.cocacola.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ValidationConditon extends RealmObject {

    @PrimaryKey
    int id;
    @SerializedName("salepoint_validation_id")
    int salepointValidationId;
    @SerializedName("validation_condition_id")
    int validationConditionId;
    @SerializedName("data_id")
    String dataId;
    @SerializedName("data_type")
    String  dataType;

    int status;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSalepointValidationId() {
        return salepointValidationId;
    }

    public void setSalepointValidationId(int salepointValidationId) {
        this.salepointValidationId = salepointValidationId;
    }

    public int getValidationConditionId() {
        return validationConditionId;
    }

    public void setValidationConditionId(int validationConditionId) {
        this.validationConditionId = validationConditionId;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
