package com.noseplugapp.android.database;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.noseplugapp.android.App;
import com.noseplugapp.android.MapsActivity;
import com.noseplugapp.android.R;
import com.noseplugapp.android.utils.NoseplugLatLng;

import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Math.*;

public class NoseplugFirebaseMessagingService extends FirebaseMessagingService{
    private static final String TAG = NoseplugFirebaseMessagingService.class.getSimpleName();
    private final App app = App.getInstance();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Log.d(TAG, "Message received from: " + remoteMessage.getFrom());
            if (remoteMessage.getData().size() <= 0) {
                Log.e(TAG, "Missing data payload");
                return;
            }

            JSONObject event = new JSONObject(remoteMessage.getData().get("event"));
            JSONObject report = new JSONObject(remoteMessage.getData().get("report"));
            Log.d(TAG, "Message data.event: " + event);
            Log.d(TAG, "Message data.report: " + report);

            String content = report.getJSONObject("odor").toString();
            String location = report.getString("location");

            Gson gson = new Gson();
            LatLng eventLocation = gson.fromJson(location, NoseplugLatLng.class).toGoogleLatLng();
            Log.i(TAG, "New event occurred at " + eventLocation);

            double distance = greatCircleDistanceKm(app.getUserLastKnownLocation(), eventLocation);
            if (distance > 1000.0) {
                Log.i(TAG, String.format("Event too far away (%f km), not notifying user", distance));
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Calculate the great-circle distance between two coordinates in kilometers.
    // Approx radius of the earth => 6371 km
    // https://en.wikipedia.org/wiki/Great-circle_distance
    private static double greatCircleDistanceKm(LatLng a, LatLng b) {
        return 6371.0 * acos(
                sin(a.latitude) * sin(b.latitude) +
                cos(a.latitude) * cos(b.latitude) * cos(abs(a.longitude - b.longitude))
        );
    }
}
