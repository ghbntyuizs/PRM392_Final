package com.example.project_prm392.Activity.StudentInformation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm392.Security.SetUpPINCodeActivity;
import com.example.project_prm392.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        handleButton();
    }

    private void handleButton() {
        //Handle "Thiết lập mã PIN"
        binding.btnToSetupPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, SetUpPINCodeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        binding.imageView9.setOnClickListener(v -> {
            startActivity(new Intent(SettingActivity.this, UserActivity.class));
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
