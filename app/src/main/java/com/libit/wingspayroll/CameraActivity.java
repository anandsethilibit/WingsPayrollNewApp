package com.libit.wingspayroll;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationRequest;
import com.libit.wingspayroll.Model.SendAttendenceModel;
import com.libit.wingspayroll.Network.ApiClient;
import com.libit.wingspayroll.Network.ApiServices;
import com.libit.wingspayroll.Network.StaticDataHelper;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraActivity extends AppCompatActivity implements LocationListener {
    private int PICK_IMAGE_REQUEST = 0;
    private static final int MY_CAMERA_PERMISSION_CODE = 200;
    String uploadImagePhoto = "No Image";
    ImageView ivImagePhoto;
    private static final int CAMERA_REQUEST = 1888;
    ProgressDialog pd;
    ApiServices mService;

    LocationManager lm1;
    private String Address1;
    Boolean GpsStatus;
    double longitude_double, latitude_double;
    private LocationManager locationManager;
    private String provider1;
    double latitude, longitude;
    Context context;
    StringBuilder result;
    private String TxtLatituteField;
    private String TxtLongitudeField;
    private int requestCode;
    private String[] permissions;
    private int[] grantResults;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);



        pd = new ProgressDialog(CameraActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setTitle("Loading");
        pd.setMessage("Uploading Attendence");


        ivImagePhoto = findViewById(R.id.ivImagePhoto);

        if(ContextCompat.checkSelfPermission(CameraActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CameraActivity.this,new String []{android.Manifest.permission.CAMERA},MY_CAMERA_PERMISSION_CODE);
        }


        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
            cameraIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            cameraIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
            getlocation();
            startActivityForResult(cameraIntent, CAMERA_REQUEST);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                cameraIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                cameraIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                getlocation();
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            photo = ImageResizer.reduceBitmapSize(photo, 307200);

            ivImagePhoto.setImageBitmap(photo);
            pd.show();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            uploadImagePhoto = Base64.encodeToString(imageBytes, Base64.DEFAULT);



            String EmpId = StaticDataHelper.getStringInPreferences(getApplicationContext(), "EmpId");
            String Code = StaticDataHelper.getStringInPreferences(getApplicationContext(), "Code");
            String UnitId = StaticDataHelper.getStringFromPreferences(getApplicationContext(), "UnitId");
            String address = StaticDataHelper.getStringInPreferences(getApplicationContext(), "address");
            String latitude1=String.valueOf(latitude_double);
            String longitude1=String.valueOf(longitude_double);

//          Toast.makeText(CameraActivity.this,name+"\n"+Id,Toast.LENGTH_SHORT).show();

            SendAttendenceModel model = new SendAttendenceModel();
            model.setEmpId(EmpId);
            model.setEmpCode(Code);
            model.setInLatitude(latitude1);
            model.setInLongitude(longitude1);
            model.setUnitId(UnitId);
            model.setAtten_image(uploadImagePhoto);

            if (address.equals("null")){
                model.setInAddress(Address1);
            }else{
                model.setInAddress(address);
            }

            Toast.makeText(getApplicationContext(), "" + Address1, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pd.show();
                    AttendeceData(model);
                }
            }, 2000);
        }
    }

    private void AttendeceData(SendAttendenceModel model) {
        mService = ApiClient.getClient().create(ApiServices.class);
        Call<ResponseBody> userCall = mService.MarkAttendece(model);
        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody user = response.body();

                try {
                    String data = user.string();
                    JSONObject responsejobj = new JSONObject(data);
                    String stauts = responsejobj.getString("Status_Code");
                    String message = responsejobj.getString("Message");

                    if (stauts.equalsIgnoreCase("200")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CameraActivity.this, LoginActivity.class));
                        CameraActivity.this.finish();
                        pd.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }



    //------------------------------------------Location code----------------------------------------
    @Override
    public void onLocationChanged(Location location) {
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());

        TxtLatituteField = String.valueOf(lat);
        TxtLongitudeField = String.valueOf(lng);

     /*   TxtLatituteField.setText(String.valueOf(lat));
        TxtLongitudeField.setText(String.valueOf(lng));*/
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
    }


    public void onRequestPermissionsResult(@NonNull String[] permissions, int requestCode, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 101: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {

                    Log.i("MYTAG", "Permission has been denied by user");
                    return;
                } else {

                }
            }
        }
    }

    public boolean checkLocationPermission() {
//        ActivityCompat.requestPermissions(this, new String[]{
//
        LocationRequest locationRequest = LocationRequest.create();

// Set the priority of the request
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

// Set the update interval to 10 seconds
        locationRequest.setInterval(1000 * 10);

// Set the fastest update interval to 5 seconds
        locationRequest.setFastestInterval(1000 * 5);
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);


        return (res == PackageManager.PERMISSION_GRANTED);
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

    public void getlocation() {
        pd.show();
        lm1 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = lm1.isProviderEnabled(LocationManager.GPS_PROVIDER);


        if (GpsStatus) {
            checkLocationPermission();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            Location location = lm1.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location == null) {
                lm1.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
            }

            double longituded = 0.0;
            try {

                longitude_double = location.getLongitude();


            } catch (Exception e) {
                pd.dismiss();
                Toast.makeText(this, "Unable to fetch location....Please try again", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
            double latituded = 0.0;
            try {

                latitude_double = location.getLatitude();

                geocede();

            } catch (Exception e) {

            }

        } else {
            pd.dismiss();
            Toast.makeText(getApplicationContext(), "GPS Not Enabled", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Please turn on your GPS", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(getApplicationContext(), Dashboard.class));
//            finish();
        }
    }

    public void geocede() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(CameraActivity.this, Locale.getDefault());
        try {
            pd.dismiss();
            addresses = geocoder.getFromLocation(latitude_double, longitude_double, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            Address1 = address;
        } catch (IOException e) {
            pd.dismiss();
            e.printStackTrace();
            Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(getApplicationContext(), Dashboard.class));
//            finish();
        } catch (NullPointerException e) {
            pd.dismiss();
            e.printStackTrace();
            Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(getApplicationContext(), Dashboard.class));
//            finish();
        }
    }
}
