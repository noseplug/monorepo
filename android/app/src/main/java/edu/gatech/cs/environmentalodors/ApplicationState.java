package edu.gatech.cs.environmentalodors;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import edu.gatech.cs.environmentalodors.models.OdorEvent;
import edu.gatech.cs.environmentalodors.models.OdorReport;

/**
 * Global singleton for application state. See: http://stackoverflow.com/a/19457168
 */
public final class ApplicationState {
    private Map<UUID, OdorEvent> odorEvents = new HashMap<>();
    public Map<String, OdorEvent> polygonEventMap = new HashMap<>();

    public OdorEvent getOdorEvent(UUID uuid) {
        return odorEvents.get(uuid);
    }

    public OdorReport getOdorReport(UUID uuid) {
        OdorReport found = null;

        for(OdorEvent o : odorEvents.values())
        {
            for(OdorReport r : o.getOdorReports())
            {
                if (r.uuid.equals(uuid))
                {
                    found = r;
                }
            }
        }

        return found;
    }

    public UUID addOdorEvent(OdorEvent event) {
        odorEvents.put(event.uuid, event);
        return event.uuid;
    }

    public Iterable<OdorEvent> getOdorEvents() {
        return odorEvents.values();
    }

    // Singleton stuff
    private ApplicationState() {} // prevent direct instantiation
    private static ApplicationState instance;
    public static ApplicationState getInstance() {
        if(instance == null) {
            instance = new ApplicationState();
        }
        return instance;
    }
}
