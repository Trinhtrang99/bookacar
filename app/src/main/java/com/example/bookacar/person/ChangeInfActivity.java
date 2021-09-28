package com.example.bookacar.person;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.bookacar.BaseActivity;
import com.example.bookacar.R;
import com.example.bookacar.databinding.ActivityChangeInfBinding;
import com.example.bookacar.util.Constants;
import com.example.bookacar.util.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class ChangeInfActivity extends BaseActivity {
    private ActivityChangeInfBinding binding;
    private String encodeImg;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_inf);
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding.imgAvar.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            pickImage.launch(photoPickerIntent);
        });
        binding.save.setOnClickListener(v -> {
            updateAccount();
        });
        binding.imgBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void updateAccount() {
        showProgressDialog(true);
        //if (binding.edtPassword.getText().toString().equals(binding.edtPasswordAgain.getText().toString())) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            HashMap<String, Object> user = new HashMap<>();
            user.put(Constants.KEY_NAME, binding.edtName.getText().toString());
            user.put(Constants.KEY_PHONE_NUMBER, binding.edtSdt.getText().toString());
            user.put(Constants.KEY_IMAGE, encodeImg);
            database.collection(Constants.KEY_COLLECTION_ACCOUNT)
                    .whereEqualTo(Constants.KEY_PHONE_NUMBER, preferenceManager.getString(Constants.KEY_PHONE_NUMBER))
                    .get()
                    .addOnCompleteListener( task -> {
                        if (task.isSuccessful() && task.getResult() != null
                                && task.getResult().getDocuments().size() > 0) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                            database.collection(Constants.KEY_COLLECTION_ACCOUNT)
                                    .document(documentSnapshot.getId())
                                    .update(user)
                                    .addOnSuccessListener(documentReference -> {
                                        showProgressDialog(false);
                                        onBackPressed();
                                    });

                        }
                    });
        /*} else {
            Toast.makeText(getApplicationContext(), "Khong dung", Toast.LENGTH_SHORT).show();
        }*/
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getData() != null) {
                    try {
                        final Uri imageUri = result.getData().getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        encodeImg = encodeImage(selectedImage);
                        binding.imgAvar.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(ChangeInfActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(ChangeInfActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
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