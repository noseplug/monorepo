package com.noseplugapp.android.models;

import com.google.android.gms.maps.model.LatLng;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

import com.noseplugapp.android.utils.NoseplugLatLng;

/**
 * OdorReport is a individual report of an environmental odor at a specific time and place.
 */
public class OdorReport {

    private String id = UUID.randomUUID().toString();
    private User user;
    private OdorEvent odorEvent;
    private Date filingTime; // When the report was filed.
    private NoseplugLatLng location;
    private Odor odor;

    // TODO: OdorReport should be created via the {@link User} model rather than directly instantiated.

    public OdorReport() {
    }
    public OdorReport(User user, Date filingTime, LatLng location, Odor odor) {
        this.user = user;
        this.filingTime = filingTime;
        this.location = new NoseplugLatLng(location);
        this.odor = odor;
    }

    public UUID getId() {
        return UUID.fromString(id);
    }
    public void setFirebaseId(String fid) {id = fid;}

    public User getUser() {
        return user;
    }

    // TODO: populate this when creating the odor report
    public OdorEvent getOdorEvent() {
        return odorEvent;
    }

    public Date getFilingTime() {
        return filingTime;
    }
//    public void setFilingTime(int d) {
//        filingTime = new Date(d);
//    }

    public LatLng getLocation() {
        return location.toGoogleLatLng();
    }

    public void setLocation(NoseplugLatLng latLng) { this.location = latLng;}

    public Odor getOdor() {
        return odor;
    }

    public void setOdor(Odor odor) {this.odor = odor;}

    @Override
    public String toString() {
        return String.format("%s: %s", filingTime, odor);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
