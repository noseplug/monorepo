package com.noseplugapp.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEt;
    private EditText passwordEt;
    private Button submitBtn;
    private String username;
    private String password;
    private TextView link2Register;
    private TextView link2Pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Welcome back");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        usernameEt = (EditText) findViewById(R.id.login_username);
        passwordEt = (EditText) findViewById(R.id.login_password);
        submitBtn = (Button) findViewById(R.id.btnLogin);
        link2Register = (TextView) findViewById(R.id.link_to_register);
        link2Pwd = (TextView) findViewById(R.id.link_to_reset) ;
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameEt.getText().toString();
                password = passwordEt.getText().toString();
                //TODO: Check against the database if the username/email match
                Toast.makeText(getApplicationContext(), "Congratulations! You have successfully logged in!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, ReportFormDateTimeActivity.class);
                startActivity(intent);



            }
        });
        link2Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
        link2Pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ResetEmailActivity.class);
                startActivity(intent);
            }
        });
    }

}
