package com.noseplugapp.android.database;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.noseplugapp.android.events.CommentAddedEvent;
import com.noseplugapp.android.events.DataChangedEvent;
import com.noseplugapp.android.models.OdorEvent;
import com.noseplugapp.android.models.OdorReport;
import com.noseplugapp.android.models.User;

import com.google.firebase.database.*;
import com.noseplugapp.android.models.Wallpost;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Online version of the Noseplug API that communicates with the Firebase backend.
 */
public class FirebaseApi implements NoseplugApiInterface,
        FirebaseAuth.AuthStateListener, OnCompleteListener<AuthResult> {

    private static final String TAG = FirebaseApi.class.getSimpleName();

    private final Context ctx;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    private OfflineApi offline = new OfflineApi();

    private ArrayList<OdorEvent> odorEvents = new ArrayList<OdorEvent>();
    private ArrayList<OdorReport> odorReports = new ArrayList<OdorReport>();

    public FirebaseApi(Context ctx) {
        this.ctx = ctx;
        auth.addAuthStateListener(this);
        auth.signInAnonymously().addOnCompleteListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
        } else {
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }

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
        else
        {
            db.getReference().child("events").addValueEventListener(OdorEventsListener);
            db.getReference().child("reports").addValueEventListener(OdorReportsListener);
            db.getReference().child("wallposts").addValueEventListener(WallPostsListener);
        }
    }

    @Override
    public OdorEvent getOdorEvent(UUID uuid) {
        OdorEvent found = null;
        for(OdorEvent e : odorEvents) {
            if (e.getId().equals(uuid)) {
                found = e;
            }
        }
        return found;
    }

    @Override
    public OdorReport getOdorReport(UUID uuid) {
        OdorReport found = null;
        for(OdorReport r : odorReports) {
            if (r.getId().equals(uuid)) {
                found = r;
            }
        }
        return found;
    }

    @Override
    public User getUser(UUID uuid) {

        return offline.getUser(uuid);
    }

    @Override
    public UUID addOdorEvent(OdorEvent event) {
        try {
            db.getReference().child("events").child(event.getId().toString()).child("name").setValue(event.getName());
            db.getReference().child("events").child(event.getId().toString()).child("ownerid").setValue(event.ownerid);
            db.getReference().child("events").child(event.getId().toString()).child("reportids").setValue(event.reportids);
            db.getReference().child("events").child(event.getId().toString()).child("subscriberids").setValue(event.getSubscribers());
        }
        catch (DatabaseException e)
        {
            Log.d(TAG, "Error adding odor event: ", e);

        }
        return event.getId();
    }

    @Override
    public UUID addOdorReport(OdorReport report) {
        db.getReference().child("reports").child(report.getId().toString()).child("filingTime").setValue(report.getFilingTime());
        db.getReference().child("reports").child(report.getId().toString()).child("odor").setValue(report.getOdor());
        db.getReference().child("reports").child(report.getId().toString()).child("location").setValue(report.getLocation());
        db.getReference().child("reports").child(report.getId().toString()).child("userid").setValue(report.getUser().getUuid().toString());
        return report.getId();
    }

    @Override
    public void addWallPost(Wallpost post, String eventID) {
        db.getReference().child("wallposts").child(eventID).child(post.id).setValue(post);
    }

    @Override
    public Iterable<OdorEvent> getOdorEvents() {
        return odorEvents;
    }

    @Override
    public UUID registerUser(User user) {

        return offline.registerUser(user);
    }

    ValueEventListener OdorEventsListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot v : dataSnapshot.getChildren()) {
                OdorEvent newEvent = v.getValue(OdorEvent.class);
                newEvent.setFirebaseId(v.getKey());
                if (getOdorEvent(newEvent.getId()) != null) {
                    odorEvents.remove(getOdorEvent(newEvent.getId()));
                }
                odorEvents.add(newEvent);
            }
            EventBus.getDefault().post(new DataChangedEvent());
            Log.w(TAG, "onDataChange");
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        }
    };

    ValueEventListener OdorReportsListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot v : dataSnapshot.getChildren()) {
                OdorReport newReport = v.getValue(OdorReport.class);
                newReport.setFirebaseId(v.getKey());
                if (getOdorReport(newReport.getId()) != null) {
                    odorReports.remove(getOdorEvent(newReport.getId()));
                }
                odorReports.add(newReport);
            }
            EventBus.getDefault().post(new DataChangedEvent());
            Log.w(TAG, "onDataChange");
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        }
    };

    ValueEventListener WallPostsListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot v : dataSnapshot.getChildren()) {
                for(DataSnapshot e : v.getChildren()) {
                    Wallpost newPost = e.getValue(Wallpost.class);
                    OdorEvent o = getOdorEvent(UUID.fromString(v.getKey()));
                    if (o == null)
                    {
                        OdorEvent t = new OdorEvent();
                        t.setFirebaseId(v.getKey());
                        odorEvents.add(t);
                        o = t;
                    }
                    else {
                        o.getWallposts().remove(newPost);
                    }
                    o.addWallpost(newPost);
                }
            }
            EventBus.getDefault().post(new CommentAddedEvent());
            Log.w(TAG, "onDataChange");
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        }
    };
}



