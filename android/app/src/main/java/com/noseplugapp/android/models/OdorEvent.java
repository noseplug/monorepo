package com.noseplugapp.android.models;

import com.noseplugapp.android.App;
import com.noseplugapp.android.events.DataChangedEvent;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    private String id = UUID.randomUUID().toString();
    public String name; // TODO: populate name (when we create the event in User)
    public String ownerid; // TODO: populate owner (when we create the event in User)
    public List<String> reportids = new ArrayList<>();
    private List<User> subscribers = new ArrayList<>(); // TODO: add methods for subscribers
    private List<Wallpost> wallposts = new ArrayList<>();

    private final App app = App.getInstance();

    public OdorEvent(OdorReport odorReport) {

        reportids.add(odorReport.getId().toString());
    }
    public OdorEvent() {
        name = "TEST EVENT";
        // We're allowed to create an odor event without an odor report for debugging purposes.
    }

    public void setFirebaseId(String fid) {id = fid;}

    public UUID getId() {
        return UUID.fromString(id);
    }

    public String getName() {
        return name;
    }

    public User getOwner() {
        if (ownerid == null) {
            return null;
        }
        else {
            return app.api().getUser(UUID.fromString(ownerid));
        }
    }

    public List<User> getSubscribers() {
        return subscribers;
    }

    public List<Wallpost> getWallposts() {
        return wallposts;
    }

    public List<Wallpost> getSortedWallposts() {
        wallposts = getWallposts();
        Collections.sort(wallposts,new Comparator<Wallpost>() {
            @Override
            public int compare(Wallpost o1, Wallpost o2) {
                if (o1.type != o2.type) {
                    if (o1.type == Wallpost.Type.normal) {
                        return 1;
                    }
                    else {
                        return -1;
                    }
                }
                else {
                    return o2.date.compareTo(o1.date);
                }

            }
        }
        );

        return wallposts;
    }

    public void addWallpost(Wallpost wallpost) {
        wallposts.add(wallpost);
    }

    public List<OdorReport> getOdorReports() {
        ArrayList<OdorReport> reports = new ArrayList<OdorReport>();
        for (String reportid : reportids) {
            OdorReport correspondingReport = app.api().getOdorReport(UUID.fromString(reportid));
            if(correspondingReport != null) {
                reports.add(correspondingReport);
            }
        }
        return reports;
    }

    public void addOdorReport(OdorReport report) {
        reportids.add(report.getId().toString());
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
