package com.storexecution.cocacola.ui.newpos.location;

import android.location.Location;

public interface LocationValidationCallbackInterface {

    public void setValidLocation(Location location);

    public void unsetLocation();
}
