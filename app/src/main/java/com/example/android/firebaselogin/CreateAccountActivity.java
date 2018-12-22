package com.example.android.firebaselogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.android.firebaselogin.AccountManager.RC_SIGN_IN_GOOGLE;

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
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Initialize UI views
        createEmailLoginButton = findViewById(R.id.createAccountEmailButton);
        createGoogleLoginButton = findViewById(R.id.createAccountGoogleButton);
        createFacebookLoginButton = findViewById(R.id.createAccountFacebookButton);
        createSwitchAuthentication = findViewById(R.id.textViewSignUp);

        // Button listeners
        createEmailLoginButton.setOnClickListener(this);
        createGoogleLoginButton.setOnClickListener(this);
        createFacebookLoginButton.setOnClickListener(this);
        createSwitchAuthentication.setOnClickListener(this);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        accountManager = new AccountManager(this, new ProgressDialog(this));
    }

    @Override
    public void onStart() {
        super.onStart();
        accountManager.directIfUserLoggedIn(MainActivity.class);
    }

    @Override
    public void onClick(final View view) {
        if (view == createEmailLoginButton) {
            Intent intent = new Intent(getApplicationContext(), EmailAuthActivity.class);
            intent.putExtra(getText(R.string.AuthAction).toString(), getText(R.string.CreateAccount).toString());
            startActivity(intent);
        } else if (view == createGoogleLoginButton) {
            googleSignUp();
        } else if (view == createFacebookLoginButton) {

        } else if (view == createSwitchAuthentication) {
            switchToLoginActivity(view);
        }
    }


    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                accountManager.firebaseAuthWithGoogle(account);
            } catch (final ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * User wants to log in instead of create an account. Switch to
     * the LoginActivity.
     */
    public void switchToLoginActivity(final View view) {
        final TextView curView = (TextView) view;
        curView.setTextColor(Color.parseColor(DARK_GRAY));
        finish();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Start the sign-in process for google sign-in.
     */
    private void googleSignUp() {
        final Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

}