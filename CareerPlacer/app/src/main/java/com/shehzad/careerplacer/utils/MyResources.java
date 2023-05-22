package com.shehzad.careerplacer.utils;

import static android.os.Build.VERSION_CODES.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class MyResources {
    static ProgressDialog pd;

    public static void showProgressDialog(Context context, String message) {
        pd = new ProgressDialog(context);
        pd.setMessage(message);
        pd.setCancelable(false);
        pd.show();
    }

    public static void dismissProgressDialog() {
        pd.hide();
    }

    public static void showToast(Context context, String message, String length) {
        if (length.equals("short")) Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static byte[] compressImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static String getDateTime(String type) {
        Calendar calendar = Calendar.getInstance();
        if (type.equals("date")) {
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
            return currentDate.format(calendar.getTime());
        } else {
            SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
            return currentTime.format(calendar.getTime());
        }
    }

    public static TextWatcher createTextWatcher(TextInputLayout event) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                event.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    public static int getRandomColor() {
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        return Color.argb(255, red, green, blue);
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

}
