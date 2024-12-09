package com.example.project;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
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

        // DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://projectsewmp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users");
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        TextView forgotPassword = findViewById(R.id.forgotpassword);
        TextView dontHaveAccount = findViewById(R.id.donthaveacc);

        // Navigate to Forgot Password screen
        forgotPassword.setOnClickListener(v -> {
            Intent openForgotPassword = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
            startActivity(openForgotPassword);
        });

        // Navigate to Registration screen
        dontHaveAccount.setOnClickListener(v -> {
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

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            Intent openFaculty = new Intent(LoginActivity.this, FacultyActivity.class);
                            startActivity(openFaculty);
                            finish();
                        }
                    } else {
                        Log.e("LoginError", "Login failed: " + task.getException());
                        Toast.makeText(LoginActivity.this, "Incorrect email or password! Try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
