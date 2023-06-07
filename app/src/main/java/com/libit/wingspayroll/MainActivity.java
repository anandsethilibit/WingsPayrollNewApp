package com.libit.wingspayroll;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.libit.wingspayroll.Admin.MobileAttendanceActivity;
import com.libit.wingspayroll.Network.StaticDataHelper;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    String User,UserType;
    TextView usernametv,gradienttv;
    CardView attendecebtn,leaveRequestbtn,ShowAttendancebtn,DocumentUploadbtn,DocumentShowbtn,BirthdayAnniversary,
            UpdateProfile,MonthlyAttendance,LeaveApprovel,MobileAttendance,DailyAttendance;
    TextView myattendance;


    private static final int PERMISSION_REQUEST_CODE_LOCATION = 100;
    double longitude_double, latitude_double;
    private String Address1 = null;
    Boolean GpsStatus;
    LocationManager lm1;
    double employerlat, employerlong;
    ProgressDialog lodingBar;
    ProgressBar prgruser;
    //EditText loctionet;


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.title_bar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.itemlogout:
                StaticDataHelper.setBooleanInPreferences(MainActivity.this, "islogin",false);
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,Splash2Activity.class));
                MainActivity.this.finishAffinity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        User = StaticDataHelper.getStringFromPreferences(MainActivity.this, "Name");
        UserType = StaticDataHelper.getStringFromPreferences(MainActivity.this, "Usertype");


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lodingBar = new ProgressDialog(MainActivity.this);
        lodingBar.setTitle("Get Location...");
        lodingBar.setMessage("Please wait...");

        prgruser=findViewById(R.id.prgruser);
        prgruser.setVisibility(View.GONE);
        attendecebtn = findViewById(R.id.Card_attendecebtn);
        leaveRequestbtn=findViewById(R.id.Card_leaveRequestbtn);
        ShowAttendancebtn=findViewById(R.id.Card_ShowAttendancebtn);
        DocumentUploadbtn=findViewById(R.id.Card_DocumentUpload);
        DocumentShowbtn=findViewById(R.id.Card_DocumentShow);
        BirthdayAnniversary=findViewById(R.id.card_birthAnniver);
        UpdateProfile =findViewById(R.id.Card_updateProfile);
        MonthlyAttendance=findViewById(R.id.card_MonthlyAttendance);
        MobileAttendance=findViewById(R.id.card_MobileAttendance);
        LeaveApprovel=findViewById(R.id.card_LeaveApprovel);
        DailyAttendance=findViewById(R.id.card_DailyAttendance);
        myattendance=findViewById(R.id.txt_myattendance);
        usernametv = findViewById(R.id.usernametv);
        usernametv.setText("User :-" + User);

        //Runtime permissions
        if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String []{
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }


