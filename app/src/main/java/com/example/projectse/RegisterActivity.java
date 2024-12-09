    package com.example.project;

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

    import com.google.android.gms.tasks.Task;
    import com.google.firebase.FirebaseApp;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.auth.UserProfileChangeRequest;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;

    import java.io.ByteArrayOutputStream;

    public class RegisterActivity extends AppCompatActivity {
        private FirebaseAuth databaseReference;
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


            databaseReference = FirebaseAuth.getInstance();
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
                    Task<AuthResult> createUser = databaseReference.createUserWithEmailAndPassword(email, password);
                    createUser.addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();
                            if(user == null) {
                                Log.d("USER", "Failed to get user");
                                return;
                            }

                            user.updateProfile(profile);
                            Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                            Intent openLogin = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(openLogin);
                        } else {
                            Log.e("RegistError", "Regist error failed: " + task.getException().getMessage());
                            Toast.makeText(RegisterActivity.this, "Registration Failed! Try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            // Camera button
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

                // Not storing image because firebase can't store Image
                // ImageView imageView = findViewById(R.id.imageViewProfile);
                // imageView.setImageBitmap(imageBitmap);

                Toast.makeText(this, "Image captured but not uploaded.", Toast.LENGTH_SHORT).show();
            }
        }
    }
