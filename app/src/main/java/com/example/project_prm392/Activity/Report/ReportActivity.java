package com.example.project_prm392.Activity.Report;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.project_prm392.Activity.Base.BaseActivity;
import com.example.project_prm392.Activity.Base.MainActivity;
import com.example.project_prm392.Helper.DataEncode;
import com.example.project_prm392.entities.Report;

import com.example.project_prm392.databinding.ActivityReportBinding;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;

public class ReportActivity extends BaseActivity {
    ActivityReportBinding binding;

    DataEncode dataEncode = new DataEncode();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnReportCf.setEnabled(false);
        binding.tvReportErr1.setVisibility(View.GONE);
        binding.tvReportErr2.setVisibility(View.GONE);
        binding.tvReportErr3.setVisibility(View.GONE);
        setVariable();
        handleTextChange();
        handleButton();
    }

    private void setVariable() {
        String[] options = {"Lỗi giao dịch", "Vấn đề khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spReportCategory.setAdapter(adapter);
    }

    private void handleButton() {
        binding.btnReportBack.setOnClickListener(v -> finish());

        binding.btnReportCf.setOnClickListener(v -> {
            //Get data from view
            SharedPreferences preferences = getSharedPreferences("currentStudent", MODE_PRIVATE);
            String report_category = binding.spReportCategory.getSelectedItem().toString().trim();
            String report_transaction_id = binding.edtReportTransactionId.getText().toString().trim();
            String report_title = Objects.requireNonNull(binding.edtReportTitle.getText()).toString().trim();
            String report_description = binding.edtReportDescription.getText().toString().trim();

            //Get current student roll number
            String student_roll_number = preferences.getString("student_roll_number", "");

            //Save data to DB
            DatabaseReference report_reference = database.getReference("Report").child(student_roll_number);
            report_reference.push().setValue(new Report(report_category, report_transaction_id,
                            report_title, report_description,
                            dataEncode.getCurrentTime(), 0))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Lưu dữ liệu thành công
                            // Hiển thị dialog thông báo
                            showDialogAndNavigateToMainActivity();
                        }  // Xử lý khi lưu dữ liệu không thành công (nếu cần)

                    });
        });
        binding.textView99.setOnClickListener(v -> startActivity(new Intent(ReportActivity.this, ReportListActivity.class)));
    }

    private void showDialogAndNavigateToMainActivity() {
        // Hiển thị dialog thông báo
        AlertDialog.Builder builder = new AlertDialog.Builder(ReportActivity.this);
        builder.setTitle("Success")
                .setMessage("Data saved successfully.")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Chuyển về MainActivity
                    startActivity(new Intent(ReportActivity.this, MainActivity.class));
                    finish(); // Đóng ReportActivity để ngăn người dùng quay lại từ MainActivity
                }).show();
    }


    private void handleTextChange() {
        binding.edtReportTransactionId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String report_transaction_id = s.toString().trim();
                boolean isTransactionIdValid = report_transaction_id.matches("^-([A-Za-z0-9_]{19})$");
                binding.tvReportErr1.setVisibility(isTransactionIdValid ? View.GONE : View.VISIBLE);
                enableReportButton();
            }
        });

        binding.edtReportTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String report_title = s.toString().trim();
                boolean isTitleValid = report_title.matches("^.{1,49}$");
                binding.tvReportErr2.setVisibility(isTitleValid ? View.GONE : View.VISIBLE);
                enableReportButton();
            }
        });

        binding.edtReportDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                String report_description = s.toString().trim();
                boolean isDescriptionValid = report_description.matches("^.{1,199}$");
                binding.tvReportErr3.setVisibility(isDescriptionValid ? View.GONE : View.VISIBLE);
                enableReportButton();
            }
        });
    }

    private void enableReportButton() {
        String report_transaction_id = binding.edtReportTransactionId.getText().toString().trim();
        String report_title = Objects.requireNonNull(binding.edtReportTitle.getText()).toString().trim();
        String report_description = Objects.requireNonNull(binding.edtReportDescription.getText()).toString().trim();
        boolean isTransactionIdValid = report_transaction_id.matches("^-([A-Za-z0-9_]{19})$");
        boolean isTitleValid = report_title.matches("^.{1,49}$");
        boolean isDescriptionValid = report_description.matches("^.{1,199}$");
        binding.btnReportCf.setEnabled(isTransactionIdValid && isTitleValid && isDescriptionValid);
    }

}