package com.example.project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.widget.SearchView;

public class LecturerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    private List<Profile> profileList;
    private List<Profile> filteredList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        profileList = new ArrayList<>();
        filteredList = new ArrayList<>();
        profileAdapter = new ProfileAdapter(this, filteredList);
        recyclerView.setAdapter(profileAdapter);

        databaseReference = FirebaseDatabase.getInstance("https://projectsewmp-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("profiles");
        fetchDataFromFirebase();

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterProfiles(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProfiles(newText);
                return true;
            }
        });

        // Retrieve SharedPreferences
        String selectedFaculty = getFacultyFromSharedPreferences();
        Log.d("LecturerActivity", "Selected Faculty: " + selectedFaculty);
    }

    private void fetchDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profileList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Profile profile = dataSnapshot.getValue(Profile.class);
                    if (profile != null) {
                        profileList.add(profile);
                    }
                }
                filteredList.clear();
                filteredList.addAll(profileList);
                profileAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching data: " + error.getMessage());
            }
        });
    }

    private void filterProfiles(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(profileList);
        } else {
            for (Profile profile : profileList) {
                if (profile.getName() != null && profile.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(profile);
                }
            }
        }
        profileAdapter.notifyDataSetChanged();
    }

    // SharedPreferences
    private void saveFacultyToSharedPreferences(String faculty) {
        SharedPreferences sharedPreferences = getSharedPreferences("FacultySession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selectedFaculty", faculty);
        editor.apply();
    }

    // Retrieve from SharedPreferences
    private String getFacultyFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("FacultySession", MODE_PRIVATE);
        return sharedPreferences.getString("selectedFaculty", "No faculty selected");
    }
}
