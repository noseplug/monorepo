package edu.gatech.cs.environmentalodors;

import android.app.Application;
import android.content.Intent;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import edu.gatech.cs.environmentalodors.events.LocationEvent;
import edu.gatech.cs.environmentalodors.events.OdorReportEvent;
import edu.gatech.cs.environmentalodors.models.OdorEvent;

public class MapsActivity extends FragmentActivity {
    private static final String TAG = MapsActivity.class.getSimpleName();

    private static final float INITIAL_LOCATION_ZOOM_FACTOR = (float) 10.0;

    private GoogleApiClientWrapper googleApiClientWrapper;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        googleApiClientWrapper = new GoogleApiClientWrapper(this);
        initMaps();
        initOnClickListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClientWrapper.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClientWrapper.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onLocationEvent(LocationEvent locationEvent) {
        Log.v(TAG, "Received a location event");
        Location loc = locationEvent.location;
        LatLng current = new LatLng(loc.getLatitude(), loc.getLongitude());
        mMap.addMarker(new MarkerOptions().position(current).title("Where you are"));

        CameraPosition pos = new CameraPosition.Builder()
                .target(current)
                .zoom(INITIAL_LOCATION_ZOOM_FACTOR)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
    }

    @Subscribe
    public void onOdorReportEvent(OdorReportEvent odorReportEvent) {
        Log.v(TAG, "Received an odor report event");
        OdorEvent odorEvent = new OdorEvent(odorReportEvent.odorReport);
        ApplicationState.getInstance().addOdorEvent(odorEvent);
        // TODO: draw morker on the map.
    }

    private void initMaps() {
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
            }
        });
    }

    private void initOnClickListeners() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.report_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapsActivity.this.startActivity(new Intent(MapsActivity.this, ReportFormDateTimeActivity.class));
            }
        });
    }
}
