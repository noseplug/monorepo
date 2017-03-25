package com.noseplugapp.android.database;

import com.noseplugapp.android.models.OdorEvent;
import com.noseplugapp.android.models.OdorReport;
import com.noseplugapp.android.models.User;
import com.noseplugapp.android.models.Wallpost;

import java.util.List;
import java.util.UUID;


/**
 * Created by Esteban on 2/8/17.
 */

public interface NoseplugApiInterface {

    public OdorEvent getOdorEvent(UUID uuid);
    public OdorReport getOdorReport(UUID uuid);
    public User getUser(UUID uuid);
    public UUID addOdorEvent(OdorEvent event);
    public UUID addOdorReport(OdorReport report);
    public void addWallPost(Wallpost post, String eventID);
    public Iterable<OdorEvent> getOdorEvents();
    public UUID registerUser(User user);
}
