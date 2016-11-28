package edu.gatech.cs.environmentalodors.events;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class LocationEvent {
    public final LatLng location;
    public LocationEvent(LatLng location) {
        this.location = location;
    }
}
