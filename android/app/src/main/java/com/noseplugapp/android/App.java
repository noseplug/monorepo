package com.noseplugapp.android;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.noseplugapp.android.database.NoseplugApiInterface;

// Singleton for global variables.
public class App {
    private static App instance;

    private App() {}

    public static App getInstance() {
        if(instance == null) {
            instance = new App();
        }
        return instance;
    }

    private NoseplugApiInterface api;

    public void api(NoseplugApiInterface api) {
        this.api = api;
    }

    public NoseplugApiInterface api() {
        return api;
    }

    private LatLng location;
    public void setMostRecentLocation(LatLng location) {
        this.location = location;
    }

    private boolean isNew;

    public boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public LatLng getMostRecentLocation() {
        return location;
    }

}
