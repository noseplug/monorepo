package edu.gatech.cs.environmentalodors;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import edu.gatech.cs.environmentalodors.events.LocationEvent;
import edu.gatech.cs.environmentalodors.events.OdorReportEvent;
import edu.gatech.cs.environmentalodors.models.OdorEvent;

import static edu.gatech.cs.environmentalodors.IntentExtraNames.SELECTED_LOCATION;

/**
 * MapsActivity is the home page of the environmental odor app.
 */
public class MapsActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = MapsActivity.class.getSimpleName();

    private static final float INITIAL_LOCATION_ZOOM_FACTOR = (float) 10.0;

    private GoogleApiClientWrapper googleApi;
    private GoogleMap map;

    private LatLng selectedLocation; // Starts at the user's last known location.
    private Marker userMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        googleApi = new GoogleApiClientWrapper(this);
        initMaps();
        initOnClickListeners();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApi.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApi.onStop();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onLocationEvent(LocationEvent locationEvent){
        Log.v(TAG, "Received a location event");
        selectedLocation = locationEvent.location;
        LatLng current = locationEvent.location;

        if (userMarker != null) {
            userMarker.remove();
        }
        BitmapDescriptor userIcon = BitmapDescriptorFactory.fromAsset("user_icon.png");
        userMarker = map.addMarker(new MarkerOptions().position(current).title("You are Here").zIndex(-1.0f).icon(userIcon));

        CameraPosition pos = new CameraPosition.Builder()
                .target(current)
                .zoom(INITIAL_LOCATION_ZOOM_FACTOR)
                .build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
    }

    @Subscribe
    public void onOdorReportEvent(OdorReportEvent odorReportEvent) {
        Log.v(TAG, "Received an odor report event");
        OdorEvent odorEvent = new OdorEvent(odorReportEvent.odorReport);
        ApplicationState.getInstance().addOdorEvent(odorEvent);

        map.addMarker(new MarkerOptions()
                .position(odorReportEvent.odorReport.getLocation())
                .title("Odor Report"));
    }


    private void initMaps() {
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng latLng) {
                        EventBus.getDefault().post(new LocationEvent(latLng));
                    }
                });
            }
        });
    }

    private void initOnClickListeners() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.report_fab);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.report_fab:
                Log.v(TAG, "Clicked FAB, launching odor report activity");
                Intent reportIntent = new Intent(this, ReportFormDateTimeActivity.class);
                reportIntent.putExtra(SELECTED_LOCATION, selectedLocation);
                this.startActivity(reportIntent);
                break;

            default:
                String name = this.getResources().getResourceEntryName(v.getId());
                throw new RuntimeException("Clicked an unknown view: " + name);
        }
    }
}
