package com.example.android.firebaselogin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EmailAuthActivity extends AppCompatActivity {

    // UI elements
    private EditText emailEditText = null;
    private EditText passwordEditText = null;
    private EditText passwordAgainEditText = null;

    // Activity objects
    private AccountManager accountManager = null;

    // Indicates whether the user is logging in or creating a new account
    private boolean userLoggingIn = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_auth);

        String authAction = getIntent().getStringExtra(getText(R.string.AuthAction).toString());

        // Initialize UI views.
        emailEditText = findViewById(R.id.emailField);
        passwordEditText = findViewById(R.id.passwordField);
        passwordAgainEditText = findViewById(R.id.passwordAgainField);

        // Determine whether user is logging in or creating account.
        if (authAction.equals(getText(R.string.Login).toString())) {
            passwordAgainEditText.setVisibility(View.GONE);
            userLoggingIn = true;
        }

        // Initialize activity objects.
        accountManager = new AccountManager(this, new ProgressDialog(this));
    }

    @Override
    public void onStart() {
        super.onStart();
        accountManager.directIfUserLoggedIn(MainActivity.class);
    }

    /**
     * This method is an onClick listener for the create-account button. The method will create a
     * new account associated with the email address depending on error checking results.
     *
     * @param view the button clicked to create a new account
     */
    public void createEmailAccount(final View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordAgain = passwordAgainEditText.getText().toString();

        // Check if any of the required fields are empty.
        if (email.isEmpty() || password.isEmpty() || (passwordAgain.isEmpty() && !userLoggingIn)) {
            Toast.makeText(this, getText(R.string.EmptyField).toString(), Toast.LENGTH_LONG).show();
            return;
        }

        // Perform account creation depending on whether user is logging in or creating account.
        if (userLoggingIn)
            accountManager.loginEmailAccount(email, password, this);
        else if (!password.equals(passwordAgain))
            Toast.makeText(this, getText(R.string.PasswordMismatch).toString(), Toast.LENGTH_LONG).show();
        else
            accountManager.createNewEmailAccount(email, password, passwordAgain);
    }
}
