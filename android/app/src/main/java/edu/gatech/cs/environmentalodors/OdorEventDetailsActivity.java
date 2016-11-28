package edu.gatech.cs.environmentalodors;

import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

        TextView descriptionBox = (TextView) findViewById(R.id.description);
        descriptionBox.append(description);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
