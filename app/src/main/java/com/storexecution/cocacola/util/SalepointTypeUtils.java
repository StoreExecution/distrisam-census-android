package com.storexecution.cocacola.util;

import com.storexecution.cocacola.R;

public class SalepointTypeUtils {

    public static String getType(int type) {

        switch (type) {
            case 1:
                return "AG";
            case 2:
                return "SUP";
            case 3:
                return "FastFood";
            case 4:
                return "Café";
            case 5:
                return "Restaurant";
            case 6:
                return "Thé";
            case 7:
                return "Patisserie";
            case 8:
                return "BT";
        }

        return "";
    }

    public static int getIcon(int type) {

        switch (type) {
            case 1:
                return R.drawable.ag_red;
            case 2:
                return R.drawable.supp_red;
            case 3:
                return R.drawable.fast_food;
            case 4:
                return R.drawable.cafe;
            case 5:
                return R.drawable.restaurant;
            case 6:
                return R.drawable.tee;
            case 7:
                return R.drawable.patisserie;
            case 8:
                return R.drawable.bureau_tabac;
        }

        return R.drawable.ag_red;
    }
}
