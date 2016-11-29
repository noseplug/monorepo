package edu.gatech.cs.environmentalodors;

import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.UUID;

import edu.gatech.cs.environmentalodors.models.OdorEvent;

import static edu.gatech.cs.environmentalodors.IntentExtraNames.ODOR_EVENT_ID;

public class OdorEventDetailsActivity extends AppCompatActivity {
    private static final String TAG = OdorEventDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_odor_event_details);
        setSupportActionBar((Toolbar) this.findViewById(R.id.toolbar));

        UUID reportID = ((ParcelUuid) getIntent().getParcelableExtra(ODOR_EVENT_ID)).getUuid();
        OdorEvent odorEvent = ApplicationState.getInstance().getOdorEvent(reportID);
        String description = odorEvent.getOdorReports().get(0).getOdor().getDescription();

        Log.v(TAG, String.format("Starting activity with odor event %s (%s)",
                reportID, description));

        ListView listView = (ListView) this.findViewById(R.id.odor_report_list);
        listView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, odorEvent.getOdorReports()));
    }
}
