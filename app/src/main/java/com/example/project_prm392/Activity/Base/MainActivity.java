package com.example.project_prm392.Activity.Base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.project_prm392.Activity.Authentication.LoginActivity;
import com.example.project_prm392.Activity.Report.ReportActivity;
import com.example.project_prm392.Activity.StudentInformation.UserActivity;
import com.example.project_prm392.Activity.Transaction.Paying.PayingActivity;
import com.example.project_prm392.Activity.Transaction.QR.QRGenerateActivity;
import com.example.project_prm392.Activity.Transaction.TopUpActivity;
import com.example.project_prm392.Activity.Transaction.TransactionView.ListAllTransactionActivity;
import com.example.project_prm392.Activity.Transaction.TransferActivity;
import com.example.project_prm392.Adapter.TransactionAdapter;
import com.example.project_prm392.Helper.TransferNotification;
import com.example.project_prm392.databinding.ActivityMainBinding;
import com.example.project_prm392.entities.Transaction;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private final List<Transaction> transactions = new ArrayList<>();
    private DatabaseReference transactionRef;
    private TransactionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
        SharedPreferences preferences = getSharedPreferences("currentStudent", MODE_PRIVATE);
        String currentStudentRollNumber = preferences.getString("student_roll_number", "");
        if (currentStudentRollNumber.isEmpty()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }
        binding.textView32.setText(preferences.getString("student_name", ""));
        binding.progressBar2.setVisibility(View.VISIBLE);
        transactionRef = database.getReference("Transaction").child(currentStudentRollNumber);
        displayCurrentAmount(currentStudentRollNumber);
        handleButton();
        listenForTransactionChanges();
        initRecyclerView();
    }
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    Toast.makeText(MainActivity.this, "Permission denied to post notifications", Toast.LENGTH_SHORT).show();
                }
            });

    private void displayCurrentAmount(String currentStudentRollNumber) {
        DatabaseReference reference = database.getReference("Student");
        Query query = reference.orderByChild("student_roll_number")
                .equalTo(currentStudentRollNumber);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot studentDataSnapshot = snapshot.getChildren().iterator().next();
                    long currentAmount = studentDataSnapshot.child("student_amount").getValue(Long.class);
                    binding.tvCurrentAmount.setText(dataEncode.formatMoney(currentAmount));
                    binding.progressBar2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event if needed
            }
        });
    }

    private void handleButton() {
        binding.btnTopUp.setOnClickListener(v -> {startActivity(new Intent(MainActivity.this, TopUpActivity.class));});
        binding.btnUser.setOnClickListener(v -> {startActivity(new Intent(MainActivity.this, UserActivity.class));});
        binding.btnTransfer.setOnClickListener(v -> {startActivity(new Intent(MainActivity.this, TransferActivity.class));});
        binding.btnReport.setOnClickListener(v -> {startActivity(new Intent(MainActivity.this, ReportActivity.class));});
        binding.btnMainToTransactionHistory.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ListAllTransactionActivity.class)));
        binding.btnQrInMain.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, QRGenerateActivity.class)));
        binding.btnPaying.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PayingActivity.class)));
    }

    private void initRecyclerView() {
        adapter = new TransactionAdapter(transactions);
        binding.transactionView.setLayoutManager(new LinearLayoutManager(this));
        binding.transactionView.setAdapter(adapter);
    }

    private void listenForTransactionChanges() {
        transactionRef.orderByChild("time").limitToLast(10).addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                Transaction transaction = snapshot.getValue(Transaction.class);
                if (transaction != null) {
                    transactions.add(0, transaction);
                    Collections.sort(transactions, (t1, t2) -> {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        try {
                            Date date1 = sdf.parse(t1.getTime());
                            Date date2 = sdf.parse(t2.getTime());
                            return date2.compareTo(date1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    });

                    if (transactions.size() > 10) {
                        transactions.remove(transactions.size() - 1);
                    }
                    adapter.notifyDataSetChanged();
                    binding.transactionView.smoothScrollToPosition(0);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


}