//        gradienttv = findViewById(R.id.gradienttv);
//        gradienttv.setText("Mabicons Application".toUpperCase());
//        TextPaint paint = gradienttv.getPaint();
//        float width = paint.measureText("Future-focused: Building a Better Life");
//        Shader textShader = new LinearGradient(0, 0, width, gradienttv.getTextSize(),
//                new int[]{
//                        Color.parseColor("#F97C3C"),
//                        Color.parseColor("#FDB54E"),
//                        Color.parseColor("#64B678"),
//                        Color.parseColor("#478AEA"),
//                        Color.parseColor("#8446CC"),
//                }, null, Shader.TileMode.CLAMP);
//        gradienttv.getPaint().setShader(textShader);
//        gradienttv.startAnimation((Animation) AnimationUtils.loadAnimation(MainActivity.this, R.anim.myanime));


        if(UserType.equalsIgnoreCase("Admin")){
            attendecebtn.setVisibility(View.GONE);
            leaveRequestbtn.setVisibility(View.GONE);
            DocumentUploadbtn.setVisibility(View.GONE);
            DocumentShowbtn.setVisibility(View.GONE);
            UpdateProfile.setVisibility(View.GONE);
            BirthdayAnniversary.setVisibility(View.VISIBLE);
            ShowAttendancebtn.setVisibility(View.GONE);
            MonthlyAttendance.setVisibility(View.VISIBLE);
            LeaveApprovel.setVisibility(View.GONE);
            MobileAttendance.setVisibility(View.VISIBLE);
            DailyAttendance.setVisibility(View.VISIBLE);

        }else{
            attendecebtn.setVisibility(View.VISIBLE);
            leaveRequestbtn.setVisibility(View.GONE);
            DocumentUploadbtn.setVisibility(View.GONE);
            DocumentShowbtn.setVisibility(View.GONE);
            BirthdayAnniversary.setVisibility(View.VISIBLE);
            UpdateProfile.setVisibility(View.GONE);
            MonthlyAttendance.setVisibility(View.VISIBLE);
            myattendance.setText("My Attendance");
            ShowAttendancebtn.setVisibility(View.GONE);
            LeaveApprovel.setVisibility(View.GONE);
            MobileAttendance.setVisibility(View.GONE);
            DailyAttendance.setVisibility(View.GONE);
        }

        attendecebtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]
                                {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        101);
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 3);

                try {
                    int LOCATION_MODE = Settings.Secure.getInt(MainActivity.this.getContentResolver(), Settings.Secure.LOCATION_MODE);
                    if (LOCATION_MODE == 0 || LOCATION_MODE == 1) {
                        showSettingsAlert();
                        Toast.makeText(MainActivity.this, "Please on your Gps Loction ", Toast.LENGTH_LONG).show();
                    } else
                        //lodingBar.show();
                        getlocation1();
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Please on your Gps Loction ", Toast.LENGTH_LONG).show();
                }
            }
        });

        BirthdayAnniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BirthdayAndAnniverActivity.class);
                startActivity(intent);
            }
        });



        MonthlyAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, MonthlyAttendanceReport2Activity.class);
                startActivity(intent);
            }
        });


        MobileAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MobileAttendanceActivity.class);
                startActivity(intent);
            }
        });

        DailyAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DailyAttendanceReportActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    public void checkLocationPermission() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000 * 10);
        locationRequest.setFastestInterval(1000 * 5);
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
    }


    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public synchronized void onLocationChanged(Location l) {
            mLocation = l;
        }

        private Location mLocation = null;

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };


    public void getlocation1() {
        lodingBar.show();
        //prgruser.setVisibility(View.VISIBLE);
        lm1 = (LocationManager) getSystemService(android.content.Context.LOCATION_SERVICE);
        GpsStatus = lm1.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (GpsStatus) {
            checkLocationPermission();
            Location location = lm1.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location == null) {
                //lodingBar.dismiss();
                lm1.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, mLocationListener);
            }
            double longituded = 0.0;
            try {
                longitude_double = location.getLongitude();
                employerlong = longitude_double;
            } catch (Exception e) {
                getlocation1();
                lodingBar.dismiss();
//                Log.e("longitude", "checkout: " + e);
//                searchloc.performClick();
            }
            double latituded = 0.0;
            try {
                latitude_double = location.getLatitude();
                employerlat = latitude_double;
                geocede1();
                //lodingBar.dismiss();
            } catch (Exception e) {
//                Log.e("latitude", "checkout: " + e);
                lodingBar.dismiss();
            }
        } else {
//              Toast.makeText(getApplicationContext(), "GPS Not Enabled", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Please turn on your GPS", Toast.LENGTH_LONG).show();
            lodingBar.dismiss();
        }
    }


    public void geocede1() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude_double, longitude_double, 1);
            String address = addresses.get(0).getAddressLine(0);
//            loctionet.setText(address);
            Address1 = address;
            Toast.makeText(MainActivity.this, Address1, Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this, "address", Toast.LENGTH_SHORT).show();
            lodingBar.dismiss();
            prgruser.setVisibility(View.GONE);
            StaticDataHelper.setStringInPreferences(this, "address", address);
            startActivity(new Intent(getApplicationContext(), CameraActivity.class));
//            StaticDataHelper.setStringInPreferences(this, "latitude", String.valueOf(latitude_double));
//            StaticDataHelper.setStringInPreferences(this, "longitude", String.valueOf(longitude_double));
        } catch (IOException | NullPointerException e) {
            lodingBar.dismiss();
            e.printStackTrace();
        }
    }


    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("GPS is not Enabled!");
        alertDialog.setMessage("Do you want to turn on GPS?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getApplicationContext().startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

}