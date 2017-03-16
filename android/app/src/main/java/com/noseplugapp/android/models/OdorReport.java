package com.noseplugapp.android.models;

import com.google.android.gms.maps.model.LatLng;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;
import java.util.UUID;

/**
 * OdorReport is a individual report of an environmental odor at a specific time and place.
 */
public class OdorReport {

    private UUID id = UUID.randomUUID();
    private User user;
    private OdorEvent odorEvent;
    private Date filingTime; // When the report was filed.
    private LatLng location;
    private Odor odor;

    // TODO: OdorReport should be created via the {@link User} model rather than directly instantiated.

    public OdorReport(User user, Date filingTime, LatLng location, Odor odor) {
        this.user = user;
        this.filingTime = filingTime;
        this.location = location;
        this.odor = odor;
    }

    public UUID getId() {
        return id;
    }

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

    public LatLng getLocation() {
        return location;
    }

    public Odor getOdor() {
        return odor;
    }

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
