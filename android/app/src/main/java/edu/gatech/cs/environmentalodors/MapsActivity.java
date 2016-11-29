package edu.gatech.cs.environmentalodors;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;
import java.util.UUID;

import edu.gatech.cs.environmentalodors.events.CreateOdorReportEvent;
import edu.gatech.cs.environmentalodors.events.LocationEvent;
import edu.gatech.cs.environmentalodors.events.OdorReportEvent;
import edu.gatech.cs.environmentalodors.models.Odor;
import edu.gatech.cs.environmentalodors.models.OdorEvent;
import edu.gatech.cs.environmentalodors.models.OdorReport;
import edu.gatech.cs.environmentalodors.models.User;

import static edu.gatech.cs.environmentalodors.IntentExtraNames.LOCATION;
import static edu.gatech.cs.environmentalodors.IntentExtraNames.ODOR_EVENT_ID;

/**
 * MapsActivity is the home page of the environmental odor app.
 */
public class MapsActivity extends FragmentActivity implements
        View.OnClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnPolygonClickListener,
        OnMapReadyCallback {
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
        super.onDestroy();
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
        UUID eventID = ApplicationState.getInstance().addOdorEvent(odorEvent);

        map.addMarker(new MarkerOptions()
                .position(odorReportEvent.odorReport.location)
                .title("Odor Report"))
            .setTag(eventID);


        updateMap();
    }


    private void initMaps() {
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                EventBus.getDefault().post(new LocationEvent(latLng));
            }
        });

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                EventBus.getDefault().post(new CreateOdorReportEvent(latLng));
            }
        });

        map.setOnInfoWindowClickListener(this);
        map.setOnPolygonClickListener(this);
        generateFakeData();
    }

    private void initOnClickListeners() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.report_fab);
        fab.setOnClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.v(TAG, "Info Window clicked, starting odor event details activity");
        Intent detailsIntent = new Intent(this, OdorEventDetailsActivity.class);
        detailsIntent.putExtra(ODOR_EVENT_ID, new ParcelUuid((UUID) marker.getTag()));
        this.startActivity(detailsIntent);
    }

    @Override
    public void onClick(View v) {



        switch (v.getId()) {
            case R.id.report_fab:
                Log.v(TAG, "Clicked FAB, launching odor report activity");
                EventBus.getDefault().post(new CreateOdorReportEvent(selectedLocation));
                break;

            default:
                String name = this.getResources().getResourceEntryName(v.getId());
                throw new RuntimeException("Clicked an unknown view: " + name);
        }
    }

    @Subscribe
    public void onCreateOdorReportEvent(CreateOdorReportEvent e) {
        Intent reportIntent = new Intent(this, ReportFormDateTimeActivity.class);
        reportIntent.putExtra(LOCATION, e.location);
        this.startActivity(reportIntent);
    }

    public void updateMap() {
        map.clear();
        ApplicationState.getInstance().polygonEventMap.clear();

        for(OdorEvent o : ApplicationState.getInstance().getOdorEvents())
        {
            PolygonOptions polyOptions = new PolygonOptions();
            for(OdorReport r : o.getOdorReports()) {
                polyOptions.add(r.location);

                map.addMarker(new MarkerOptions()
                        .position(r.location)
                        .title("Odor Report"))
                        .setTag(o.uuid);
            }
            polyOptions.fillColor(Color.argb(50, 250, 250, 0));
            polyOptions.strokeColor(Color.argb(80, 250, 250, 0));
            polyOptions.clickable(true);
            Polygon polygon = map.addPolygon(polyOptions);
            ApplicationState.getInstance().polygonEventMap.put(polygon.getId(), o);
        }
    }

    @Override
    public void onPolygonClick(Polygon polygon)
    {
        Log.v(TAG, "Polygon clicked, starting odor event details activity");
        OdorEvent event = ApplicationState.getInstance().polygonEventMap.get(polygon.getId());
        Intent detailsIntent = new Intent(this, OdorEventDetailsActivity.class);
        detailsIntent.putExtra(ODOR_EVENT_ID, new ParcelUuid((UUID) event.uuid));
        this.startActivity(detailsIntent);
    }
    public void generateFakeData() {
        LatLng center = new LatLng(32, -84); // approximately atlanta
        float radius = 4;
        int reportCount = 5;
        Odor.Type type = Odor.Type.CHEMICAL;

        OdorEvent event = new OdorEvent();

        for(int i = 0; i < reportCount; i++)
        {
            Odor tempOdor = new Odor(Odor.Strength.MODERATE, type, "Generated Odor");
            LatLng tempLocation = new LatLng(
                    center.latitude + Math.cos(((float) i / reportCount) * 2 * Math.PI) * radius,
                    center.longitude + Math.sin(((float) i / reportCount) * 2 * Math.PI) * radius);
            OdorReport tempOdorReport = new OdorReport(new User(), new Date(), new Date(), tempLocation, tempOdor);
            event.addOdorReport(tempOdorReport);
            EventBus.getDefault().post(new OdorReportEvent(tempOdorReport));
        }
        ApplicationState.getInstance().addOdorEvent(event);
        //EventBus.getDefault().post(new OdorEvent(tempOdorReport));
    }
}
