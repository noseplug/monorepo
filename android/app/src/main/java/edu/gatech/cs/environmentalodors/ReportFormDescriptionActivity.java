package edu.gatech.cs.environmentalodors;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import edu.gatech.cs.environmentalodors.events.OdorReportEvent;
import edu.gatech.cs.environmentalodors.models.Odor;
import edu.gatech.cs.environmentalodors.models.OdorReport;
import edu.gatech.cs.environmentalodors.models.User;

import static edu.gatech.cs.environmentalodors.IntentExtraNames.CREATE_DATE;
import static edu.gatech.cs.environmentalodors.IntentExtraNames.LOCATION;
import static edu.gatech.cs.environmentalodors.IntentExtraNames.REPORT_DATE;

public class ReportFormDescriptionActivity extends AppCompatActivity {
    private Spinner strengthSpinner;
    private Spinner typeSpinner;

    private LatLng location;
    private Date reportDate;
    private Date createDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_form_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Report Form - Details");

        strengthSpinner = (Spinner) findViewById(R.id.strength_spinner);
        typeSpinner = (Spinner) findViewById(R.id.type_spinner);

        location = getIntent().getParcelableExtra(LOCATION);
        reportDate = (Date)getIntent().getSerializableExtra(REPORT_DATE);
        createDate = (Date)getIntent().getSerializableExtra(CREATE_DATE);
        Button next = (Button) findViewById(R.id.submit_btn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Congratulations! You have successfully submitted an odor report!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                // HACK: this should be done along the way but I need something to test the maps and show event page with

                Odor odor = new Odor(
                        Odor.Strength.values()[(int) strengthSpinner.getSelectedItemId()],
                        Odor.Type.values()[(int) typeSpinner.getSelectedItemId()],
                        ((EditText) findViewById(R.id.affect)).getText().toString());

                // TODO: Fetch the user object from the API, don't just make a new one.
                OdorReport report = new OdorReport(new User(), createDate, reportDate, location, odor);
                EventBus.getDefault().post(new OdorReportEvent(report));
                // end HACK
                finish();
            }
        });

        String[] strTypeSpinner = new String[Odor.Type.values().length];
        for (int i = 0; i < Odor.Type.values().length; i++) {
            strTypeSpinner[i] = Odor.Type.values()[i].toString();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, strTypeSpinner);
        typeSpinner.setAdapter(adapter);


        String[] strStrengthSpinner = new String[Odor.Strength.values().length];
        for (int i = 0; i < Odor.Strength.values().length; i++) {
            strStrengthSpinner[i] = Odor.Strength.values()[i].toString();
        }
        ArrayAdapter<String> strengthAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, strStrengthSpinner);
        strengthSpinner.setAdapter(strengthAdapter);


    }

}
