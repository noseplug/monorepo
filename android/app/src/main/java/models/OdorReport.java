package models;

import android.location.Location;

import java.util.Date;

/**
 * OdorReport is a individual report of an environmental odor at a specific time and place.
 */
public class OdorReport {
    private User user;
    private Date creationDate; // When the report was created.

    private Date reportDate;   // When the odor was smelled.
    private Location location;
    private Odor odor;

    // TODO: how do we represent duration?
}
