package com.example.project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.Calendar;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    private List<Profile> profileList;
    private Context context;

    public ProfileAdapter(Context context, List<Profile> profileList) {
        this.context = context;
        this.profileList = profileList;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profile_card, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Profile profile = profileList.get(position);
        holder.profileName.setText(profile.getName());
        holder.profileDescription.setText(profile.getDescription());

        Glide.with(context)
                .load(profile.getImageUrl())
                .placeholder(R.drawable.user)
                .into(holder.profileImage);

        // Handle Date button click
        holder.btnDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    (view, year1, month1, dayOfMonth1) -> {
                        String selectedDate = dayOfMonth1 + "/" + (month1 + 1) + "/" + year1;
                        holder.btnDate.setText(selectedDate);
                    }, year, month, dayOfMonth);
            datePickerDialog.show();
        });


        holder.btnTime.setOnClickListener(v -> {
            // Initialize the calendar
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);


            TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                    (view, hourOfDay, minute1) -> {
                        String selectedTime = hourOfDay + ":" + (minute1 < 10 ? "0" + minute1 : minute1);
                        holder.btnTime.setText(selectedTime);
                    }, hour, minute, true);
            timePickerDialog.show();
        });

        holder.btnBook.setOnClickListener(v -> {
            Toast.makeText(context, "Date already sent, waiting for confirmation!", Toast.LENGTH_SHORT).show(); //Under process
        });
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        TextView profileName, profileDescription;
        ImageView profileImage;
        Button btnDate, btnTime, btnBook;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            profileName = itemView.findViewById(R.id.profileName);
            profileDescription = itemView.findViewById(R.id.profileDescription);
            profileImage = itemView.findViewById(R.id.profileImage);
            btnDate = itemView.findViewById(R.id.btnDate);
            btnTime = itemView.findViewById(R.id.btnTime);
            btnBook = itemView.findViewById(R.id.btnBook);
        }
    }
}
