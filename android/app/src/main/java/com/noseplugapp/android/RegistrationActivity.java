package com.noseplugapp.android;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegistrationActivity extends BaseActivity {
    private static final String TAG = "Registration";
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
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]
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
                Log.d(TAG, "createAccount:" + email);
                // showProgressDialog();
                username = usernameEt.getText().toString();
                password = passwordEt.getText().toString();
                retypePwd = retypePwdEt.getText().toString();
                email = emailEt.getText().toString();


                //check empty fields
                if (!validateForm()) {
                    return;
                }

                //check if password matches
                if (!password.equals(retypePwd)) {
                    Toast.makeText(getApplicationContext(), "Password does not match!",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                // [START initialize_auth]
                mAuth = FirebaseAuth.getInstance();
                // [END initialize_auth]

                mAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            // User is signed in
                            Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        } else {
                            // User is signed out
                            Log.d(TAG, "onAuthStateChanged:signed_out");
                        }
                    }
                };
                createAccount(email, password);

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

    private boolean validateForm() {
        boolean valid = true;

        String email = emailEt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEt.setError("Required.");
            valid = false;
        } else {
            emailEt.setError(null);
        }

        String password = passwordEt.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordEt.setError("Required.");
            valid = false;
        } else {
            passwordEt.setError(null);
        }

        return valid;
    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            String message = task.getException().getMessage();
                            if(message.equals("An internal error has occurred. [ WEAK_PASSWORD  ]")) {
                                message = "Minimum length of Password is 6";
                            }
                            Toast.makeText(RegistrationActivity.this, message,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Congratulations! You are now logged in!",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile created.");
                                            }
                                        }
                                    });
                            ActionBar ab = getActionBar();
                            ab.setTitle(user.getDisplayName());
                            Intent intent = new Intent(RegistrationActivity.this, MapsActivity.class);
                            startActivity(intent);
                        }

                    }
                });
        // [END create_user_with_email]
    }
}
