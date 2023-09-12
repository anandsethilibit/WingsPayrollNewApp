package com.libit.wingspayroll;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.libit.wingspayroll.Network.StaticDataHelper;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QrCodeScannerActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnScan;
    String QRcodeUrl;
    TextView txt_name,txt_Unit;

    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 100;
    double longitude_double, latitude_double;
    private String Address1 ;
    double employerlat, employerlong;

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 5*1000; //Delay for 5 seconds.  One second = 1000 milliseconds.


    ProgressDialog lodingBar;



    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.title_bar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.itemlogout:
                StaticDataHelper.setBooleanInPreferences(QrCodeScannerActivity.this, "islogin",false);
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(QrCodeScannerActivity.this,LoginActivity.class));
                QrCodeScannerActivity.this.finishAffinity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scanner);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String EmpIdd = StaticDataHelper.getStringFromPreferences(QrCodeScannerActivity.this, "EmpId");
        String Name = StaticDataHelper.getStringFromPreferences(QrCodeScannerActivity.this, "Name");
        String Code = StaticDataHelper.getStringFromPreferences(QrCodeScannerActivity.this, "Code");
        String Unit = StaticDataHelper.getStringFromPreferences(QrCodeScannerActivity.this, "UnitName");
        Address1 = StaticDataHelper.getStringInPreferences(getApplicationContext(), "address");

        txt_name=findViewById(R.id.txt_name);
        txt_Unit=findViewById(R.id.txt_Unit);
        txt_name.setText(Name);
        txt_Unit.setText(Unit);
        btnScan=findViewById(R.id.btnscan);
        lodingBar = new ProgressDialog(QrCodeScannerActivity.this);
        lodingBar.setTitle("Get Location...");
        lodingBar.setMessage("Please wait...");
        lodingBar.setCancelable(false);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();

//        handler.postDelayed( runnable = new Runnable() {
//            public void run() {
//                //do something
//                getLastLocation();
//                handler.postDelayed(runnable, delay);
//            }
//        }, delay);


        int PERMISSIONS_ALL = 1;
        String [] PERMISSSIONS = {
                Manifest.permission.CAMERA,
        };


        if(!hasPermisssions(this,PERMISSSIONS))
            ActivityCompat.requestPermissions(this,PERMISSSIONS,PERMISSIONS_ALL);
        else{

        }

        //Runtime permissions
        if(ContextCompat.checkSelfPermission(QrCodeScannerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(QrCodeScannerActivity.this,new String []{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }



        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Address1 != null && !Address1.isEmpty() && !Address1.equals("null")){
                    scanCode();
                }else {
                    Toast.makeText(QrCodeScannerActivity.this, "Please Check your GPS(Location Services) is ON And Provide Location Permissions Required", Toast.LENGTH_SHORT).show();
                    getLastLocation();
                }
//                Intent mainIntent = new Intent(MainActivity.this, SimpleScannerActivity.class);
//                startActivity(mainIntent);
            }
        });
    }

    private  void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on\nVolume down to flash off");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }


    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null)
        {
            QRcodeUrl=result.getContents();
            AlertDialog.Builder builder = new AlertDialog.Builder(QrCodeScannerActivity.this);
            builder.setTitle(" Qr Code No :- ");
            builder.setMessage(result.getContents());
            builder.setCancelable(false);
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    StaticDataHelper.setStringInPreferences(getApplicationContext(), "QRcode", QRcodeUrl);
                    //startActivity(new Intent(QrCodeScannerActivity.this, StartTestActivity.class));
                    //GoToURL(QRcodeUrl);
                    String currentString = QRcodeUrl;
                    String[] separated = currentString.split(",");
                    String Qrcode = separated[0];
                    String unitid =separated[1];
                    String QrId =separated[2];
                    Date currentTime = Calendar.getInstance().getTime();
                    String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
                    StaticDataHelper.setStringInPreferences(QrCodeScannerActivity.this, "Qrcode", Qrcode);
                    StaticDataHelper.setStringInPreferences(QrCodeScannerActivity.this, "unitid", unitid);
                    StaticDataHelper.setStringInPreferences(QrCodeScannerActivity.this, "QrId", QrId);
                    StaticDataHelper.setStringInPreferences(QrCodeScannerActivity.this, "time", String.valueOf(currentTime));
                    StaticDataHelper.setStringInPreferences(QrCodeScannerActivity.this, "date", date);
                    startActivity(new Intent(QrCodeScannerActivity.this, Camera2Activity.class));
                    dialogInterface.dismiss();

                    //scantext.setText(barcode.toString().trim());
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            }).show();
        }
    });

    public static boolean hasPermisssions(Context context, String...permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    void GoToURL(String url){
        Uri uri = Uri.parse(url);
        Intent intent= new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }


    private void getLastLocation() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location !=null){
                                Geocoder geocoder = new Geocoder(QrCodeScannerActivity.this, Locale.getDefault());
                                List<Address>addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                    Address1 = "Address : - "+addresses.get(0).getAddressLine(0);
                                    latitude_double = addresses.get(0).getLatitude();
                                    longitude_double = addresses.get(0).getLongitude();
                                    StaticDataHelper.setStringInPreferences(QrCodeScannerActivity.this, "address", Address1);
                                    StaticDataHelper.setStringInPreferences(QrCodeScannerActivity.this, "latitude", String.valueOf(latitude_double));
                                    StaticDataHelper.setStringInPreferences(QrCodeScannerActivity.this, "longitude", String.valueOf(longitude_double));
                                    Toast.makeText(QrCodeScannerActivity.this, Address1 +" , "+latitude_double+" , "+longitude_double, Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }else {
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(QrCodeScannerActivity.this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        try {
            int LOCATION_MODE = Settings.Secure.getInt(QrCodeScannerActivity.this.getContentResolver(), Settings.Secure.LOCATION_MODE);
            if (LOCATION_MODE == 0 || LOCATION_MODE == 1) {
                showSettingsAlert();
                Toast.makeText(QrCodeScannerActivity.this, "Please on your Gps Loction ", Toast.LENGTH_LONG).show();
            } else {
                getLastLocation();
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(QrCodeScannerActivity.this, "Please on your Gps Loction ", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else {
                Toast.makeText(this, "Permissions Required", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(QrCodeScannerActivity.this);
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