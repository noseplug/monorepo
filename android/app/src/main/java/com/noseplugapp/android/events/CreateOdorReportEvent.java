package com.noseplugapp.android.events;

import com.google.android.gms.maps.model.LatLng;
import com.noseplugapp.android.models.OdorEvent;

public class CreateOdorReportEvent {
    public final LatLng location;
    public final OdorEvent event;
    public CreateOdorReportEvent(LatLng location, OdorEvent event) {
        this.location = location;
        this.event = event;
    }
}
