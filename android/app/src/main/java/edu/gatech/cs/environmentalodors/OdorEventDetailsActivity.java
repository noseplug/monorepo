package edu.gatech.cs.environmentalodors;

import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.UUID;

import edu.gatech.cs.environmentalodors.models.OdorEvent;

import static edu.gatech.cs.environmentalodors.IntentExtraNames.REPORT_ID;

public class OdorEventDetailsActivity extends AppCompatActivity {
    private static final String TAG = OdorEventDetailsActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odor_event_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        UUID reportID = ((ParcelUuid) getIntent().getParcelableExtra(REPORT_ID)).getUuid();
        OdorEvent r = ApplicationState.getInstance().getOdorEvent(reportID);

        TextView descriptionBox = (TextView) findViewById(R.id.description);
        assert r != null;
        assert r.getFirstOdorReport() != null;
        assert r.getFirstOdorReport().getOdor() != null;
        assert r.getFirstOdorReport().getOdor().getDescription() != null;
        Log.v(TAG, r.getFirstOdorReport().getOdor().getDescription());
        descriptionBox.append(r.getFirstOdorReport().getOdor().getDescription());
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
