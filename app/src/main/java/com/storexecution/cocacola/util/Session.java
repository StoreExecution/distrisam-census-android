package com.storexecution.cocacola.util;

import android.content.Context;

import com.storexecution.cocacola.model.Salepoint;


import java.util.ArrayList;


/**
 * Created by koceila on 28/03/16.
 */
public class Session {
    SharedPrefUtil preferences;

    public static String TAG_INV = "INV";

    public Session(Context context) {
        preferences = SharedPrefUtil.getInstance(context);

    }

    /**
     * Function to store the current invistigation
     *
     * @param salepoint
     */
    public void setSalepoint(Salepoint salepoint) {
        String inv = GsonUtils.salepointToJson(salepoint);
        preferences.putString(TAG_INV, inv);

    }

    /**
     * Function that returns the currently stored salepoint
     *
     * @return Salepoint
     */
    public Salepoint getSalepoint() {
        String inv = preferences.getString(TAG_INV, null);
        if (inv != null)
            return GsonUtils.salepointFromJson(inv);
        return new Salepoint();

    }

    /**
     * function to store the last used wilaya
     *
     * @param wilaya
     */
    public void setWilaya(String wilaya) {

        preferences.putString("wilaya", wilaya);


    }

    /**
     * function to get the last used wilaya
     *
     * @return String
     */

    public String getWilaya() {
        return preferences.getString("wilaya", "");
    }

    /**
     * Function to store the last used commune
     *
     * @param commune
     */
    public void setCommune(String commune) {

        preferences.putString("commune", commune);

    }

    /**
     * Function to get the last used commune
     *
     * @return String
     */

    public String getCommune() {
        return preferences.getString("commune", "");
    }

    /**
     * Function to clear the current stored invistigation
     */
    public void clearSalepoint() {
        preferences.remove(TAG_INV);
    }


    public void setSearchResult(String results) {
        preferences.putString("results", results);
    }

    public String getSearchResult() {
        return preferences.getString("results", GsonUtils.salepointListToJson(new ArrayList<Salepoint>()));
    }


    public void setExpireTime(long time) {

        preferences.putString("expire", time + "");
    }

    public long getExpireTime() {

        return Long.valueOf(preferences.getString("expire", "0"));
    }

    public boolean isExpired(long time) {


        return time > getExpireTime();


    }


}
