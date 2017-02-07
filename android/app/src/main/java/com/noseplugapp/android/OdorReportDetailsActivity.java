package com.noseplugapp.android;

import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.noseplugapp.android.models.OdorReport;

import java.util.UUID;

import static com.noseplugapp.android.IntentExtraNames.ODOR_REPORT_ID;

public class OdorReportDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odor_report_details);
        setTitle("Odor Report Details"); // TODO: find out where this shoud be set

        UUID odorReportId = ((ParcelUuid) getIntent().getParcelableExtra(ODOR_REPORT_ID)).getUuid();
        Log.d("asd", odorReportId.toString());
        OdorReport odorReport = ApplicationState.getInstance().getOdorReport(odorReportId);

        ((TextView) findViewById(R.id.odor_report_details_textview))
                .setText(odorReport.toString());
    }
}
