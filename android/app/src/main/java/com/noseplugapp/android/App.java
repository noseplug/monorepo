package com.noseplugapp.android;

import android.content.Context;

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
}
