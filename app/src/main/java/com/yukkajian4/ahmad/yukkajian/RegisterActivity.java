package com.yukkajian4.ahmad.yukkajian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText UserEmail, UserPassword, UserConfirmPassword;
    private Button CreateAccountButton, LoginAccountButton;

    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        UserEmail = (EditText) findViewById(R.id.register_email);
        UserPassword = (EditText) findViewById(R.id.register_password);
        UserConfirmPassword = (EditText) findViewById(R.id.register_confirm_password);

        LoginAccountButton = (Button)findViewById(R.id.register_button_login);


        LoginAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendToLogin();

            }
        });


        CreateAccountButton = (Button) findViewById(R.id.register_create_account);
        loadingBar = new ProgressDialog(this);
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateNewAccount();
            }
        });
    }

    private void SendToLogin() {

        Intent registerToLogin  = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(registerToLogin);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            SendUserToSetupActivity();

        }
    }

    private void CreateNewAccount() {

        String Email = UserEmail.getText().toString();
        String Password = UserPassword.getText().toString();
        String ConfirmPassword = UserConfirmPassword.getText().toString();


        if (TextUtils.isEmpty(Email)) {

            Toast.makeText(this, "PLease write your email", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(Password)) {

            Toast.makeText(this, "Please write your Password", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(ConfirmPassword)) {

            Toast.makeText(this, "Please write confirm password", Toast.LENGTH_SHORT).show();
        } else if (!Password.equals(ConfirmPassword)) {

            Toast.makeText(this, "You password do not same", Toast.LENGTH_SHORT).show();
        } else {

            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please Wait Loading Acoount");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        SendUserToSetupActivity();

                        Toast.makeText(RegisterActivity.this, "success regiter", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    } else {

                        String message = task.getException().getMessage();
                        Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    }
                }
            });
        }
    }

    private void SendUserToSetupActivity() {

        Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();

    }
}
