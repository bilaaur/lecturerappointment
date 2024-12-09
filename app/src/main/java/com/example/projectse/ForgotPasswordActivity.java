package com.example.project;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.view.WindowInsetsCompat;


import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ForgotPasswordActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextStudentId = findViewById(R.id.editTextStudentId);
        EditText editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        EditText editTextNewPassword = findViewById(R.id.editTextNewPassword);
        EditText editTextConfirmNewPassword = findViewById(R.id.editTextConfirmPassword);
        TextView donthaveacc = findViewById(R.id.donthaveacc);
        TextView accountalready = findViewById(R.id.accountalready);
        Button buttonChangePass = findViewById(R.id.buttonChangePass);

        donthaveacc.setOnClickListener(v -> {
            Intent openRegister = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivities(new Intent[]{openRegister});
        });

        accountalready.setOnClickListener(v -> {
            Intent openMain = new Intent(getApplicationContext(), LoginActivity.class);
            startActivities(new Intent[]{openMain});
        });

        buttonChangePass.setOnClickListener(v -> {
            // Get input from the user
            String email = editTextEmail.getText().toString().trim();
            String studentId = editTextStudentId.getText().toString().trim();
            String phoneNumber = editTextPhoneNumber.getText().toString().trim();
            String newPassword = editTextNewPassword.getText().toString().trim();
            String confirmNewPassword = editTextConfirmNewPassword.getText().toString().trim();

            // Basic validation
            if (email.isEmpty() || studentId.isEmpty() || phoneNumber.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmNewPassword)) {
                Toast.makeText(ForgotPasswordActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Query to find the user with the matching email, studentId, and phoneNumber
            Query userQuery = databaseReference.orderByChild("email").equalTo(email);

            userQuery.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        // Check if the student ID and phone number match
                        boolean userFound = false;
                        for (DataSnapshot snapshot : task.getResult().getChildren()) {
                            String storedStudentId = snapshot.child("studentId").getValue(String.class);
                            String storedPhoneNumber = snapshot.child("phoneNumber").getValue(String.class);

                            if (storedStudentId.equals(studentId) && storedPhoneNumber.equals(phoneNumber)) {
                                userFound = true;
                                // Update the password
                                String userId = snapshot.getKey();
                                databaseReference.child(userId).child("password").setValue(newPassword)
                                        .addOnCompleteListener(updateTask -> {
                                            if (updateTask.isSuccessful()) {
                                                Toast.makeText(ForgotPasswordActivity.this, "Password changed successfully!", Toast.LENGTH_SHORT).show();
                                                Intent openLogin = new Intent(getApplicationContext(), LecturerActivity.class);
                                                startActivity(openLogin);
                                            } else {
                                                Toast.makeText(ForgotPasswordActivity.this, "Failed to change password. Try again.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                break;
                            }
                        }

                        if (!userFound) {
                            Toast.makeText(ForgotPasswordActivity.this, "Incorrect Student ID or Phone Number!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Email not found!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Error retrieving data. Try again.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}

