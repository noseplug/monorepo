package com.noseplugapp.android.events;

import com.google.android.gms.maps.model.LatLng;

public class LocationEvent {
    public final LatLng location;
    public LocationEvent(LatLng location) {
        this.location = location;
    }
}
