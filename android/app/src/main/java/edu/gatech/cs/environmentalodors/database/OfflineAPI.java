package edu.gatech.cs.environmentalodors.database;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//import edu.gatech.cs.environmentalodors.database.NoseplugApiInterface;
import edu.gatech.cs.environmentalodors.models.OdorReport;
import edu.gatech.cs.environmentalodors.models.Odor;
import edu.gatech.cs.environmentalodors.models.OdorEvent;
import edu.gatech.cs.environmentalodors.models.User;

/**
 * Created by Esteban on 2/8/17.
 */

public class OfflineApi implements NoseplugApiInterface {

    public static final NoseplugApiInterface noseplug = new OfflineApi();
    public static  final Map<String, OdorEvent> polygonEventMap = new ConcurrentHashMap<>();
    private Map<UUID, OdorEvent> odorEvents = new ConcurrentHashMap<>();
    private Map<UUID, User> users = new ConcurrentHashMap<>();
    private User dummyUser = new User();
    private OdorEvent dummyOdorEvent = new OdorEvent();
    private OdorReport dummyOdorReport = new OdorReport(
            dummyUser,
            new Date(),
            new Date(),
            new LatLng(0, 0),
            new Odor(Odor.Strength.STRONG, Odor.Type.SULFUR, "Ewww"));

    public OfflineApi() {
        dummyOdorEvent.addOdorReport(dummyOdorReport);
        odorEvents.put(dummyOdorEvent.uuid, dummyOdorEvent);
        users.put(dummyUser.uuid, dummyUser);
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
        users.put(dummyUser.uuid, dummyUser);
        return dummyUser.uuid;
    }

    public Iterable<OdorEvent> getOdorEvents() {
        return odorEvents.values();
    }

}