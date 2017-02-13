package com.noseplugapp.android;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.noseplugapp.android.events.LocationEvent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;

/**
 * GoogleApiClientWrapper wraps the GoogleApiClient. This should help separate the activities from
 * the API boilerplate.
 */
class GoogleApiClientWrapper implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = GoogleApiClientWrapper.class.getSimpleName();

    private final GoogleApiClient googleApiClient;
    private final Context ctx;

    public GoogleApiClientWrapper(Context ctx) {
        this.ctx = ctx;
        googleApiClient = new GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // onStart and onStop are used to keep the wrapper aware of the activity lifecycle.

    public void onStart() {
        googleApiClient.connect();
    }

    public void onStop() {
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v(TAG, "Connected");

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e(TAG, "No location permissions are available");
            return;
        }
        Location temp = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        // TODO: Only navigate to the current location on first startup instead of every
        // time we connect to Google.
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
}
