package com.storexecution.cocacola.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PaymentSummary {

@SerializedName("total_amount")
    String totalAmount;

    @SerializedName("payment_details")
    ArrayList<PaymentDetail> paymentDetails;
    @SerializedName("last_update")
    String lastUpdate;


    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ArrayList<PaymentDetail> getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(ArrayList<PaymentDetail> paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
