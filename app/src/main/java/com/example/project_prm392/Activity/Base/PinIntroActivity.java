package com.example.project_prm392.Activity.Base;

import android.content.Intent;
import android.os.Bundle;

import com.example.project_prm392.Security.SetUpPINCodeActivity;
import com.example.project_prm392.databinding.ActivityPinintroBinding;

public class PinIntroActivity extends BaseActivity{
    ActivityPinintroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPinintroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //handle button
        binding.btnPINIntroBack.setOnClickListener(v -> finish());

        binding.btnPINIntroContinue.setOnClickListener(v ->
                startActivity(new Intent(PinIntroActivity.this, SetUpPINCodeActivity.class)));
    }
}
