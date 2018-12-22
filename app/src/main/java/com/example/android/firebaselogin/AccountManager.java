package com.example.android.firebaselogin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

class AccountManager {
    // Constants
    static final int RC_SIGN_IN = 9001;

    // AccountManager objects
    private FirebaseAuth mAuth = null;
    private Context context = null;

    AccountManager(final Context context) {
        this.mAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    /**
     * Direct the user to the logged in pages if they are already authenticated
     */
    void directIfUserLoggedIn(Class activity) {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            // User is signed in already.
            context.startActivity(new Intent(context.getApplicationContext(), activity));
    }

    /**
     * Direct the user to the authentication pages  if the user is not logged in
     */
    void directIfUserNotLoggedIn(Class activity) {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null)
            // User is signed in already.
            context.startActivity(new Intent(context.getApplicationContext(), activity));
    }

    void loginEmailAccount(final String email, final String password,
                           final Context context, final ProgressDialog progressDialog) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context.getApplicationContext(), MainActivity.class));
                        } else
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
    }

    void createNewEmailAccount(final String email, final String password,
                               final String passwordAgain,
                               final ProgressDialog progressDialog) {

        if (email.isEmpty() || password.isEmpty() || passwordAgain.isEmpty()) {
            Toast.makeText(context, context.getText(R.string.EmptyField).toString(), Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(passwordAgain)) {
            Toast.makeText(context, context.getText(R.string.PasswordMismatch).toString(), Toast.LENGTH_LONG).show();
            return;
        }

        // if validations are ok
        // we will first show a progressbar
        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context.getApplicationContext(), MainActivity.class));
                        } else
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();
                    }
                });
    }

    void firebaseAuthWithGoogle(final GoogleSignInAccount acct, final Activity activity) {

        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context.getApplicationContext(), MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
