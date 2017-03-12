package com.noseplugapp.android;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.noseplugapp.android.database.OfflineApi;
import com.noseplugapp.android.events.CreateOdorReportEvent;
import com.noseplugapp.android.events.LocationEvent;
import com.noseplugapp.android.events.OdorReportEvent;
import com.noseplugapp.android.models.Comment;
import com.noseplugapp.android.models.Odor;
import com.noseplugapp.android.models.OdorEvent;
import com.noseplugapp.android.models.OdorReport;
import com.noseplugapp.android.models.User;

/**
 * MapsActivity is the home page of the environmental odor app.
 */
public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback{
    private static final String TAG = MapsActivity.class.getSimpleName();
    private static final String MAP_FRAGMENT_TAG = "map_fragment";
    private static final float INITIAL_LOCATION_ZOOM_FACTOR = (float) 10.0;
    private static final LatLng DEFAULT_LOCATION = new LatLng(32, -84);

    private final Context ctx = this;

    private GoogleApiClientWrapper googleApi;
    private GoogleMap map;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    private LatLng selectedLocation; // Starts at the user's last known location.
    private Marker userMarker;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    //Defining Variable
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initMap();

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        googleApi = new GoogleApiClientWrapper(this);
        initOnClickListeners();
        EventBus.getDefault().register(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthstateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        firebaseAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInAnonymously", task.getException());
                    Toast.makeText(ctx, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sideSlideMenu();
    }

    private void sideSlideMenu() {
        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

        // This method will trigger on item Click of navigation menu
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {


        //Checking if the item is in checked state or not, if not make it in checked state
            if(menuItem.isChecked()) menuItem.setChecked(false);
            else menuItem.setChecked(true);
            //Closing drawer on item click
            drawerLayout.closeDrawers();
            //Check to see which item was being clicked and perform appropriate action
            switch (menuItem.getItemId()){

            //Replacing the main content with ContentFragment Which is our Inbox View;
                case R.id.register:
//                    Toast.makeText(getApplicationContext(),"Inbox Selected",Toast.LENGTH_SHORT).show();
//                    ContentFragment fragment = new ContentFragment();
//                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.frame,fragment);
//                    fragmentTransaction.commit();
                    Intent intent = new Intent(ctx, RegistrationActivity.class);
                    startActivity(intent);
                    return true;
                    // For rest of the options we just show a toast on click

                case R.id.login:
                    intent = new Intent(ctx, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Login Selected",Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.odor_classification:
                    Toast.makeText(getApplicationContext(),"Send Selected",Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Clicked Odors Button");
                    new OdorsDialogFragment().show(getFragmentManager(), "dialog");
                    return true;
                default:
                    return true;
                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open, R.string.drawer_open){
                                                    //0x7f07003a
        @Override
        public void onDrawerClosed(View drawerView) {
        // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
            super.onDrawerClosed(drawerView);
        }
        @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        //drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.nav, menu);
            return true;
        }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.register) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    private void initMap() {
        FragmentManager manager = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) manager.findFragmentByTag(
                MAP_FRAGMENT_TAG);

        if (mapFragment == null) {
            mapFragment = new SupportMapFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.map_fragment_container, mapFragment, "mapFragment");
            transaction.commit();
            manager.executePendingTransactions();
        }

        mapFragment.getMapAsync(this);
    }

    private void initOnClickListeners() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.report_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "Clicked FAB, launching odor report activity");
                EventBus.getDefault().post(new CreateOdorReportEvent(selectedLocation));
            }
        });
