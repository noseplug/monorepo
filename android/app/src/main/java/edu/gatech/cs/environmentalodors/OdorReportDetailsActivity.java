package edu.gatech.cs.environmentalodors;

import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.UUID;

import edu.gatech.cs.environmentalodors.models.OdorEvent;
import edu.gatech.cs.environmentalodors.models.OdorReport;

import static edu.gatech.cs.environmentalodors.IntentExtraNames.ODOR_EVENT_ID;
import static edu.gatech.cs.environmentalodors.IntentExtraNames.ODOR_REPORT_INDEX;

public class OdorReportDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odor_report_details);
        setTitle("Odor Report Details"); // TODO: find out where this shoud be set

        UUID odorEventId = ((ParcelUuid) getIntent().getParcelableExtra(ODOR_EVENT_ID)).getUuid();
        OdorEvent odorEvent = ApplicationState.getInstance().getOdorEvent(odorEventId);

        // TODO: sanity check index and handle error condition
        int odorReportIndex = getIntent().getIntExtra(ODOR_REPORT_INDEX, -1);

        OdorReport odorReport = odorEvent.getOdorReports().get(odorReportIndex);
        ((TextView) findViewById(R.id.odor_report_details_textview))
                .setText(odorReport.toString());
    }
}
