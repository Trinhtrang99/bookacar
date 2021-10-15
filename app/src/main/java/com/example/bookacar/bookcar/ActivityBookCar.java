package com.example.bookacar.bookcar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookacar.BaseActivity;
import com.example.bookacar.ICallBackBookCar;
import com.example.bookacar.R;
import com.example.bookacar.driver.BookCarActivity;
import com.example.bookacar.firebase.network.NotificationApi;
import com.example.bookacar.firebase.network.RetrofitInstance;
import com.example.bookacar.firebase.network.response.NotificationData;
import com.example.bookacar.firebase.network.response.PushNotification;
import com.example.bookacar.map.GPSLocation;
import com.example.bookacar.test.ChangePositionMap;
import com.example.bookacar.util.Constants;
import com.example.bookacar.util.PreferenceManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.here.android.mpa.common.GeoCoordinate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityBookCar extends BaseActivity implements ICallBackBookCar {
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
    private TextView txt_DiemDon;
    String txtlocation;
    TextView txtChangeDon;
    TextView txtChangeDen;
    TextView edtDen;
    TextView donGia;
    private Double lat;
    public static Double log;
    public static Double txtDonlat, txtDonLong;
    private Button m_calculateRouteButton;
    private PreferenceManager preferenceManager;
    private String typeBook;
    private int Cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_car);
        m_calculateRouteButton = findViewById(R.id.btnBook);
        mBottomSheetLayout = findViewById(R.id.lnbtn);
        txt_DiemDon = findViewById(R.id.txt_diemDon);
        donGia = findViewById(R.id.donGia);
        edtDen = findViewById(R.id.edtDen);

        // nhân giá tiền ở đây nhé
        Cost = getIntent().getIntExtra("Cost", 0);
        donGia.setText(donGia + "vnđ");
        preferenceManager = new PreferenceManager(getApplicationContext());
        typeBook = getIntent().getStringExtra(Constants.KEY_TYPE_BOOK);

        sheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout);
        GPSLocation location = new GPSLocation(this);
        txtlocation = location.getLatitude() + "," + location.getLongitude();
        txtDonlat = location.getLatitude();
        txtDonLong = location.getLongitude();
        String url = "https://revgeocode.search.hereapi.com/v1/revgeocode?at=" + txtlocation + "&lang=vn-VN&apikey=qDWjknIKplTBlIqEYb9J__Oj2LtJtwEoZPZQpjX-A5Y";
        Data(url);
        header_Arrow_Image = findViewById(R.id.bottom_sheet_arrow);
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

        txtChangeDon = findViewById(R.id.txtChangeDon);
        txtChangeDen = findViewById(R.id.txtChangeDen);
        txtChangeDen.setOnClickListener(v -> {
            Intent i = new Intent(this, ChangePositionMap.class);
            startActivityForResult(i, 99);
        });
        txtChangeDon.setOnClickListener(v -> {
            Intent i = new Intent(this, ChangePositionMap.class);
            startActivityForResult(i, 100);
        });

        m_calculateRouteButton.setText("Xem thông tin");
        m_calculateRouteButton.setOnClickListener(v -> {
            if (lat != null && log != null && txtDonlat != null && txtDonLong != null) {
                m_calculateRouteButton.setEnabled(true);
                GeoCoordinate geoCoordinateStart = new GeoCoordinate(lat, log);
                GeoCoordinate geoCoordinateEnd = new GeoCoordinate(txtDonlat, txtDonLong);
                m_mapFragmentView.addFakeRoute(geoCoordinateStart, geoCoordinateEnd);
                if (m_mapFragmentView.m_map != null && m_mapFragmentView.m_mapRoute != null) {
                    m_mapFragmentView.m_map.removeMapObject(m_mapFragmentView.m_mapRoute);
                    m_mapFragmentView.m_mapRoute = null;
                    m_calculateRouteButton.setText("Xem thông tin");
                    addBook();
                } else {
                    showProgressDialog(true);
                    m_mapFragmentView.calculateRoute(this);
                }
            } else {
                // m_calculateRouteButton.setEnabled(false);
            }
        });

        if (hasPermissions(this, RUNTIME_PERMISSIONS)) {
            setupMapFragmentView();
        } else {
            ActivityCompat
                    .requestPermissions(this, RUNTIME_PERMISSIONS, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    private String TOPIC = "/topics/myTopic";

    private void addBook() {
        showProgressDialog(true);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> books = new HashMap<>();
        books.put(Constants.KEY_LOCATION_START, txt_DiemDon.getText().toString());
        books.put(Constants.KEY_LOCATION_END, edtDen.getText().toString());
        books.put(Constants.KEY_PHONE_NUMBER, preferenceManager.getString(Constants.KEY_PHONE_NUMBER));
        books.put(Constants.KEY_TOTAL_MONEY, m_mapFragmentView.getTotalMoney());
        books.put(Constants.KEY_NAME, preferenceManager.getString(Constants.KEY_NAME));
        books.put(Constants.KEY_TYPE_BOOK, typeBook);
        books.put(Constants.KEY_DATE, new Date());
        db.collection(Constants.KEY_COLLECTION_BOOK)
                .document(preferenceManager.getString(Constants.KEY_ID_USER))
                .set(books)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    showProgressDialog(false);

                    FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);
                    String message = preferenceManager.getString(Constants.KEY_NAME) + " Đã đặt xe";
                    PushNotification pushNotification = new PushNotification(
                            new NotificationData(typeBook, message),
                            TOPIC
                    );

                    sendNotification(pushNotification);

                    //finish();
                });
    }

    private void sendNotification(PushNotification pushNotification) {
        RetrofitInstance.getRetrofit().create(NotificationApi.class)
                .postNotification(pushNotification)
                .enqueue(new Callback<PushNotification>() {
                    @Override
                    public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ActivityBookCar.this, "OK", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PushNotification> call, Throwable t) {
                        Log.d("KMFG", t.getMessage());
                    }
                });
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

    public void Data(final String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Json(response);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void Json(JSONObject response) {
        try {

            JSONArray jsonArrayItems = response.getJSONArray("items");
            for (int i = 0; i < jsonArrayItems.length(); i++) {
                JSONObject jsonItem = jsonArrayItems.getJSONObject(i);
                txt_DiemDon.setText(jsonItem.getString("title"));
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 99) {
                data.getStringExtra("location");
                lat = data.getDoubleExtra("lat", -1.0);
                log = data.getDoubleExtra("long", -1.0);
                edtDen.setText(data.getStringExtra("title"));
                if (lat != null && log != null && txtDonlat != null && txtDonLong != null) {
                    m_calculateRouteButton.setEnabled(true);
                }
            } else if (requestCode == 100) {
                txt_DiemDon.setText(data.getStringExtra("title"));
                txtDonlat = data.getDoubleExtra("lat", -1.0);
                txtDonLong = data.getDoubleExtra("long", -1.0);
                if (lat != null && log != null && txtDonlat != null && txtDonLong != null) {
                    m_calculateRouteButton.setEnabled(true);
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void hideProgressDialog() {
        showProgressDialog(false);
    }
}