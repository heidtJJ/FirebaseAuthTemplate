package com.example.android.firebaselogin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountManager {
    private FirebaseAuth mAuth = null;
    private Context context = null;

    public AccountManager(final Context context) {
        this.mAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    /**
     * Direct the user to the logged in pages if they are already authenticated
     */
    public void directUser(Class activity) {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            // User is signed in already.
            context.startActivity(new Intent(context.getApplicationContext(), activity));
        else
            // User not signed in.
            mAuth = FirebaseAuth.getInstance();
    }

    public void createNewEmailAccount(final String email, final String password,
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
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, context.getText(R.string.AuthFailed).toString(), Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }
}
