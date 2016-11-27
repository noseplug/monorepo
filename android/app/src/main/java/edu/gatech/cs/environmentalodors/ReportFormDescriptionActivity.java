package edu.gatech.cs.environmentalodors;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import edu.gatech.cs.environmentalodors.models.Odor;

public class ReportFormDescriptionActivity extends AppCompatActivity {
    private Odor.Type[] typeSpinner;
    private Odor.Strength[] strengthSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_form_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Report Form - Details");
        Button next = (Button) findViewById(R.id.submit_btn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Congratulations! You have successfully submitted an odor report!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        this.typeSpinner = Odor.Type.values();
        String[] strTypeSpinner = new String[typeSpinner.length];
        for (int i = 0; i < typeSpinner.length; i++) {
            strTypeSpinner[i] = typeSpinner[i].toString();
        }
        Spinner s = (Spinner) findViewById(R.id.type_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, strTypeSpinner);
        s.setAdapter(adapter);

        this.strengthSpinner = Odor.Strength.values();
        String[] strStrengthSpinner = new String[strengthSpinner.length];
        for (int i = 0; i < strengthSpinner.length; i++) {
            strStrengthSpinner[i] = strengthSpinner[i].toString();
        }
        Spinner strengthSpinner = (Spinner) findViewById(R.id.strength_spinner);
        ArrayAdapter<String> strengthAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, strStrengthSpinner);
        strengthSpinner.setAdapter(strengthAdapter);
    }

}
