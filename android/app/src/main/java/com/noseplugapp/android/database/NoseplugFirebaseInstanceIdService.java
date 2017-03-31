package com.noseplugapp.android.database;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class NoseplugFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = NoseplugFirebaseInstanceIdService.class.getSimpleName();
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }
}
