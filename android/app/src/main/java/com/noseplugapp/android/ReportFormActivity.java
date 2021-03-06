package com.noseplugapp.android;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.noseplugapp.android.events.OdorReportEvent;
import com.noseplugapp.android.models.Odor;
import com.noseplugapp.android.models.OdorEvent;
import com.noseplugapp.android.models.OdorReport;
import com.noseplugapp.android.models.User;

public class ReportFormActivity extends AppCompatActivity {
    private Calendar myCalendar = Calendar.getInstance();

    private DatabaseReference mDatabase;

    private boolean firstClick = true;

    private Date reportDate;
    private UUID eventId;

    private Date reportCreateDate = new Date(System.currentTimeMillis());
    private LatLng odorLocation;

    // BEGIN form
    // TODO: Note how there is no place in the model for start/end time and wallposts.
    //       The model object is incomplete.
    private EditText reportDateEditText;
    // private TimePicker startTimePicker;
    // private TimePicker endTimePicker;
    private Spinner odorTypeSpinner;
    private Spinner odorStrengthSpinner;
    // private EditText odorCommentsEditText;
    private EditText odorEffectEditText;

    // END form

    private void findFormViews() {
        reportDateEditText = (EditText) findViewById(R.id.report_date);
        // startTimePicker = (TimePicker) findViewById(R.id.start_time);
        // endTimePicker = (TimePicker) findViewById(R.id.end_time);
        odorTypeSpinner = (Spinner) findViewById(R.id.odor_type);
        odorStrengthSpinner = (Spinner) findViewById(R.id.odor_strength);
        // odorCommentsEditText = (EditText) findViewById(R.id.odor_comments);
        odorEffectEditText = (EditText) findViewById(R.id.odor_effect);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_form);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        findFormViews();

        odorLocation = getIntent().getParcelableExtra(
                getResources().getString(R.string.intent_extra_location));

        eventId = UUID.fromString(getIntent().getStringExtra(getResources().getString(R.string.intent_extra_odor_event_id)));

        odorTypeSpinner.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Odor.Type.values()));
        odorStrengthSpinner.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Odor.Strength.values()));

        findViewById(R.id.submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // HACK: this should be done along the way but I need something to test the maps and show event page with
                Odor odor = new Odor(
                        Odor.Strength.values()[(int) odorStrengthSpinner.getSelectedItemId()],
                        Odor.Type.values()[(int) odorTypeSpinner.getSelectedItemId()],
                        odorEffectEditText.getText().toString());

                // TODO: Fetch the user object from the API, don't just make a new one.
                OdorReport report = new OdorReport(new User(), reportDate, odorLocation, odor);

                mDatabase.child("reports").child(report.getId().toString()).child("filingTime").setValue(report.getFilingTime());
                mDatabase.child("reports").child(report.getId().toString()).child("odor").setValue(report.getOdor());
                mDatabase.child("reports").child(report.getId().toString()).child("location").setValue(report.getLocation());
                mDatabase.child("reports").child(report.getId().toString()).child("userid").setValue(report.getUser().getUuid().toString());

                if (App.getInstance().getIsNew() == false) {
                    OdorEvent event = App.getInstance().api().getOdorEvent(eventId);
                    event.addOdorReport(report);
                    App.getInstance().api().addOdorEvent(event);
                } else {
                    EventBus.getDefault().post(new OdorReportEvent(report));
                }
                // end HACK
                finish();
            }
        });

        final DatePickerDialog.OnDateSetListener dateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                reportDate = myCalendar.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                reportDateEditText.setText(sdf.format(reportDate));
            }
        };

        reportDateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b && firstClick) {
                    InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    firstClick = false;
                }

                if (view.hasFocus()) {
                    new DatePickerDialog(ReportFormActivity.this, dateDialog, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });
    }
}
