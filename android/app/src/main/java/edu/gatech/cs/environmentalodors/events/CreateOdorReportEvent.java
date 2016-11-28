package edu.gatech.cs.environmentalodors.events;

import com.google.android.gms.maps.model.LatLng;

import edu.gatech.cs.environmentalodors.models.OdorReport;

public class CreateOdorReportEvent {
    public final LatLng location;
    public CreateOdorReportEvent(LatLng location) {
        this.location = location;
    }
}
