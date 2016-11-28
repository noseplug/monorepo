package edu.gatech.cs.environmentalodors;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;

import edu.gatech.cs.environmentalodors.events.LocationEvent;

/**
 * GoogleApiClientWrapper wraps the GoogleApiClient. This should help separate the activities from
 * the API boilerplate.
 */
class GoogleApiClientWrapper implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = GoogleApiClientWrapper.class.getSimpleName();

    private GoogleApiClient googleApiClient;

    GoogleApiClientWrapper(Context ctx) {
        googleApiClient = new GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // onStart and onStop are used to keep the wrapper aware of the activity lifecycle.

    void onStart() {
        googleApiClient.connect();
    }
    void onStop() {
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v(TAG, "Connected");

        // TODO: Make sure this doesn't crap out if location (wifi-based) is not turned on.
        // TODO: Only navigate to the current location on first startup instead of every
        // time we connect to Google.
        Location temp = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (temp == null) {
            Log.w(TAG, "No location returned from Google");
        } else {
            Log.d(TAG, "Posting location to EventBus");
            LatLng location = new LatLng(temp.getLatitude(), temp.getLongitude());
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
/*
    public static com.google.android.gms.maps.model.LatLng latLngFromLocation(android.location.Location l) {
        com.google.android.gms.maps.model.LatLng ret
            = new com.google.android.gms.maps.model.LatLng(
                l.getLatitude(),
                l.getLongitude()
            );

        return ret;
    }
    */
}
