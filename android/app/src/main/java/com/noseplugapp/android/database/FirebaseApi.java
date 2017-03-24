package com.noseplugapp.android.database;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.noseplugapp.android.models.OdorEvent;
import com.noseplugapp.android.models.OdorReport;
import com.noseplugapp.android.models.User;

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

    public FirebaseApi(Context ctx) {
        this.ctx = ctx;
        auth.addAuthStateListener(this);
        auth.signInAnonymously().addOnCompleteListener(this);

        FirebaseMessaging.getInstance().subscribeToTopic("newOdorEvents");
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
    }

    @Override
    public OdorEvent getOdorEvent(UUID uuid) {
        return null;
    }

    @Override
    public OdorReport getOdorReport(UUID uuid) {
        return null;
    }

    @Override
    public User getUser(UUID uuid) {
        return null;
    }

    @Override
    public UUID addOdorEvent(OdorEvent event) {
        return null;
    }

    @Override
    public Iterable<OdorEvent> getOdorEvents() {
        return new ArrayList<>();
    }

    @Override
    public UUID registerUser(User user) {
        return null;
    }
}
