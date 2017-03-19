package com.noseplugapp.android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.noseplugapp.android.models.OdorEvent;
import com.noseplugapp.android.models.OdorReport;
import com.noseplugapp.android.models.Wallpost;

import java.util.UUID;

public class OdorEventDetailsActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {
    private static final String TAG = OdorEventDetailsActivity.class.getSimpleName();
    private OdorEvent odorEvent;
    private final Context context = this;
    private final App app = App.getInstance();

    private String commentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_odor_event_details);
        setSupportActionBar((Toolbar) this.findViewById(R.id.my_toolbar));

        UUID odorEventId = ((ParcelUuid) getIntent().getParcelableExtra(
                getResources().getString(R.string.intent_extra_odor_event_id))
        ).getUuid();

        odorEvent = app.api().getOdorEvent(odorEventId);
        String description = odorEvent.getOdorReports().get(0).getOdor().getDescription();

        Log.v(TAG, String.format("Starting activity with odor event %s (%s)",
                odorEventId, description));

        final ListView reportList = (ListView) this.findViewById(R.id.odor_report_list);
        reportList.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, odorEvent.getOdorReports()));
        reportList.setOnItemClickListener(this);

        final ListView commentList = (ListView) this.findViewById(R.id.comment_list);
        commentList.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, odorEvent.getWallposts()));

        findViewById(R.id.commentButton).setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Log.v(TAG, "Clicked make comment button");
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);


                alertDialogBuilder
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        commentText = userInput.getText().toString();
                                        Wallpost newWallpost = new Wallpost("You", commentText, Wallpost.Type.normal);
                                        odorEvent.addWallpost(newWallpost);
                                        ((BaseAdapter) commentList.getAdapter()).notifyDataSetChanged();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog commentDialog = alertDialogBuilder.create();
                commentDialog.show();
            }

        });
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.v(TAG, "Received click for odor report at index " + position);
        Intent intent = new Intent(this, OdorReportDetailsActivity.class);
        OdorReport report = odorEvent.getOdorReports().get(position);
        intent.putExtra(getResources().getString(R.string.intent_extra_odor_report_id),
                new ParcelUuid(report.getId()));
        startActivity(intent);
    }
}
