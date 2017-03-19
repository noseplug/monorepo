package com.noseplugapp.android.database;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.noseplugapp.android.models.OdorReport;
import com.noseplugapp.android.models.Odor;
import com.noseplugapp.android.models.OdorEvent;
import com.noseplugapp.android.models.User;

/**
 * Created by Esteban on 2/8/17.
 */

public class OfflineApi implements NoseplugApiInterface {

    public static final NoseplugApiInterface noseplug = new OfflineApi();
    public static final Map<String, OdorEvent> polygonEventMap = new ConcurrentHashMap<>();
    private Map<UUID, OdorEvent> odorEvents = new ConcurrentHashMap<>();
    private Map<UUID, User> users = new ConcurrentHashMap<>();
    private User dummyUser = new User();
    private OdorEvent dummyOdorEvent = new OdorEvent();
    private OdorReport dummyOdorReport = new OdorReport(
            dummyUser,
            new Date(),
            new LatLng(0, 0),
            new Odor(Odor.Strength.STRONG, Odor.Type.SULFUR, "Ewww"));

    public OfflineApi() {
        dummyOdorEvent.addOdorReport(dummyOdorReport);
        odorEvents.put(dummyOdorEvent.getId(), dummyOdorEvent);
        users.put(dummyUser.getUuid(), dummyUser);
    }

    public OdorEvent getOdorEvent(UUID uuid) {
        return odorEvents.get(uuid);
    }

    public OdorReport getOdorReport(UUID uuid) {
        OdorReport found = null;

        for(OdorEvent event : odorEvents.values()) {
            for (OdorReport report : event.getOdorReports()) {
                if (report.getId().equals(uuid)) {
                    found = report;
                }
            }
        }
        return found;
    }

    public User getUser(UUID uuid) {
        return users.get(uuid);
    }

    public UUID addOdorEvent(OdorEvent event) {
        odorEvents.put(event.getId(), event);
        return event.getId();
    }

    public UUID registerUser(User user) {
        users.put(dummyUser.getUuid(), dummyUser);
        return dummyUser.getUuid();
    }

    public Iterable<OdorEvent> getOdorEvents() {
        return odorEvents.values();
    }

}