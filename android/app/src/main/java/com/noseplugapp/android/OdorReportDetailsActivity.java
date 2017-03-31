package com.noseplugapp.android;

import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.UUID;

import com.noseplugapp.android.database.OfflineApi;
import com.noseplugapp.android.models.OdorReport;

public class OdorReportDetailsActivity extends AppCompatActivity {

    private final App app = App.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odor_report_details);
        setTitle("Odor Report Details"); // TODO: find out where this shoud be set

        UUID odorReportId = ((ParcelUuid) getIntent().getParcelableExtra(
                getResources().getString(R.string.intent_extra_odor_report_id))
        ).getUuid();

        Log.d("asd", odorReportId.toString());
        OdorReport odorReport = app.api().getOdorReport(odorReportId);

        ((TextView) findViewById(R.id.odor_report_details_textview))
                .setText(odorReport.toString());
    }
}
