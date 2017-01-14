package edu.gatech.cs.environmentalodors;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
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

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static edu.gatech.cs.environmentalodors.IntentExtraNames.CREATE_DATE;
import static edu.gatech.cs.environmentalodors.IntentExtraNames.LOCATION;
import static edu.gatech.cs.environmentalodors.IntentExtraNames.REPORT_DATE;

public class ReportFormDateTimeActivity extends AppCompatActivity {
    private EditText dateEditText;
    private Calendar myCalendar = Calendar.getInstance();

    private boolean firstClick = true;

    private Date reportDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Report Form - Select Date and Time");

        Button next = (Button) findViewById(R.id.right_arrow);
        dateEditText = (EditText) findViewById(R.id.select_date);

        final LatLng location = getIntent().getParcelableExtra(LOCATION);
        final Date createDate = new Date(System.currentTimeMillis());

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportFormDateTimeActivity.this, ReportFormDescriptionActivity.class);
                intent.putExtra(LOCATION, location);
                intent.putExtra(REPORT_DATE, reportDate);
                intent.putExtra(CREATE_DATE, createDate);
                startActivityForResult(intent, 0);
                finish();
            }
        });

        final DatePickerDialog.OnDateSetListener dateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };


        dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                if (event.getAction() != MotionEvent.ACTION_DOWN) {
                    return false;
                }
                if (dateEditText.isFocused()) {
                    Rect outRect = new Rect();
                    dateEditText.getGlobalVisibleRect(outRect);
                    if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                        dateEditText.clearFocus();
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                return false;
            }
        });
    }
    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        reportDate = myCalendar.getTime();
        dateEditText.setText(sdf.format(myCalendar.getTime()));
    }

}
