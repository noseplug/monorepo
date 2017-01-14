package edu.gatech.cs.environmentalodors;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import edu.gatech.cs.environmentalodors.models.OdorEvent;
import edu.gatech.cs.environmentalodors.models.OdorReport;

/**
 * Global singleton for application state. See: http://stackoverflow.com/a/19457168
 */
public final class ApplicationState {
    // NOTE: The ConcurrentHashMap does not synchronize all get/put requests, so it does not
    // guarantee consistency. It is still much better than plain HashMap, which is only single
    // threaded.
    private Map<UUID, OdorEvent> odorEvents = new ConcurrentHashMap<>();
    public Map<String, OdorEvent> polygonEventMap = new ConcurrentHashMap<>();

    private static final ApplicationState INSTANCE = new ApplicationState();
    public static ApplicationState getInstance() {
        return INSTANCE;
    }

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

}
