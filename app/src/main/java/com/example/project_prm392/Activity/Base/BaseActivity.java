package com.example.project_prm392.Activity.Base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import com.example.project_prm392.Helper.DataEncode;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class BaseActivity extends AppCompatActivity {
    public FirebaseDatabase database;
    public FirebaseAuth auth;
    public String TAG = "E-Wallet-Check";
    public DataEncode dataEncode = new DataEncode();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
    }

}
