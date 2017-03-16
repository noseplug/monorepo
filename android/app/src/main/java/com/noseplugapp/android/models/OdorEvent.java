package com.noseplugapp.android.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * OdorEvent is a group of spatiotemporally related {@link OdorReport}s.
 *
 * For example:
 *      - The smoke odor from a wildfire could affect a vast area like a huge swath of a state,
 *        but last only a few days. (geographically spread out, temporally close)
 *      - A sulfur odor from a paper mill could affect a small area like a neighborhood,
 *        but last for years. (geographically close, temporally spread out)
 *      - etc.
 *
 * These will probably have to be crowdsourced or curated by experts, rather than automatically
 * created. Unless someone here is already an expert on machine learning.
 *
 * Many OdorEvents will only have a single OdorReport.
 */
public class OdorEvent {

    private UUID id = UUID.randomUUID();
    private String name; // TODO: populate name (when we create the event in User)
    private User owner; // TODO: populate owner (when we create the event in User)
    private List<OdorReport> reports = new ArrayList<>();
    private List<User> subscribers = new ArrayList<>(); // TODO: add methods for subscribers
    private List<Wallpost> wallposts = new ArrayList<>();

    public OdorEvent(OdorReport odorReport) {
        reports.add(odorReport);
    }
    public OdorEvent() {
        // We're allowed to create an odor event without an odor report for debugging purposes.
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getOwner() {
        return owner;
    }

    public List<OdorReport> getReports() {
        return reports;
    }

    public List<User> getSubscribers() {
        return subscribers;
    }

    public List<Wallpost> getWallposts() {
        return wallposts;
    }

    public void addWallpost(Wallpost wallpost) {
        wallposts.add(wallpost);
    }

    public List<OdorReport> getOdorReports() {
        return reports;
    }

    public void addOdorReport(OdorReport report) {
        reports.add(report);
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
