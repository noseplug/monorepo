package com.noseplugapp.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {
    private EditText usernameEt;
    private EditText emailEt;
    private EditText passwordEt;
    private EditText retypePwdEt;
    private Button submitBtn;
    private String username;
    private String password;
    private String email;
    private String retypePwd;
    private TextView link2Login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Thank you for joining us");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        usernameEt = (EditText) findViewById(R.id.reg_username);
        emailEt = (EditText) findViewById(R.id.reg_email);
        passwordEt = (EditText) findViewById(R.id.reg_password);
        retypePwdEt = (EditText) findViewById(R.id.retype_password);
        submitBtn = (Button) findViewById(R.id.btnRegister);
        link2Login = (TextView) findViewById(R.id.link_to_login);
        submitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                username = usernameEt.getText().toString();
                password = passwordEt.getText().toString();
                retypePwd = retypePwdEt.getText().toString();
                email = emailEt.getText().toString();
                //TODO: Check against the database if the username/email exists already

                //check email validity
                if (!isValidEmail(email)) {
                    Toast.makeText(getApplicationContext(), "Your email is not valid!",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                //check password against password standards
                if (password.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password must be at least length of 8!",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                //check if password matches
                if (!password.equals(retypePwd)) {
                    Toast.makeText(getApplicationContext(), "Password does not match!",
                            Toast.LENGTH_LONG).show();
                    return;
                }


            }
        });
        link2Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
