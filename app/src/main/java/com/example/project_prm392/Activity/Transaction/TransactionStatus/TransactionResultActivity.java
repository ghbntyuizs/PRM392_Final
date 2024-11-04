package com.example.project_prm392.Activity.Transaction.TransactionStatus;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm392.Activity.Base.MainActivity;
import com.example.project_prm392.databinding.ActivityTransactionResultBinding;

public class TransactionResultActivity extends AppCompatActivity {
    ActivityTransactionResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvTransactionResultId.setText(getIntent().getStringExtra("Transaction key"));
        binding.tvTransactionResultAmount.setText(String.valueOf(getIntent().getIntExtra("Transaction amount", 0)));
        binding.tvTransactionResultTime.setText(getIntent().getStringExtra("Transaction time"));
        handleButton();

    }

    private void handleButton() {

        binding.btnCfTransactionResult.setOnClickListener(v -> {
            Intent intent = new Intent(TransactionResultActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}