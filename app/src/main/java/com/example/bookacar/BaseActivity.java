package com.example.bookacar;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private final CustomProgressDialogFragment customProgressDialogFragment = new CustomProgressDialogFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showProgressDialog(boolean show) {
        try {
            if (show) {
                if (!customProgressDialogFragment.isShowing()) {
                    customProgressDialogFragment.setShowing(true);
                    customProgressDialogFragment.show(getSupportFragmentManager(), "");
                }
            } else {
                if (customProgressDialogFragment.isShowing()) {
                    customProgressDialogFragment.dismiss();
                    customProgressDialogFragment.setShowing(false);
                }
            }
        } catch (Exception ex) {

        }
    }

}
