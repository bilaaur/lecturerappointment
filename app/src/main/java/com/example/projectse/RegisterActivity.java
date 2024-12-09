package com.example.projectse;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextStudentId = findViewById(R.id.editTextStudentId);
        EditText editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextPassword = findViewById(R.id.editTextPassword);

        TextView accountalready = findViewById(R.id.accountalready);
        Button buttonRegister = findViewById(R.id.buttonRegister);
        ImageButton buttonCamera = findViewById(R.id.buttonCamera);

        accountalready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openMain = new Intent(getApplicationContext(), LoginActivity.class);
                startActivities(new Intent[]{openMain});
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String studentId = editTextStudentId.getText().toString().trim();
                String phoneNumber = editTextPhoneNumber.getText().toString().trim();

                // Validation
                if (name.isEmpty()) {
                    editTextName.setError("Name is required!");
                    editTextName.requestFocus();
                    return;
                }
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
                if (studentId.isEmpty()) {
                    editTextStudentId.setError("Student ID is required!");
                    editTextStudentId.requestFocus();
                    return;
                }
                if (phoneNumber.isEmpty()) {
                    editTextPhoneNumber.setError("Phone number is required!");
                    editTextPhoneNumber.requestFocus();
                    return;
                }
                userId = databaseReference.push().getKey();
                User user = new User(userId, name, email, password, studentId, phoneNumber);

                databaseReference.child(userId).setValue(user)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                Intent openLogin = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(openLogin);
                            } else {
                                Log.e("LoginError", "Login failed: " + task.getException().getMessage());
                                Toast.makeText(RegisterActivity.this, "Registration Failed! Try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Keep the camera button functionality, but don't store the image in Firebase
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // If you still want to show the image in an ImageView, you can do this:
            // ImageView imageView = findViewById(R.id.imageViewProfile);
            // imageView.setImageBitmap(imageBitmap);

            // Do not upload image to Firebase
            Toast.makeText(this, "Image captured but not uploaded.", Toast.LENGTH_SHORT).show();
        }
    }
}
