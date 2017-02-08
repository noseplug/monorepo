package edu.gatech.cs.environmentalodors.database;


import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import edu.gatech.cs.environmentalodors.ApplicationState;
import edu.gatech.cs.environmentalodors.models.OdorReport;
import edu.gatech.cs.environmentalodors.models.Odor;
import edu.gatech.cs.environmentalodors.models.OdorEvent;
import edu.gatech.cs.environmentalodors.models.User;

/**
 * Created by Esteban on 2/8/17.
 */

public class OfflineAPI implements NoseplugAPIInterface {

    private Map<UUID, OdorEvent> odorEvents = new ConcurrentHashMap<>();
    private Map<UUID, User> users = new ConcurrentHashMap<>();
    User user1 = new User();
    OdorEvent odorEvent1 = new OdorEvent();
    OdorReport odorReport1 = new OdorReport(
            user1,
            new Date(),
            new Date(),
            new LatLng(0, 0),
            new Odor(Odor.Strength.STRONG, Odor.Type.SULFUR, "Ewww"));

    public OfflineAPI() {
        odorEvent1.addOdorReport(odorReport1);
        odorEvents.put(odorEvent1.uuid, odorEvent1);
        users.put(user1.uuid, user1);
    }

    public OdorEvent getOdorEvent(UUID uuid) {
        return odorEvents.get(uuid);
    }

    public OdorReport getOdorReport(UUID uuid) {
        OdorReport found = null;

        for(OdorEvent event : odorEvents.values()) {
            for (OdorReport report : event.getOdorReports()) {
                if (report.uuid.equals(uuid)) {
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
        odorEvents.put(event.uuid, event);
        return event.uuid;
    }

    public UUID registerUser(User user) {
        users.put(user1.uuid, user1);
        return user1.uuid;
    }

    public Iterable<OdorEvent> getOdorEvents() {
        return odorEvents.values();
    }

    //NoseplugApiInterface noseplug = new noseplug.OfflineApi();

}
