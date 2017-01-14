package edu.gatech.cs.environmentalodors.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.UUID;

/**
 * OdorReport is a individual report of an environmental odor at a specific time and place.
 */
public class OdorReport {
    public final UUID uuid = UUID.randomUUID();

    public final User user;
    public final Date creationDate; // When the report was created.
    public final Date reportDate;   // When the odor was smelled.
    public final LatLng location;
    public final Odor odor;

    // TODO: how do we represent duration?

    public OdorReport(User user, Date creationDate, Date reportDate, LatLng location, Odor odor) {
        this.user = user;
        this.creationDate = creationDate;
        this.reportDate = reportDate;
        this.location = location;
        this.odor = odor;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", reportDate, odor);
    }
}
