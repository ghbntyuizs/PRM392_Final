package com.example.project_prm392.Activity.Transaction.Paying;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.project_prm392.Activity.Base.BaseActivity;
import com.example.project_prm392.Constant.Constant;
import com.example.project_prm392.Security.PINActivity;
import com.example.project_prm392.databinding.ActivitySemesterFeeBinding;
import com.example.project_prm392.entities.Interface.StudentMoneyCallBack;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SemesterFeeActivity extends BaseActivity {
    private boolean isInitialized = false;
    private ActivitySemesterFeeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySemesterFeeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setVariable();
        Log.d(TAG, "Hecker 1 created");
        binding.btnBackToFee.setOnClickListener(v -> finish());
    }

    private void setVariable() {
        if (isInitialized) return;
        isInitialized = true;
        SharedPreferences pre = getSharedPreferences("currentStudent", MODE_PRIVATE);
        String student_roll_number = pre.getString("student_roll_number", "");
        String type = getIntent().getStringExtra("fee_type");
        Intent intent = new Intent(SemesterFeeActivity.this, PINActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d(TAG, "data" + pre.getAll().toString());
        if (type != null) {
            switch (type) {
                case Constant.SEMESTER_FEE_TYPE:
                    binding.tvFeeTitle.setText("Thanh toán học phí");
                    binding.tvFeeAmount.setText(dataEncode.formatMoney(pre.getInt(Constant.SEMESTER_FEE_TYPE, 0)));
                    binding.tvFee.setText("Học phí");
                    binding.tvContentFee.setText(student_roll_number + " thanh toán học phí.");
                    int feeMoney = pre.getInt(Constant.SEMESTER_FEE_TYPE, 0);
                    binding.btnPayFee.setOnClickListener(v -> {
                        isValidStudentMoney(Constant.SEMESTER_FEE_TYPE, isValid -> {
                            if (isValid) {
                                intent.putExtra("transaction_amount", feeMoney);
                                intent.putExtra("transaction_type", 3);
                                startActivity(intent);
                                finishAffinity();
                            }
                        });
                    });
                    break;
                case Constant.DORMITORY_FEE:
                    binding.tvFeeTitle.setText("Thanh toán ký túc xá");
                    binding.tvFeeAmount.setText(dataEncode.formatMoney(pre.getInt(Constant.DORMITORY_FEE, 0)));
                    binding.tvFee.setText("Phí ký túc xá");
                    binding.tvContentFee.setText(pre.getString("student_roll_number", "") + " thanh toán phí ký túc xá.");
                    binding.btnPayFee.setOnClickListener(v -> {
                        isValidStudentMoney(Constant.DORMITORY_FEE, isValid -> {
                            if (isValid) {
                                intent.putExtra("transaction_amount", pre.getInt(Constant.DORMITORY_FEE, 0));
                                intent.putExtra("transaction_type", 4);
                                startActivity(intent);
                                finishAffinity();
                            }
                        });
                    });
                    break;
                case Constant.ADDITIONAL_DORMITORY_FEE_TYPE:
                    binding.tvFeeTitle.setText("Thanh toán phụ trội ký túc xá");
                    binding.tvFeeAmount.setText(dataEncode.formatMoney(pre.getInt(Constant.ADDITIONAL_DORMITORY_FEE_TYPE, 0)));
                    binding.tvFee.setText("Phí phụ trội ký túc xá");
                    binding.tvContentFee.setText(pre.getString("student_roll_number", "") + " thanh toán phụ trội ký túc xá.");
                    binding.btnPayFee.setOnClickListener(v -> {
                        isValidStudentMoney(Constant.ADDITIONAL_DORMITORY_FEE_TYPE, isValid -> {
                            if (isValid) {
                                intent.putExtra("transaction_amount", pre.getInt(Constant.ADDITIONAL_DORMITORY_FEE_TYPE, 0));
                                intent.putExtra("transaction_type", 5);
                                startActivity(intent);
                                finishAffinity();
                            }
                        });
                    });
                    break;
                case Constant.LIBRARY_FINES:
                    binding.tvFeeTitle.setText("Thanh toán khoản phạt thư viện");
                    binding.tvFeeAmount.setText(dataEncode.formatMoney(pre.getInt(Constant.LIBRARY_FINES, 0)));
                    binding.tvFee.setText("Phí khoản phạt thư viện");
                    binding.tvContentFee.setText(pre.getString("student_roll_number", "") + " thanh toán phí khoản phạt thư viện.");
                    binding.btnPayFee.setOnClickListener(v -> {
                        isValidStudentMoney(Constant.LIBRARY_FINES, isValid -> {
                            if (isValid) {
                                intent.putExtra("transaction_amount", pre.getInt(Constant.LIBRARY_FINES, 0));
                                intent.putExtra("transaction_type", 6);
                                startActivity(intent);
                                finishAffinity();
                            }
                        });
                    });
                    break;
                case Constant.RE_STUDY_FEE:
                    binding.tvFeeTitle.setText("Thanh toán tiền học lại");
                    binding.tvFeeAmount.setText(dataEncode.formatMoney(pre.getInt(Constant.RE_STUDY_FEE, 0)));
                    binding.tvFee.setText("Phí tiền học lại");
                    binding.tvContentFee.setText(pre.getString("student_roll_number", "") + " thanh toán tiền học lại.");
                    binding.btnPayFee.setOnClickListener(v -> {
                        isValidStudentMoney(Constant.RE_STUDY_FEE, isValid -> {
                            if (isValid) {
                                intent.putExtra("transaction_amount", pre.getInt(Constant.RE_STUDY_FEE, 0));
                                intent.putExtra("transaction_type", 7);
                                startActivity(intent);
                                finishAffinity();
                            }
                        });
                    });
                    break;
                case Constant.SCHOLARSHIP_PENALTY_FEE:
                    binding.tvFeeTitle.setText("Thanh toán tiền phạt học bổng");
                    binding.tvFeeAmount.setText(dataEncode.formatMoney(pre.getInt(Constant.SCHOLARSHIP_PENALTY_FEE, 0)));
                    binding.tvFee.setText("Phí phạt học bổng");
                    binding.tvContentFee.setText(pre.getString("student_roll_number", "") + " thanh toán tiền phạt học bổng.");
                    binding.btnPayFee.setOnClickListener(v -> {
                        isValidStudentMoney(Constant.SCHOLARSHIP_PENALTY_FEE, isValid -> {
                            if (isValid) {
                                intent.putExtra("transaction_amount", pre.getInt(Constant.SCHOLARSHIP_PENALTY_FEE, 0));
                                intent.putExtra("transaction_type", 8);
                                startActivity(intent);
                                finishAffinity();
                            }
                        });
                    });
                    break;
            }
        }

    }

    //    feeType : semester_fee
    private void isValidStudentMoney(String feeType, StudentMoneyCallBack callBack) {
        SharedPreferences pre = getSharedPreferences("currentStudent", MODE_PRIVATE);
        String student_roll_number = pre.getString("student_roll_number", "");
        DatabaseReference reference = database.getReference("Student");
        Query query = reference.orderByChild("student_roll_number")
                .equalTo(pre.getString("student_roll_number", ""));
        int feeMoney = pre.getInt(feeType, 0);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot studentDataSnapshot = snapshot.getChildren().iterator().next();
                    if (studentDataSnapshot.hasChild("student_amount")) {
                        long currentAmount = studentDataSnapshot.child("student_amount").getValue(Long.class);

                        if (currentAmount < feeMoney) {
                            Toast.makeText(SemesterFeeActivity.this, "Số tiền không đủ để thực hiện giao dịch", Toast.LENGTH_SHORT).show();
                            callBack.onCallback(false);
                            return;
                        }
                        DatabaseReference transactionRef = database.getReference("Transaction").child(student_roll_number);
                        Query query = transactionRef.orderByChild("time").startAt(dataEncode.getTodayDateString());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int totalTransactionAmountToday = 0;
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String from = dataSnapshot.child("from").getValue(String.class);
                                    String to = dataSnapshot.child("to").getValue(String.class);
                                    int transactionAmount = dataSnapshot.child("amount").getValue(Integer.class);

                                    //Check if the transaction is related to the current student
                                    if (from.equals(student_roll_number) || to.equals(student_roll_number)) {
                                        totalTransactionAmountToday += transactionAmount;
                                    }
                                }
                                //Check transaction limit
                                if (!(totalTransactionAmountToday + feeMoney <= 100000000)) {
                                    Toast.makeText(SemesterFeeActivity.this, "Giao dịch vượt quá hạn mức hôm nay", Toast.LENGTH_SHORT).show();
                                    callBack.onCallback(false);
                                    return;
                                }
                                callBack.onCallback(true);
                                return;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                callBack.onCallback(false);
                                return;
                            }
                        });


                    } else {
                        Toast.makeText(SemesterFeeActivity.this, "Không có dữ liệu về số tiền của sinh viên", Toast.LENGTH_SHORT).show();
                        callBack.onCallback(false);
                        return;
                    }
                } else {
                    Toast.makeText(SemesterFeeActivity.this, "Không tìm thấy sinh viên", Toast.LENGTH_SHORT).show();
                    callBack.onCallback(false);
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callBack.onCallback(false);
                return;
            }
        });
    }
}