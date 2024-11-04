package com.example.project_prm392.Activity.StudentInformation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.project_prm392.Activity.Base.BaseActivity;
import com.example.project_prm392.Constant.Constant;
import com.example.project_prm392.Helper.SendOTP;
import com.example.project_prm392.Security.EnterCodeActivity;
import com.example.project_prm392.databinding.ActivityUpdateProfileBinding;
import com.example.project_prm392.entities.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UpdateProfileActivity extends BaseActivity {
    ActivityUpdateProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bindingView();
        handleButton();
    }

    private void bindingView() {
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
                            binding.edtUpdateProfileActivityStudentFullname.setText(student.getStudent_name());
                            binding.edtUpdateProfileActivityStudentPhone.setText(student.getStudent_phone());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void handleButton() {
        // Handle btn back to ProfileActivity
        binding.btnUpdateProfileBack.setOnClickListener(v -> {
            Intent intent_back = new Intent(UpdateProfileActivity.this, ProfileActivity.class);
            startActivity(intent_back);
        });

        // Handle btn confirm to update profile
        binding.btnUpdateProfileCf.setOnClickListener(v -> {
            SharedPreferences preferences = getSharedPreferences("currentStudent", MODE_PRIVATE);
            String student_email = preferences.getString("student_email", "");
            String verificationCode = dataEncode.generateRandomCode();
            new Thread(
                    () -> {
                        new SendMailTask().execute(student_email, "Xác minh cập nhật thông tin cá nhân",
                                "Mã xác minh cập nhật thông tin cá nhân của bạn là: " + verificationCode);
                    }
            ).start();

            //Save new student information to sharedPre
            SharedPreferences profileUpdate = getSharedPreferences("profileUpdate", MODE_PRIVATE);
            SharedPreferences.Editor editor = profileUpdate.edit();
            editor.putString("fullnameUpdate", Objects.requireNonNull(binding.edtUpdateProfileActivityStudentFullname).getText().toString().trim());
            editor.putString("phoneUpdate", Objects.requireNonNull(binding.edtUpdateProfileActivityStudentPhone.getText()).toString().trim());
            editor.apply();
            Intent intent = new Intent(UpdateProfileActivity.this, EnterCodeActivity.class);
            intent.putExtra("code", verificationCode);
            intent.putExtra("type", "update profile");
            startActivity(intent);

        });
    }

    private class SendMailTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String receiveEmail = params[0];
            String emailSubject = params[1];
            String text = params[2];

            SendOTP sendMail = new SendOTP();
            sendMail.sendMail(receiveEmail, emailSubject, text);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        }
    }
}
