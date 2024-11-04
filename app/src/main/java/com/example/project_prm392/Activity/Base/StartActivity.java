package com.example.project_prm392.Activity.Base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm392.*;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();
        }, 1000);

    }
}
