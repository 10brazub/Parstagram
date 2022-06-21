package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private EditText etUsername;
    private EditText etPassword;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        ImageView ivInsta = findViewById(R.id.ivInsta);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            Log.i(TAG, "onClick login button");
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            loginUser(username, password);
        });

        Button btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            signupUser(username, password);
            
        });

        ivInsta.setImageDrawable(getResources().getDrawable(R.drawable.parstagram_font));
    }

    private void signupUser(String username, String password) {
        // Creates the ParseUser
        ParseUser user = new ParseUser();
        // Sets core properties
        user.setUsername(username);
        user.setPassword(password);

        // Invokes signUpInBackground
        user.signUpInBackground(e -> {
            if (e == null) {
                Toast.makeText(this, "Signed Up!", Toast.LENGTH_SHORT).show();
                goMainActivity();
            } else {
                Log.e(TAG, "SignUp Failed", e);
                Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "Trying to login user " + username);
        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (e != null) {
                Log.e(TAG, "issue with login", e);
                Toast.makeText(LoginActivity.this, "Wrong Username/Password", Toast.LENGTH_SHORT).show();
                return;
            }
            goMainActivity();
            Toast.makeText(LoginActivity.this, "Logged In!", Toast.LENGTH_SHORT).show();
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}