package com.storexecution.cocacola.util;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.model.Suivi;


import java.util.ArrayList;


/**
 * Created by koceila on 28/03/16.
 */
public class GsonUtils {


    public static String salepointToJson(Salepoint salepoint) {
        Gson gson = new Gson();
        return gson.toJson(salepoint, Salepoint.class);


    }

    public static Salepoint salepointFromJson(String salepoint) {
        Gson gson = new Gson();
        return gson.fromJson(salepoint, Salepoint.class);

    }

    public static String salepointListToJson(ArrayList<Salepoint> salepoints) {

        Gson gson = new Gson();
        return gson.toJson(salepoints, new TypeToken<ArrayList<Salepoint>>() {
        }.getType());

    }

    public static ArrayList<Salepoint> salepointsListFromJson(String salepoints) {

        Gson gson = new Gson();
        return gson.fromJson(salepoints, new TypeToken<ArrayList<Salepoint>>() {
        }.getType());

    }


    public static String trackingListToJson(ArrayList<Suivi> trackings) {

        Gson gson = new Gson();
        return gson.toJson(trackings, new TypeToken<ArrayList<Suivi>>() {
        }.getType());

    }


}

