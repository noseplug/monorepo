package com.noseplugapp.android.events;

import com.google.android.gms.maps.model.LatLng;

public class CreateOdorReportEvent {
    public final LatLng location;
    public CreateOdorReportEvent(LatLng location) {
        this.location = location;
    }
}
