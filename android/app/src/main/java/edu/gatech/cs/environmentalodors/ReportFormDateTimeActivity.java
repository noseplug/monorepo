package edu.gatech.cs.environmentalodors;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static edu.gatech.cs.environmentalodors.IntentExtraNames.SELECTED_LOCATION;

public class ReportFormDateTimeActivity extends AppCompatActivity {
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;
    private EditText date_et;
    Calendar myCalendar = Calendar.getInstance();

    boolean firstClick = true;

    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Report Form - Select Date and Time");

        Button next = (Button) findViewById(R.id.right_arrow);
        startTimePicker = (TimePicker) findViewById(R.id.timePicker);
        endTimePicker = (TimePicker) findViewById(R.id.timePicker2);
        date_et = (EditText) findViewById(R.id.select_date);

        location = getIntent().getParcelableExtra(SELECTED_LOCATION);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateString = date_et.getText().toString();
                int startTimeHour, startTimeMinute;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    startTimeHour= startTimePicker.getHour();
                    startTimeMinute = startTimePicker.getMinute();
                } else {
                    startTimeHour = startTimePicker.getCurrentHour();
                    startTimeMinute = startTimePicker.getCurrentMinute();
                }
                int endTimeHour, endTimeMinute;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    endTimeHour= startTimePicker.getHour();
                    endTimeMinute = startTimePicker.getMinute();
                } else {
                    endTimeHour = startTimePicker.getCurrentHour();
                    endTimeMinute = startTimePicker.getCurrentMinute();
                }
                Intent intent = new Intent(ReportFormDateTimeActivity.this, ReportFormDescriptionActivity.class);
                startActivity(intent);
            }
        });

        final DatePickerDialog.OnDateSetListener dateDialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        date_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b&firstClick) {
                    InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    firstClick = false;

                }
                if (view.hasFocus()) {
                    new DatePickerDialog(ReportFormDateTimeActivity.this, dateDialog, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });
        FrameLayout touchInterceptor = (FrameLayout)findViewById(R.id.touchInterceptor);
        touchInterceptor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (date_et.isFocused()) {
                        Rect outRect = new Rect();
                        date_et.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                            date_et.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });
    }
    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date_et.setText(sdf.format(myCalendar.getTime()));
    }

}