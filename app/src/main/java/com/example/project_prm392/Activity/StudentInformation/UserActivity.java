package com.example.project_prm392.Activity.StudentInformation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.project_prm392.Activity.Authentication.LoginActivity;
import com.example.project_prm392.Activity.Base.BaseActivity;
import com.example.project_prm392.Activity.Base.MainActivity;
import com.example.project_prm392.Activity.Report.ReportActivity;
import com.example.project_prm392.databinding.ActivityUserBinding;
import com.example.project_prm392.entities.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserActivity extends BaseActivity {
    ActivityUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        handleButton();
        setVariable();
    }
    private void handleButton() {
        //Handle "Hồ sơ"
        binding.btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(UserActivity.this, ProfileActivity.class));
            finish();
        });

        //Handle "Cài đặt"
        binding.btnSetting.setOnClickListener(v -> {
            startActivity(new Intent(UserActivity.this, SettingActivity.class));
            finish();
        });

        //Handle "Đăng xuất"
        binding.btnLogout.setOnClickListener(v -> {
            //Xóa thông tin người dùng đang đăng nhập trong preferences
            SharedPreferences preferences = getSharedPreferences("currentStudent", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            //Chuyển đến màn hình đăng nhập
            Intent intent = new Intent(UserActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        binding.btnUserHome.setOnClickListener(v -> startActivity(new Intent(UserActivity.this, MainActivity.class)));
        binding.btnUserReport.setOnClickListener(v -> {startActivity(new Intent(UserActivity.this, ReportActivity.class));});
    }
    private void setVariable() {
        SharedPreferences currentStudent = getSharedPreferences("currentStudent", MODE_PRIVATE);
        String student_roll_number = currentStudent.getString("student_roll_number", "");
        Query query = database.getReference("Student").orderByChild("student_roll_number").equalTo(student_roll_number);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot studentSnapshot : snapshot.getChildren()) {
                        Student student = studentSnapshot.getValue(Student.class);
                        if (student != null) {
                            binding.tvFullName.setText(student.getStudent_name());
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
