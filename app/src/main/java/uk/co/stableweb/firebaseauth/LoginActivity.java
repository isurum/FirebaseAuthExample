package uk.co.stableweb.firebaseauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    // Initialisation of subviews
    private EditText inputEmail, inputPassword;
    private Button btnSignup, btnLogin, btnReset;
    private ProgressBar progressBar;

    // Initialisation of FirebaseAuth variable
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get FirebaseAuth instance
        auth = FirebaseAuth.getInstance();

        // Construction of subviews
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        // set the click listener to start sign up activity on click.
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });

        // set the click listener to start password reset activity
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
                finish();
            }
        });

        // set the click listener.
        // send the email and password to verify the login detaisl.
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the email and password from user inputs
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(
                            getApplicationContext(),
                            "Enter email address!",
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(
                            getApplicationContext(),
                            "Enter valid password!",
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Authenticate the user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);

                                if(!task.isSuccessful()){
                                    if(password.length() < 6){
                                        inputPassword.setError(getString(R.string.auth_failed));
                                    }else {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                getString(R.string.auth_failed),
                                                Toast.LENGTH_LONG
                                        ).show();
                                    }
                                }else {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });

        auth.signOut();


    }

}
