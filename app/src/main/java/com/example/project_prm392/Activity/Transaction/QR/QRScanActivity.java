package com.example.project_prm392.Activity.Transaction.QR;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm392.databinding.ActivityQrscanBinding;


public class QRScanActivity extends AppCompatActivity {
    private ActivityQrscanBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQrscanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
