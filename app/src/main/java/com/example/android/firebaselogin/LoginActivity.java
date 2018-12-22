package com.example.android.firebaselogin;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static com.example.android.firebaselogin.AccountManager.RC_SIGN_IN;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    // Constants
    private static final String DARK_GRAY = "#A9A9A9";

    // Activity objects
    private FirebaseAuth mAuth = null;
    private AccountManager accountManager = null;
    private GoogleSignInClient mGoogleSignInClient;

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

        // Button listeners
        emailLoginButton.setOnClickListener(this);
        googleLoginButton.setOnClickListener(this);
        facebookLoginButton.setOnClickListener(this);
        switchAuthenticationButton.setOnClickListener(this);

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
        accountManager = new AccountManager(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        accountManager.directIfUserLoggedIn(MainActivity.class);
    }

    @Override
    public void onClick(final View view) {
        if (view == emailLoginButton) {
            final Intent intent = new Intent(getApplicationContext(), EmailAuthActivity.class);
            intent.putExtra(getText(R.string.AuthAction).toString(), getText(R.string.Login).toString());
            startActivity(intent);
        } else if (view == googleLoginButton) {
            // Configure Google Sign In
            googleSignIn();
        } else if (view == facebookLoginButton) {

        } else if (view == switchAuthenticationButton) {
            switchToCreateAccountActivity(view);
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                accountManager.firebaseAuthWithGoogle(account, this);
            } catch (final ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * User wants to create an account instead of log in. Switch to
     * the CreateAccountActivity.
     *
     * @param view The view being pressed to switch authentication functionality.
     */
    private void switchToCreateAccountActivity(final View view) {
        final TextView curView = (TextView) view;
        curView.setTextColor(Color.parseColor(DARK_GRAY));
        finish();
        final Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Start the sign-in process for google sign-in.
     */
    private void googleSignIn() {
        final Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

}
