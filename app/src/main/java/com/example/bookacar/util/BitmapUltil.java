package com.example.bookacar.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class BitmapUltil {

    public static Bitmap getBitmap (String encodeImage) {
        if (encodeImage != null) {
            try {
                byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            } catch (Exception e) {

            }
        }

        return null;
    }
}
