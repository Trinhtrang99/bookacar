package com.example.bookacar;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bookacar.databinding.ActivityMainBinding;
import com.example.bookacar.home.HomeFragment;
import com.example.bookacar.map.MapFragmentView;
import com.example.bookacar.map.Onclick;
import com.example.bookacar.notication.NoticationFragment;
import com.example.bookacar.person.PersonFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements Onclick {
    ActivityMainBinding binding;
    MapFragmentView fragmentView;
    public static Onclick onclick;
    public static String don, txtDen;
    private static EditText edtDon, edtDen;
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] RUNTIME_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        onclick = this::OnClick;

        /*binding.btnbook.setOnClickListener(v -> {

            displayAlertDialog();
        });*/
        loadFragment(new HomeFragment());
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        loadFragment(new HomeFragment());
                        backStackFragment(new HomeFragment());
                        binding.map.setVisibility(View.GONE);
                        return true;
                    case R.id.Map:
                        binding.map.setVisibility(View.GONE);
                        loadFragment(new NoticationFragment());
                        backStackFragment(new NoticationFragment());

//                        binding.map.setVisibility(View.VISIBLE);
//                        if (hasPermissions(this, RUNTIME_PERMISSIONS)) {
//                            setupMapFragmentView();
//                        } else {
//                            ActivityCompat
//                                    .requestPermissions(MainActivity.this, RUNTIME_PERMISSIONS, REQUEST_CODE_ASK_PERMISSIONS);
//                        }

                        return true;
                    case R.id.Acc:
                        binding.map.setVisibility(View.GONE);
                        loadFragment(new PersonFragment());
                        backStackFragment(new PersonFragment());
                        return true;
                }
                return false;
            }
        });
    }

    private void backStackFragment(Fragment fragment) {
        fragment = getSupportFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName());
        if (fragment != null && fragment.isVisible()) {
            getSupportFragmentManager().popBackStack();
        }
    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.frame_layout, fragment, fragment.getClass().getSimpleName());
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private boolean hasPermissions(BottomNavigationView.OnNavigationItemSelectedListener context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                for (int index = 0; index < permissions.length; index++) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {

                        /*
                         * If the user turned down the permission request in the past and chose the
                         * Don't ask again option in the permission request system dialog.
                         */
                        if (!ActivityCompat
                                .shouldShowRequestPermissionRationale(this, permissions[index])) {
                            Toast.makeText(this, "Required permission " + permissions[index]
                                            + " not granted. "
                                            + "Please go to settings and turn on for sample app",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Required permission " + permissions[index]
                                    + " not granted", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                setupMapFragmentView();
                break;
            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void setupMapFragmentView() {
        fragmentView = new MapFragmentView(this);

    }

   

    @Override
    public void OnClick() {

    }

    public static void setTextForEdt(String txt) {
        edtDon.setText(txt);
    }

    public static void setTextForDen(String txt) {
        edtDen.setText(txt);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }
}