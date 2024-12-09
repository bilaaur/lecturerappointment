package com.example.projectse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        TextView forgotpassword = findViewById(R.id.forgotpassword);
        TextView donthaveacc = findViewById(R.id.donthaveacc);

        forgotpassword.setOnClickListener(v -> {
            Intent openForgotPassword = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
            startActivity(openForgotPassword);
        });

        donthaveacc.setOnClickListener(v -> {
            Intent openRegister = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(openRegister);
        });

        buttonLogin.setOnClickListener(view -> loginUser());
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        // Check if Firebase Auth instance is working
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d("Firebase", "Already logged in: " + currentUser.getEmail());
        } else {
            Log.d("Firebase", "No user logged in.");
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Intent openFaculty = new Intent(getApplicationContext(), FacultyActivity.class);
                        startActivity(openFaculty);

                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Intent openHome = new Intent(LoginActivity.this, LoginActivity.class);
                            startActivity(openHome);
                            finish(); // Close this activity
                        }
                    } else {
                        Log.e("LoginError", "Login failed: " + task.getException());
                        Toast.makeText(LoginActivity.this, "Incorrect password/email! Try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
