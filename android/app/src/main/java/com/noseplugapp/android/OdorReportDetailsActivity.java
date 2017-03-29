package com.noseplugapp.android;

import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import java.util.UUID;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.noseplugapp.android.database.OfflineApi;
import com.noseplugapp.android.events.CreateOdorReportEvent;
import com.noseplugapp.android.models.OdorReport;

import org.greenrobot.eventbus.EventBus;

public class OdorReportDetailsActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Button add_report;
    private final App app = App.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odor_report_details);
        setTitle("Odor Report Details"); // TODO: find out where this shoud be set

        mDatabase = FirebaseDatabase.getInstance().getReference();

        UUID odorReportId = ((ParcelUuid) getIntent().getParcelableExtra(
                getResources().getString(R.string.intent_extra_odor_report_id))
        ).getUuid();

        Log.d("asd", odorReportId.toString());
        final OdorReport odorReport = app.api().getOdorReport(odorReportId);

        ((TextView) findViewById(R.id.odor_report_details_textview))
                .setText(odorReport.toString());

//        add_report = (Button) findViewById(R.id.add_report);
//        add_report.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        add_report = (Button) findViewById(R.id.add_report);
        add_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(MapsActivity.class.getSimpleName(), "Clicked button, launching odor report activity");
                CreateOdorReportEvent newOdorReport = new CreateOdorReportEvent(odorReport.getLocation());
                EventBus.getDefault().post(newOdorReport);
                ((TextView) findViewById(R.id.odor_report_details_textview))
                        .setText(odorReport.toString());
            }
        });
    }
}
