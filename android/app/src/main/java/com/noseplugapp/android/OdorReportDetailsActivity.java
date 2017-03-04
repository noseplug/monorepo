package com.noseplugapp.android;

import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.UUID;

import com.noseplugapp.android.database.OfflineApi;
import com.noseplugapp.android.models.OdorReport;

import static com.noseplugapp.android.IntentExtraNames.ODOR_REPORT_ID;

public class OdorReportDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odor_report_details);
        setTitle("Odor Report Details"); // TODO: find out where this shoud be set

        UUID odorReportId = ((ParcelUuid) getIntent().getParcelableExtra(ODOR_REPORT_ID)).getUuid();
        Log.d("asd", odorReportId.toString());
        OdorReport odorReport = OfflineApi.noseplug.getOdorReport(odorReportId);

        ((TextView) findViewById(R.id.odor_report_details_textview))
                .setText(odorReport.toString());
    }
}
