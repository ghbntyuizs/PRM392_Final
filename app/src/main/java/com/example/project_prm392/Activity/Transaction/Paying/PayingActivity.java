package com.example.project_prm392.Activity.Transaction.Paying;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.project_prm392.Activity.Base.BaseActivity;
import com.example.project_prm392.Constant.Constant;
import com.example.project_prm392.databinding.ActivityPayingBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class PayingActivity extends BaseActivity {
    ActivityPayingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getStudentFee();
        handleButton();
    }

    private void handleButton() {
        SharedPreferences preferences = getSharedPreferences("currentStudent", MODE_PRIVATE);
        binding.btnPayingBack.setOnClickListener(v -> finish());
        binding.btnPayingTuition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long tuition = preferences.getInt(Constant.SEMESTER_FEE_TYPE, 0);
                if (tuition != 0){
                    Intent intent = new Intent(PayingActivity.this, SemesterFeeActivity.class);
                    intent.putExtra("fee_type", Constant.SEMESTER_FEE_TYPE);
                    startActivity(intent);
                }else{
                    Toast.makeText(PayingActivity.this, "Bạn không nợ học phí kỳ này.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.btnPayingDomFee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long tuition = preferences.getInt(Constant.DORMITORY_FEE, 0);
                if (tuition != 0) {
                    Intent intent = new Intent(PayingActivity.this, SemesterFeeActivity.class);
                    intent.putExtra("fee_type", Constant.DORMITORY_FEE);
                    startActivity(intent);
                } else {
                    Toast.makeText(PayingActivity.this, "Bạn không nợ học phí kỳ này.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.btnPayingAdditionalDormitoryFee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long tuition = preferences.getInt(Constant.ADDITIONAL_DORMITORY_FEE_TYPE, 0);
                if (tuition != 0) {
                    Intent intent = new Intent(PayingActivity.this, SemesterFeeActivity.class);
                    intent.putExtra("fee_type", Constant.ADDITIONAL_DORMITORY_FEE_TYPE);
                    startActivity(intent);
                } else {
                    Toast.makeText(PayingActivity.this, "Bạn không nợ học phí kỳ này.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.btnLibraryFines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long tuition = preferences.getInt(Constant.LIBRARY_FINES, 0);
                if (tuition != 0) {
                    Intent intent = new Intent(PayingActivity.this, SemesterFeeActivity.class);
                    intent.putExtra("fee_type", Constant.LIBRARY_FINES);
                    startActivity(intent);
                } else {
                    Toast.makeText(PayingActivity.this, "Bạn không nợ học phí kỳ này.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnPayingReStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long tuition = preferences.getInt(Constant.RE_STUDY_FEE, 0);
                if (tuition != 0) {
                    Intent intent = new Intent(PayingActivity.this, SemesterFeeActivity.class);
                    intent.putExtra("fee_type", Constant.RE_STUDY_FEE);
                    startActivity(intent);
                } else {
                    Toast.makeText(PayingActivity.this, "Bạn không nợ học phí kỳ này.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.btnPayingScholarPenalty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long tuition = preferences.getInt(Constant.SCHOLARSHIP_PENALTY_FEE, 0);
                if (tuition != 0) {
                    Intent intent = new Intent(PayingActivity.this, SemesterFeeActivity.class);
                    intent.putExtra("fee_type", Constant.SCHOLARSHIP_PENALTY_FEE);
                    startActivity(intent);
                } else {
                    Toast.makeText(PayingActivity.this, "Bạn không nợ học phí kỳ này.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getStudentFee() {
        SharedPreferences preferences = getSharedPreferences("currentStudent", MODE_PRIVATE);
        String student_roll_number = preferences.getString("student_roll_number", "");
        DatabaseReference reference = database.getReference("Fee").child(student_roll_number);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    SharedPreferences preferences = getSharedPreferences("currentStudent", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    Log.d(TAG,"DEBUGMASTER 0" + "getED");
                    //Get fee value
                    Long additionalDormitoryFee = snapshot.child("additional_dormitory_fee").getValue(Long.class);
                    Long dormitoryFee = snapshot.child("dormitory_fee").getValue(Long.class);
                    Long libraryFines = snapshot.child("library_fines").getValue(Long.class);
                    Long reStudyFee = snapshot.child("re_study_fee").getValue(Long.class);
                    Long scholarshipPenaltyFee = snapshot.child("scholarship_penalty_fee").getValue(Long.class);
                    Long semesterFee = snapshot.child("semester_fee").getValue(Long.class);

                    // Lưu các giá trị vào SharedPreferences
                    editor.putInt("additional_dormitory_fee", additionalDormitoryFee != null ? additionalDormitoryFee.intValue() : 0);
                    editor.putInt("dormitory_fee", dormitoryFee != null ? dormitoryFee.intValue() : 0);
                    editor.putInt("library_fines", libraryFines != null ? libraryFines.intValue() : 0);
                    editor.putInt("re_study_fee", reStudyFee != null ? reStudyFee.intValue() : 0);
                    editor.putInt("scholarship_penalty_fee", scholarshipPenaltyFee != null ? scholarshipPenaltyFee.intValue() : 0);
                    editor.putInt("semester_fee", semesterFee != null ? semesterFee.intValue() : 0);
                    editor.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}