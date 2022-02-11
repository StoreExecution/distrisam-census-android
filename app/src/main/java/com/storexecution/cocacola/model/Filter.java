package com.storexecution.cocacola.model;

import com.google.gson.annotations.SerializedName;

public class Filter {


    @SerializedName("selectedUser")
    int userId;
    @SerializedName("date")
    String day;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
