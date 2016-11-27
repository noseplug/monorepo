package edu.gatech.cs.environmentalodors;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import edu.gatech.cs.environmentalodors.models.OdorEvent;

/**
 * Global singleton for application state. See: http://stackoverflow.com/a/19457168
 */
public class ApplicationState {
    private Map<UUID, OdorEvent> odorEvents = new HashMap<>();

    public OdorEvent getOdorEvent(UUID uuid) {
        return odorEvents.get(uuid);
    }

    public void addOdorEvent(OdorEvent event) {
        odorEvents.put(event.uuid, event);
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
