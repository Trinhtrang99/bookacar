package com.example.bookacar.driver;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.example.bookacar.BaseActivity;
import com.example.bookacar.ICallBackBookCar;
import com.example.bookacar.R;
import com.example.bookacar.bookcar.FragmentBookCar;
import com.example.bookacar.databinding.ActivityBookCar2Binding;
import com.example.bookacar.driver.model.UserBook;
import com.example.bookacar.test.ChangePositionMap;
import com.example.bookacar.util.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.search.AddressFilter;
import com.here.android.mpa.search.AutoSuggest;
import com.here.android.mpa.search.AutoSuggestPlace;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.TextAutoSuggestionRequest;

import java.util.List;
import java.util.Locale;

public class BookCarActivity extends BaseActivity implements ICallBackBookCar {
    private TextView tvDon, tvDen;
    private Button btnBook;
    private ICallBackBookCar callBackBookCar;
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] RUNTIME_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE
    };
    FragmentBookCar m_mapFragmentView;
    private LinearLayout mBottomSheetLayout;
    private BottomSheetBehavior sheetBehavior;
    private ImageView header_Arrow_Image;
    private TextView txt_DiemDon, txt_DiemDen;
    private ActivityBookCar2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_car2);
        initView();
        tvDon.setVisibility(View.INVISIBLE);
        tvDen.setVisibility(View.INVISIBLE);
        sheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout);
        header_Arrow_Image = findViewById(R.id.bottom_sheet_arrow);
        callBackBookCar = this::hideProgressDialog;

        header_Arrow_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                header_Arrow_Image.setRotation(slideOffset * 180);
            }
        });
        btnBook.setText("Bắt đầu đi");

        if (hasPermissions(this, RUNTIME_PERMISSIONS)) {
            setupMapFragmentView();
        } else {
            ActivityCompat
                    .requestPermissions(this, RUNTIME_PERMISSIONS, REQUEST_CODE_ASK_PERMISSIONS);
        }

        getUserBook();
    }

    private void getUserBook () {
        showProgressDialog(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_CONFIRM_BOOK)
                .get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    txt_DiemDon.setText(documentSnapshot.getString(Constants.KEY_LOCATION_START));
                    txt_DiemDen.setText(documentSnapshot.getString(Constants.KEY_LOCATION_END));

                    doSearch(documentSnapshot.getString(Constants.KEY_LOCATION_START));
                    doSearch(documentSnapshot.getString(Constants.KEY_LOCATION_END));

                    showProgressDialog(false);
                });
    }

    private void initView() {
        mBottomSheetLayout = findViewById(R.id.lnbtn);
        tvDon = findViewById(R.id.txtChangeDon);
        tvDen = findViewById(R.id.txtChangeDen);
        btnBook = findViewById(R.id.btnBook);
        txt_DiemDon = findViewById(R.id.txt_diemDon);
        txt_DiemDen = findViewById(R.id.edtDen);
    }

    private void setupMapFragmentView() {
        m_mapFragmentView = new FragmentBookCar(this);
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission)
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

    private Integer count = 0;
    private GeoCoordinate geoCoordinateStart;
    private GeoCoordinate geoCoordinateEnd;
    private void doSearch(String query) {
        //setSearchMode(true);
        TextAutoSuggestionRequest textAutoSuggestionRequest = new TextAutoSuggestionRequest(query);
        textAutoSuggestionRequest.setSearchCenter(m_mapFragmentView.m_map.getCenter());

        textAutoSuggestionRequest.execute(new ResultListener<List<AutoSuggest>>() {
            @Override
            public void onCompleted(final List<AutoSuggest> autoSuggests, ErrorCode errorCode) {
                if (errorCode == errorCode.NONE) {
                    //processSearchResults(autoSuggests);
                    AutoSuggestPlace autoSuggestPlace = (AutoSuggestPlace) autoSuggests.get(0);
                    Log.d("KMFG", autoSuggestPlace.getPosition().getLatitude() + "==");
                    Log.d("KMFG", autoSuggestPlace.getPosition().getLongitude() + "==");

                    Double lat = autoSuggestPlace.getPosition().getLatitude();
                    Double log = autoSuggestPlace.getPosition().getLongitude();
                    if (count == 0) {
                        geoCoordinateStart = new GeoCoordinate(lat, log);
                    } else {
                        geoCoordinateEnd = new GeoCoordinate(lat, log);
                    }
                    count++;
                    if (count == 2) {
                        showProgressDialog(true);
                        Toast.makeText(BookCarActivity.this, "OKOK", Toast.LENGTH_SHORT).show();
                        m_mapFragmentView.addFakeRoute(geoCoordinateStart, geoCoordinateEnd);
                        m_mapFragmentView.calculateRoute(callBackBookCar);
                    }
                } else {
                   // handleError(errorCode);
                }
            }
        });
    }

    @Override
    public void hideProgressDialog() {
        showProgressDialog(false);
    }
}