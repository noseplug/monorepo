package com.noseplugapp.android.database;

import com.noseplugapp.android.models.OdorEvent;
import com.noseplugapp.android.models.OdorReport;
import com.noseplugapp.android.models.User;

import java.util.UUID;

/**
 * Online version of the Noseplug API that communicates with the Firebase backend.
 */
public class FirebaseApi implements NoseplugApiInterface {
    @Override
    public OdorEvent getOdorEvent(UUID uuid) {
        return null;
    }

    @Override
    public OdorReport getOdorReport(UUID uuid) {
        return null;
    }

    @Override
    public User getUser(UUID uuid) {
        return null;
    }

    @Override
    public UUID addOdorEvent(OdorEvent event) {
        return null;
    }

    @Override
    public Iterable<OdorEvent> getOdorEvents() {
        return null;
    }

    @Override
    public UUID registerUser(User user) {
        return null;
    }
}
