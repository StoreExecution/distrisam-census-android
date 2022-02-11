package com.storexecution.cocacola.event;

import android.location.Location;

public class LocationUpdatedEvent {

    Location location;

    public LocationUpdatedEvent() {
    }

    public LocationUpdatedEvent(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
