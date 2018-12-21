package com.example.android.firebaselogin;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    // Constants
    private static final String DARK_GRAY = "#A9A9A9";

    // Activity objects
    private FirebaseAuth mAuth = null;
    AccountManager accountManager = null;

    // UI elements
    private Button emailLoginButton = null;
    private Button googleLoginButton = null;
    private Button facebookLoginButton = null;
    private TextView switchAuthenticationButton = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI views.
        emailLoginButton = findViewById(R.id.loginEmailButton);
        googleLoginButton = findViewById(R.id.loginGoogleButton);
        facebookLoginButton = findViewById(R.id.loginFacebookButton);
        switchAuthenticationButton = findViewById(R.id.textViewSignUp);

        emailLoginButton.setOnClickListener(this);
        googleLoginButton.setOnClickListener(this);
        facebookLoginButton.setOnClickListener(this);
        switchAuthenticationButton.setOnClickListener(this);

        accountManager = new AccountManager(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        accountManager.directUser(MainActivity.class);
    }

    @Override
    public void onClick(final View view) {
        if (view == emailLoginButton) {

        } else if (view == googleLoginButton) {

        } else if (view == facebookLoginButton) {

        } else if (view == switchAuthenticationButton) {
            switchToCreateAccountActivity(view);
        }
    }

    /**
     * Switch to the CreateAccountActivity class.
     *
     * @param view
     */
    private void switchToCreateAccountActivity(final View view) {
        final TextView curView = (TextView) view;
        curView.setTextColor(Color.parseColor(DARK_GRAY));
        finish();
        Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
