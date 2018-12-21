package com.example.android.firebaselogin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {
    // Constants
    private static final String DARK_GRAY = "#A9A9A9";

    // Activity objects
    private FirebaseAuth mAuth = null;
    private AccountManager accountManager = null;

    // UI elements
    private Button createEmailLoginButton = null;
    private Button createGoogleLoginButton = null;
    private Button createFacebookLoginButton = null;
    private TextView createSwitchAuthentication = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Initialize UI views.
        createEmailLoginButton = findViewById(R.id.createAccountEmailButton);
        createGoogleLoginButton = findViewById(R.id.createAccountGoogleButton);
        createFacebookLoginButton = findViewById(R.id.createAccountFacebookButton);
        createSwitchAuthentication = findViewById(R.id.textViewSignUp);

        createEmailLoginButton.setOnClickListener(this);
        createGoogleLoginButton.setOnClickListener(this);
        createFacebookLoginButton.setOnClickListener(this);
        createSwitchAuthentication.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        accountManager = new AccountManager(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        accountManager.directUser(MainActivity.class);
    }

    @Override
    public void onClick(final View view) {
        if (view == createEmailLoginButton) {
            Intent intent = new Intent(getApplicationContext(), EmailAuthActivity.class);
            startActivity(intent);
        } else if (view == createGoogleLoginButton) {

        } else if (view == createFacebookLoginButton) {

        } else if (view == createSwitchAuthentication) {
            switchToLoginActivity(view);
        }
    }


    public void switchToLoginActivity(final View view) {
        final TextView curView = (TextView) view;
        curView.setTextColor(Color.parseColor(DARK_GRAY));
        finish();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}