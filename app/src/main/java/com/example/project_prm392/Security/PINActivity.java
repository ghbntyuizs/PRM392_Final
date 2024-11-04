package com.example.project_prm392.Security;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.project_prm392.API.ZaloPay.CreateOrder;
import com.example.project_prm392.Activity.Base.BaseActivity;
import com.example.project_prm392.Activity.Base.MainActivity;
import com.example.project_prm392.Activity.Transaction.TransactionStatus.TransactionResultActivity;
import com.example.project_prm392.Constant.AppInfo;
import com.example.project_prm392.Constant.Constant;
import com.example.project_prm392.Helper.DataEncode;
import com.example.project_prm392.databinding.ActivityPinactivityBinding;
import com.example.project_prm392.entities.Transaction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class PINActivity extends BaseActivity {
    ActivityPinactivityBinding binding;
    DataEncode dataEncode = new DataEncode();
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPinactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(AppInfo.APP_ID, Environment.SANDBOX);
        setUpOTPInput();
        setConfirmButton();
    }

    private void setUpOTPInput() {
        binding.inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setConfirmButton() {
        binding.btnConfirmPIN.setOnClickListener(v -> {
            String PIN = binding.inputCode1.getText().toString().trim()
                    + binding.inputCode2.getText().toString().trim()
                    + binding.inputCode3.getText().toString().trim()
                    + binding.inputCode4.getText().toString().trim();

            progressDialog = new ProgressDialog(PINActivity.this);
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCancelable(false);

            new Thread(() -> checkAndPerformTransaction(PIN)).start();
        });
    }

    private void checkAndPerformTransaction(String PIN) {
        runOnUiThread(() -> progressDialog.show());

        SharedPreferences preferences = getSharedPreferences("currentStudent", MODE_PRIVATE);
        String studentPIN = preferences.getString("student_PIN", "");
        boolean isPinValid = dataEncode.verifyHash(PIN, studentPIN);
        if (isPinValid) {
            performTransaction();
        } else {
            runOnUiThread(() -> {
                progressDialog.dismiss();
                Toast.makeText(PINActivity.this, "Mã PIN không đúng", Toast.LENGTH_SHORT).show();
            });
        }
    }

    @SuppressLint("DefaultLocale")
    private void performTransaction() {
        Intent intent = getIntent();
        int transactionType = intent.getIntExtra("transaction_type", 0);
        int amount = intent.getIntExtra("transaction_amount", 0);
        Log.d(TAG, "DEBUG " + amount);
        String currentTime = dataEncode.getCurrentTime();
        switch (transactionType) {
            case 1:
                handleZaloPayPayment(amount);
                break;
            case 2:
                new Thread(() -> {
                    String transactionKey = transfer(amount, currentTime);
                    navigateToTransactionResult(transactionKey, amount, currentTime);
                }).start();
                break;
            case 3:
                new Thread(() -> {
                    String transactionKey = paying(amount, currentTime, "Thanh toán học phí", Constant.SEMESTER_FEE_TYPE);
                    navigateToTransactionResult(transactionKey, amount, currentTime);
                }).start();
                break;
            case 4:
                new Thread(() -> {
                    String transactionKey = paying(amount, currentTime, "Thanh toán ký túc xá", Constant.DORMITORY_FEE);
                    navigateToTransactionResult(transactionKey, amount, currentTime);
                }).start();
                break;
            case 5:
                new Thread(() -> {
                    String transactionKey = paying(amount, currentTime, "Thanh toán phụ trội ký túc xá", Constant.ADDITIONAL_DORMITORY_FEE_TYPE);
                    navigateToTransactionResult(transactionKey, amount, currentTime);
                }).start();
                break;
            case 6:
                new Thread(() -> {
                    String transactionKey = paying(amount, currentTime, "Thanh toán khoản phạt thư viện", Constant.LIBRARY_FINES);
                    navigateToTransactionResult(transactionKey, amount, currentTime);
                }).start();
                break;
            case 7:
                new Thread(() -> {
                    String transactionKey = paying(amount, currentTime, "Thanh toán tiền học lại", Constant.RE_STUDY_FEE);
                    navigateToTransactionResult(transactionKey, amount, currentTime);
                }).start();
                break;
            case 8:
                new Thread(() -> {
                    String transactionKey = paying(amount, currentTime, "Thanh toán tiền phạt học bổng", Constant.SCHOLARSHIP_PENALTY_FEE);
                    navigateToTransactionResult(transactionKey, amount, currentTime);
                }).start();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + transactionType);
        }
    }

    private String topUp(int amount_top_up, String currentTime) {
        SharedPreferences preferences = getSharedPreferences("currentStudent", MODE_PRIVATE);
        String to = preferences.getString("student_roll_number", "");
        String from = "ZaloPay";
        String category = "Nạp tiền vào ví";

        DatabaseReference reference = databaseReference.child("Student");
        Query query = reference.orderByChild("student_roll_number")
                .equalTo(preferences.getString("student_roll_number", ""));

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot studentSnapshot = snapshot.getChildren().iterator().next();
                    long currentAmount = studentSnapshot.child("student_amount").getValue(Long.class);
                    studentSnapshot.getRef().child("student_amount")
                            .setValue(currentAmount + amount_top_up);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        String student_roll_number = preferences.getString("student_roll_number", "");
        DatabaseReference transactionRef = databaseReference.child("Transaction").child(student_roll_number);
        DatabaseReference newTransaction = transactionRef.push();
        List<Transaction> transactions = loadTransaction(transactionRef);
        String newTransactionKey = newTransaction.getKey();
        if (transactions.size() > 0) {
            newTransaction.setValue(new Transaction(newTransactionKey, from, to, category, amount_top_up, currentTime, transactions.get(transactions.size() - 1).getHash()));
        } else {
            newTransaction.setValue(new Transaction(newTransactionKey, from, to, category, amount_top_up, currentTime, "0"));
        }

        progressDialog.dismiss();
        return newTransactionKey;
    }

    private String transfer(int transfer_amount, String currentTime) {
        SharedPreferences preferences = getSharedPreferences("currentStudent", MODE_PRIVATE);
        String from = preferences.getString("student_roll_number", "");
        String to = getIntent().getStringExtra("transfer_to");

        DatabaseReference reference = databaseReference.child("Student");
        Query query_1 = reference.orderByChild("student_roll_number")
                .equalTo(preferences.getString("student_roll_number", ""));

        query_1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot studentSnapshot = snapshot.getChildren().iterator().next();
                    long currentAmount = studentSnapshot.child("student_amount").getValue(Long.class);
                    studentSnapshot.getRef().child("student_amount")
                            .setValue(currentAmount - transfer_amount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        String student_roll_number = preferences.getString("student_roll_number", "");
        DatabaseReference transactionRef = databaseReference.child("Transaction").child(student_roll_number);
        DatabaseReference newTransaction = transactionRef.push();
        List<Transaction> transactions = loadTransaction(transactionRef);
        String newTransactionKey = newTransaction.getKey();
        if (transactions.size() > 0) {
            newTransaction.setValue(new Transaction(newTransactionKey, from, to, "Chuyển tiền đến ví khác", transfer_amount, currentTime, transactions.get(transactions.size() - 1).getHash()));
        } else {
            newTransaction.setValue(new Transaction(newTransactionKey, from, to, "Chuyển tiền đến ví khác", transfer_amount, currentTime, "0"));
        }

        Query query_2 = reference.orderByChild("student_roll_number")
                .equalTo(to);

        query_2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot studentSnapshot = snapshot.getChildren().iterator().next();
                    long currentAmount = studentSnapshot.child("student_amount").getValue(Long.class);
                    studentSnapshot.getRef().child("student_amount")
                            .setValue(currentAmount + transfer_amount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference transactionRef_2 = databaseReference.child("Transaction").child(to);
        DatabaseReference newTransaction_2 = transactionRef_2.push();
        List<Transaction> transactions_2 = loadTransaction(transactionRef);
        if (transactions.size() > 0) {
            newTransaction_2.setValue(new Transaction(newTransaction_2.getKey(), from, to, "Nhận tiền từ ví khác", transfer_amount, currentTime, transactions_2.get(transactions.size() - 1).getHash()));
        } else {
            newTransaction_2.setValue(new Transaction(newTransaction_2.getKey(), from, to, "Nhận tiền từ ví khác", transfer_amount, currentTime, "0"));
        }

        progressDialog.dismiss();
        return newTransactionKey;
    }

    private String paying(int transaction_amount, String currentTime, String category, String payingType) {
        SharedPreferences preferences = getSharedPreferences("currentStudent", MODE_PRIVATE);
        String student_roll_number = preferences.getString("student_roll_number", "");

        // Update student_amount in Firebase
        DatabaseReference reference = databaseReference.child("Student");
        Query query = reference.orderByChild("student_roll_number")
                .equalTo(preferences.getString("student_roll_number", ""));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot studentSnapshot = snapshot.getChildren().iterator().next();
                    long currentAmount = studentSnapshot.child("student_amount").getValue(Long.class);
                    studentSnapshot.getRef().child("student_amount")
                            .setValue(currentAmount - transaction_amount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        DatabaseReference feeRef = databaseReference.child("Fee").child(student_roll_number).child(payingType);
        feeRef.setValue(0);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(payingType, 0);
        editor.apply();

        DatabaseReference transactionRef = databaseReference.child("Transaction").child(student_roll_number);
        DatabaseReference newTransaction = transactionRef.push();
        List<Transaction> transactions = loadTransaction(transactionRef);
        String newTransactionKey = newTransaction.getKey();
        if (transactions.size() > 0) {
            newTransaction.setValue(new Transaction(newTransactionKey, student_roll_number, "Đại học FPT", category, transaction_amount, currentTime, transactions.get(transactions.size() - 1).getHash()));
        } else {
            newTransaction.setValue(new Transaction(newTransactionKey, student_roll_number, "Đại học FPT", category, transaction_amount, currentTime, "0"));
        }
        progressDialog.dismiss();
        return newTransactionKey;
    }

    private List<Transaction> loadTransaction(DatabaseReference transactionRef) {
        List<Transaction> transactions = new ArrayList<>();
        transactionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    transactions.clear();
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        transactions.add(issue.getValue(Transaction.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return transactions;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    private void handleZaloPayPayment(int total) {
        CreateOrder orderApi = new CreateOrder();
        try {
            @SuppressLint("DefaultLocale") JSONObject data = orderApi.createOrder(String.format("%d", total));
            String code = data.getString("returncode");
            if (code.equals("1")) {
                String token = data.getString("zptranstoken");
                ZaloPaySDK.getInstance().payOrder(PINActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        String transactionKey = topUp(total, dataEncode.getCurrentTime());
                        navigateToTransactionResult(transactionKey, total, dataEncode.getCurrentTime());
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        Intent intent1 = new Intent(PINActivity.this, MainActivity.class);
                        Toast.makeText(PINActivity.this, "Hủy thanh toán", Toast.LENGTH_SHORT).show();
                        startActivity(intent1);
                        finish();
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        Intent intent1 = new Intent(PINActivity.this, MainActivity.class);
                        Toast.makeText(PINActivity.this, "Lỗi thanh toánn", Toast.LENGTH_SHORT).show();
                        startActivity(intent1);
                        finish();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToTransactionResult(String transactionKey, int amount, String currentTime) {
        Intent resultIntent = new Intent(PINActivity.this, TransactionResultActivity.class);
        resultIntent.putExtra("Transaction key", transactionKey);
        resultIntent.putExtra("Transaction amount", amount);
        resultIntent.putExtra("Transaction time", currentTime);
        startActivity(resultIntent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(binding.btnConfirmPIN.getWindowToken(), 0);
        }
    }
}
