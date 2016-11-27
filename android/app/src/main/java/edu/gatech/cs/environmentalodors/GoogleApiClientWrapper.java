package edu.gatech.cs.environmentalodors;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.greenrobot.eventbus.EventBus;

import edu.gatech.cs.environmentalodors.events.LocationEvent;

class GoogleApiClientWrapper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = GoogleApiClientWrapper.class.getSimpleName();


    private GoogleApiClient googleApiClient;

    GoogleApiClientWrapper(Context ctx) {
        googleApiClient = new GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    void onStart() {
        googleApiClient.connect();
    }

    void onStop() {
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v(TAG, "Connected");

        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location == null) {
            Log.w(TAG, "No location returned from Google");
        } else {
            Log.d(TAG, "Posting location to EventBus");
            EventBus.getDefault().post(new LocationEvent(location));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v(TAG, "Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Connection failed: " + connectionResult);
    }
}
