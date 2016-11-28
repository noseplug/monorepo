package edu.gatech.cs.environmentalodors.models;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * OdorReport is a individual report of an environmental odor at a specific time and place.
 */
public class OdorReport {
    private final User user;
    private final Date creationDate; // When the report was created.
    private final Date reportDate;   // When the odor was smelled.
    private final LatLng location;
    private final Odor odor;

    // TODO: how do we represent duration?

    public OdorReport(User user, Date creationDate, Date reportDate, LatLng location, Odor odor) {
        this.user = user;
        this.creationDate = creationDate;
        this.reportDate = reportDate;
        this.location = location;
        this.odor = odor;
    }

    public User getUser() {
        return user;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public LatLng getLocation() {
        return location;
    }

    public Odor getOdor() {
        return odor;
    }
}
