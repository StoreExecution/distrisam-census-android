package com.storexecution.cocacola.model;

public class PaymentDetail {


    String amount;
    String posCount;
    String valid;
    String date;


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPosCount() {
        return posCount;
    }

    public void setPosCount(String posCount) {
        this.posCount = posCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }
}
