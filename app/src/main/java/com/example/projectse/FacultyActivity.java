package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacultyActivity extends AppCompatActivity {
    private SearchView searchView;
    private LinearLayout facultyContainer;
    private TextView logoutTextView;
    private Map<String, List<String>> facultyMajorsMap;
    private List<String> faculties;
    private Button submitButton;
    private String selectedMajor = null; // Track the selected major

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);

        searchView = findViewById(R.id.searchFaculty);
        facultyContainer = findViewById(R.id.facultyContainer);
        logoutTextView = findViewById(R.id.logout);
        submitButton = findViewById(R.id.submitButton);

        setupFacultyMajorsMap();
        populateFaculties(faculties);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterFaculties(newText);
                return true;
            }
        });

        logoutTextView.setOnClickListener(v -> {

        });
    }

    private void setupFacultyMajorsMap() {
        facultyMajorsMap = new HashMap<>();
        facultyMajorsMap.put("Business", Arrays.asList("Accounting", "Actuarial Science", "Agribusiness", "Business Administration", "Management"));
        facultyMajorsMap.put("Computing", Arrays.asList("Information System", "Information Technology", "Interior Design", "Visual Communication Design"));
        facultyMajorsMap.put("Engineering", Arrays.asList("Civil Engineering", "Electrical Engineering", "Environmental Engineering", "Industrial Engineering", "Mechanical Engineering"));
        facultyMajorsMap.put("Humanities", Arrays.asList("Communication", "International Relations", "Teacher Education"));
        facultyMajorsMap.put("Law", Arrays.asList("Law"));
        facultyMajorsMap.put("Medicine", Arrays.asList("Medicine"));

        faculties = new ArrayList<>(facultyMajorsMap.keySet());
        Collections.sort(faculties);
    }

    private void populateFaculties(List<String> faculties) {
        facultyContainer.removeAllViews();

        for (String faculty : faculties) {
            View facultyView = LayoutInflater.from(this).inflate(R.layout.faculty_item, facultyContainer, false);

            // Set faculty name
            TextView facultyName = facultyView.findViewById(R.id.faculty_name);
            facultyName.setText(faculty);

            // Set faculty logo
            ImageView facultyLogo = facultyView.findViewById(R.id.faculty_logo);
            int logoResource = getLogoResource(faculty);
            facultyLogo.setImageResource(logoResource);

            // Populate majors in spinner
            Spinner majorSpinner = facultyView.findViewById(R.id.major_spinner);
            List<String> majors = facultyMajorsMap.get(faculty);
            ArrayAdapter<String> majorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, majors);
            majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            majorSpinner.setAdapter(majorAdapter);

            // Add listener to spinner
            majorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedMajor = majors.get(position); // Store the selected major
                    Toast.makeText(FacultyActivity.this, "Selected: " + selectedMajor, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectedMajor = null;
                }
            });

            // Set up submit button logic
            submitButton.setOnClickListener(v -> {
                if (selectedMajor != null) {
                    // Save the selected major to SharedPreferences
                    saveFacultyToSharedPreferences(selectedMajor);

                    if ("Information Technology".equals(selectedMajor)) {
                        Intent intent = new Intent(FacultyActivity.this, LecturerActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(FacultyActivity.this, "To be announced", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FacultyActivity.this, "Please select a major first", Toast.LENGTH_SHORT).show();
                }
            });

            facultyContainer.addView(facultyView);
        }
    }

    private void filterFaculties(String query) {
        List<String> filteredFaculties = new ArrayList<>();
        for (String faculty : faculties) {
            if (faculty.toLowerCase().contains(query.toLowerCase())) {
                filteredFaculties.add(faculty);
            }
        }
        populateFaculties(filteredFaculties);
    }

    private int getLogoResource(String faculty) {
        switch (faculty) {
            case "Computing":
                return R.drawable.computing;
            case "Business":
                return R.drawable.business;
            case "Engineering":
                return R.drawable.engineering;
            case "Humanities":
                return R.drawable.humanities;
            case "Law":
                return R.drawable.law;
            case "Medicine":
                return R.drawable.medicine;
            default:
                return R.drawable.pu;
        }
    }

    private void saveFacultyToSharedPreferences(String selectedFaculty) {
        SharedPreferences sharedPreferences = getSharedPreferences("FacultySession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selectedFaculty", selectedFaculty);
        editor.apply();
    }
}
