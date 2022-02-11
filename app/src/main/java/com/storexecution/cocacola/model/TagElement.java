package com.storexecution.cocacola.model;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class TagElement implements RealmModel {
    String id;
    String name;
    int quantity;


    public TagElement() {

    }

    public TagElement(String id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public TagElement(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
