package com.noseplugapp.android.database;

import com.noseplugapp.android.models.OdorEvent;
import com.noseplugapp.android.models.OdorReport;
import com.noseplugapp.android.models.User;

import java.util.UUID;


/**
 * Created by Esteban on 2/8/17.
 */

public interface NoseplugApiInterface {

    public OdorEvent getOdorEvent(UUID uuid);
    public OdorReport getOdorReport(UUID uuid);
    public User getUser(UUID uuid);
    public UUID addOdorEvent(OdorEvent event);
    public Iterable<OdorEvent> getOdorEvents();
    public UUID registerUser(User user);
}
