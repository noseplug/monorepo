package com.noseplugapp.android.database;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.noseplugapp.android.events.CommentAddedEvent;
import com.noseplugapp.android.models.OdorReport;
import com.noseplugapp.android.models.Odor;
import com.noseplugapp.android.models.OdorEvent;
import com.noseplugapp.android.models.User;
import com.noseplugapp.android.models.Wallpost;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Esteban on 2/8/17.
 */

public class OfflineApi implements NoseplugApiInterface {

    private static final String TAG = OfflineApi.class.getSimpleName();

    private Map<UUID, OdorEvent> odorEvents = new ConcurrentHashMap<>();
    private Map<UUID, OdorReport> odorReports = new ConcurrentHashMap<>();
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

    public OdorReport getOdorReport(UUID uuid) { return odorReports.get(uuid); }

    public User getUser(UUID uuid) {
        return users.get(uuid);
    }

    public UUID addOdorEvent(OdorEvent event) {
        Log.d(TAG, "added odor event: " + event);
        odorEvents.put(event.getId(), event);
        return event.getId();
    }

    public UUID addOdorReport(OdorReport report) {
        Log.d(TAG, "added odor report: " + report);
        odorReports.put(report.getId(), report);
        return report.getId();
    }

    @Override
    public void addWallPost(Wallpost post, String eventID) {
        getOdorEvent(UUID.fromString(eventID)).addWallpost(post);
        EventBus.getDefault().post(new CommentAddedEvent());
    }

    public UUID registerUser(User user) {
        Log.d(TAG, "registered user: " + user);
        users.put(dummyUser.getUuid(), dummyUser);
        return dummyUser.getUuid();
    }

    public Iterable<OdorEvent> getOdorEvents() {
        return odorEvents.values();
    }

}