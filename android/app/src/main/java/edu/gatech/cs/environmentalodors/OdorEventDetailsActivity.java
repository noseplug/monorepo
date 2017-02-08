package edu.gatech.cs.environmentalodors;

import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.UUID;

import edu.gatech.cs.environmentalodors.database.OfflineAPI;
import edu.gatech.cs.environmentalodors.models.OdorEvent;
import edu.gatech.cs.environmentalodors.models.OdorReport;

import static edu.gatech.cs.environmentalodors.IntentExtraNames.ODOR_EVENT_ID;
import static edu.gatech.cs.environmentalodors.IntentExtraNames.ODOR_REPORT_ID;

public class OdorEventDetailsActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {
    private static final String TAG = OdorEventDetailsActivity.class.getSimpleName();

    private OdorEvent odorEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_odor_event_details);
        setSupportActionBar((Toolbar) this.findViewById(R.id.toolbar));

        UUID odorEventId = ((ParcelUuid) getIntent().getParcelableExtra(ODOR_EVENT_ID)).getUuid();
        odorEvent = OfflineAPI.noseplug.getOdorEvent(odorEventId);
        String description = odorEvent.getOdorReports().get(0).odor.description;

        Log.v(TAG, String.format("Starting activity with odor event %s (%s)",
                odorEventId, description));

        ListView listView = (ListView) this.findViewById(R.id.odor_report_list);
        listView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, odorEvent.getOdorReports()));
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.v(TAG, "Received click for odor report at index " + position);
        Intent intent = new Intent(this, OdorReportDetailsActivity.class);
        OdorReport report = odorEvent.getOdorReports().get(position);
        intent.putExtra(ODOR_REPORT_ID, new ParcelUuid(report.uuid));
        startActivity(intent);
    }
}
