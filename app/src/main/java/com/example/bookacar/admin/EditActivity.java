package com.example.bookacar.admin;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import com.example.bookacar.BaseActivity;
import com.example.bookacar.R;
import com.example.bookacar.databinding.ActivityEditBinding;
import com.example.bookacar.util.Constants;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class EditActivity extends BaseActivity {

    private String encodeImg;
    private ActivityEditBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit);

        binding.imgAvatar.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImage.launch(intent);
        });

        binding.btnAddDriver.setOnClickListener(view -> {
            addFood();
        });
    }

    private void addFood () {
        showProgressDialog(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> driver = new HashMap<>();
        driver.put(Constants.KEY_PHONE_NUMBER, binding.edtPhone.getText().toString());
        driver.put(Constants.KEY_NAME, binding.edtName.getText().toString());
        driver.put(Constants.KEY_PASSWORD, binding.edtPassword.getText().toString());
        driver.put(Constants.KEY_IMAGE, encodeImg);
        driver.put(Constants.KEY_TYPE_USER, "driver");
        db.collection(Constants.KEY_COLLECTION_ACCOUNT)
                .add(driver)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    showProgressDialog(false);
                    finish();
                });
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        binding.imgAvatar.setImageBitmap(bitmap);
                        encodeImg = encodeImage(bitmap);
                    } catch (FileNotFoundException e) {

                    }
                }
            }
    );

    private String encodeImage (Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}