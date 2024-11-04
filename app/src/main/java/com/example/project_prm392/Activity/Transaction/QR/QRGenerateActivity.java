package com.example.project_prm392.Activity.Transaction.QR;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.project_prm392.databinding.ActivityQrgenarateBinding;


public class QRGenerateActivity extends AppCompatActivity {
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    ActivityQrgenarateBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQrgenarateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        QRGenerate();
        handleButton();
    }

    private void QRGenerate() {
        SharedPreferences currentStudent = getSharedPreferences("currentStudent", MODE_PRIVATE);
        String phoneNumber = currentStudent.getString("student_phone", "");
        String QR_URL = "https://img.vietqr.io/image/TPB-" + phoneNumber + "-qr_only.png";
        Glide.with(this).load(QR_URL).override(350, 350).
                transform(new CenterCrop(), new RoundedCorners(5))
                .into(binding.imgQR);
    }

    private void handleButton() {
        binding.btnQrGenerateBack.setOnClickListener(v -> finish());
        binding.btnQRGenerateScanQR.setOnClickListener(v -> {

            openCamera();
        });
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                cameraActivityResultLauncher.launch(takePictureIntent);
            } else {
                Toast.makeText(this, "Không tìm thấy ứng dụng camera để mở", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Quyền truy cập máy ảnh bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private final ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null) {
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    Log.d("TAG",imageBitmap.toString());
                }
            }
    );
}