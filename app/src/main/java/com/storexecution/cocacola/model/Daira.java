package com.storexecution.cocacola.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Daira extends RealmObject {

    @PrimaryKey
    int id;
    String code;
    String name_ar;
    String name;
    RealmList<Commune> communes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName_ar() {
        return name_ar;
    }

    public void setName_ar(String name_ar) {
        this.name_ar = name_ar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Commune> getCommunes() {
        return communes;
    }

    public void setCommunes(RealmList<Commune> communes) {
        this.communes = communes;
    }
}
