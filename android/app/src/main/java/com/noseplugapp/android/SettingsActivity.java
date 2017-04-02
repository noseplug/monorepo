package com.noseplugapp.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = SettingsActivity.class.getSimpleName();
    public static final int DEFAULT_NEW_EVENT_NOTIFICATION_DISTANCE_KM = 1000;

    private SharedPreferences preferences;
    private EditText newEventNotificationDistanceEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        preferences = getSharedPreferences(getString(R.string.shared_preferences_noseplug), Context.MODE_PRIVATE );
        newEventNotificationDistanceEditText = (EditText) findViewById(R.id.settings_newEventNotificationDistance);

        String dist = Integer.toString(
                preferences.getInt(
                        getString(R.string.shared_preferences_newEventNotificationDistance),
                        DEFAULT_NEW_EVENT_NOTIFICATION_DISTANCE_KM));

        Editable editable = newEventNotificationDistanceEditText.getText();
        editable.clear();
        editable.append(dist);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            int dist = Integer.parseInt(newEventNotificationDistanceEditText.getText().toString());

            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(getString(R.string.shared_preferences_newEventNotificationDistance),
                    dist);
            editor.apply();
            Log.d(TAG, "Setting NewEventNotificationDistance to " + dist);

        } catch (NumberFormatException nfe) {
            Log.e(TAG, nfe.getMessage());
        }
    }
}
