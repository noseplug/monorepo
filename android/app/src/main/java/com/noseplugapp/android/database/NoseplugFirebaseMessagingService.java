package com.noseplugapp.android.database;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.noseplugapp.android.MapsActivity;
import com.noseplugapp.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static android.R.attr.data;

public class NoseplugFirebaseMessagingService extends FirebaseMessagingService{
    private static final String TAG = NoseplugFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "Message received from: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() <= 0) {
            Log.e(TAG, "Missing data payload");
            return;
        }

        JSONObject event;
        try {
            event = new JSONObject(remoteMessage.getData().get("event"));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Unable to get parse event payload");
            return;
        }

        JSONObject report;
        try {
            report = new JSONObject(remoteMessage.getData().get("report"));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Unable to get parse report payload");
            return;
        }

        Log.d(TAG, "Message data.event: " + event);
        Log.d(TAG, "Message data.report: " + report);

        String content;
        try {
            content = report.getJSONObject("odor").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Couldn't get new event report data");
            return;
        }

        Intent intent = new Intent(this, MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_add_white_24dp)
                .setContentTitle("New Odor Event")
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