//        Button registrationBtn = (Button) findViewById(R.id.register_btn);
//        registrationBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ctx, RegistrationActivity.class);
//                startActivity(intent);
//            }
//        });
//        Button loginBtn = (Button) findViewById(R.id.login_btn);
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ctx, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//        FloatingActionButton odors = (FloatingActionButton) findViewById(R.id.odorsButton);
//        odors.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.v(TAG, "Clicked Odors Button");
//                new OdorsDialogFragment().show(getFragmentManager(), "dialog");
//            }
//        });

    }




    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        drawerToggle.onConfigurationChanged(configuration);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApi.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApi.onStop();
        if (firebaseAuthStateListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthStateListener);
        }
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
        OfflineApi.noseplug.addOdorEvent(odorEvent);

        map.addMarker(new MarkerOptions()
                .position(odorReportEvent.odorReport.location)
                .title("Odor Report"))
                .setTag(odorReportEvent.odorReport.uuid);

        updateMap();
    }

    @Subscribe
    public void onCreateOdorReportEvent(CreateOdorReportEvent e) {
        Intent reportIntent = new Intent(this, ReportFormActivity.class);
        reportIntent.putExtra(getResources().getString(R.string.intent_extra_location), e.location);
        this.startActivity(reportIntent);
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

        EventBus.getDefault().post(new LocationEvent(DEFAULT_LOCATION));

        CameraPosition pos = new CameraPosition.Builder()
                .target(selectedLocation)
                .zoom(INITIAL_LOCATION_ZOOM_FACTOR)
                .build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(pos));


        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(!marker.equals(userMarker)) {
                    Log.v(TAG, "Info Window clicked, starting odor event details activity");
                    Intent reportDetailsIntent = new Intent(ctx, OdorReportDetailsActivity.class);
                    reportDetailsIntent.putExtra(
                            getResources().getString(R.string.intent_extra_odor_report_id),
                            new ParcelUuid((UUID) marker.getTag()));
                    ctx.startActivity(reportDetailsIntent);
                }
            }
        });

        map.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                Log.v(TAG, "Polygon clicked, starting odor event details activity");
                OdorEvent event = OfflineApi.polygonEventMap.get(polygon.getId());
                Intent detailsIntent = new Intent(ctx, OdorEventDetailsActivity.class);
                detailsIntent.putExtra(
                        getResources().getString(R.string.intent_extra_odor_event_id),
                        new ParcelUuid((UUID) event.uuid));
                ctx.startActivity(detailsIntent);
            }
        });

        generateFakeData();
        updateMap();
    }

    public void updateMap() {
        map.clear();

        if (userMarker != null) {
            userMarker.remove();
        }
        BitmapDescriptor userIcon = BitmapDescriptorFactory.fromAsset("user_icon.png");
        userMarker = map.addMarker(new MarkerOptions().position(selectedLocation).title("You are Here").zIndex(-1.0f).icon(userIcon));
        userMarker.showInfoWindow();

        OfflineApi.polygonEventMap.clear();

        for(OdorEvent o : OfflineApi.noseplug.getOdorEvents())
        {
            PolygonOptions polyOptions = new PolygonOptions();
            for(OdorReport r : o.getOdorReports()) {
                polyOptions.add(r.location);

                map.addMarker(new MarkerOptions()
                        .position(r.location)
                        .title("Odor Report"))
                        .setTag(r.uuid);
            }
            polyOptions.fillColor(Color.argb(50, 250, 250, 0));
            polyOptions.strokeColor(Color.argb(80, 250, 250, 0));
            polyOptions.clickable(true);
            Polygon polygon = map.addPolygon(polyOptions);
            OfflineApi.polygonEventMap.put(polygon.getId(), o);
        }
    }

    //@Override
    public void onPolygonClick(Polygon polygon)
    {
        Log.v(TAG, "Polygon clicked, starting odor event details activity");
        OdorEvent event = OfflineApi.polygonEventMap.get(polygon.getId());
        Intent detailsIntent = new Intent(this, OdorEventDetailsActivity.class);
        detailsIntent.putExtra(getResources().getString(R.string.intent_extra_odor_event_id),
                new ParcelUuid((UUID) event.uuid));
        this.startActivity(detailsIntent);
    }

    public void generateFakeData() {
        LatLng center = DEFAULT_LOCATION; // approximately atlanta
        float radius = 0.2f;
        int reportCount = 5;
        int commentCount = 5;
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

        for(int i = 0; i < commentCount; i++)
        {
            Comment tempComment = new Comment();
            event.addComment(tempComment);
        }

        OfflineApi.noseplug.addOdorEvent(event);
        //EventBus.getDefault().post(new OdorEvent(tempOdorReport));
    }
}
