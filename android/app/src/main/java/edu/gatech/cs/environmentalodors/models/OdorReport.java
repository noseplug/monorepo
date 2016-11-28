package edu.gatech.cs.environmentalodors.models;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * OdorReport is a individual report of an environmental odor at a specific time and place.
 */
public class OdorReport {
    private User user;
    private Date creationDate; // When the report was created.
    private Date reportDate;   // When the odor was smelled.
    private LatLng location;
    private Odor odor;

    // TODO: how do we represent duration?

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Odor getOdor() {
        return odor;
    }

    public void setOdor(Odor odor) {
        this.odor = odor;
    }

}
