package com.libit.wingspayroll;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import com.libit.wingspayroll.Model.SendAttendenceModel2;
import com.libit.wingspayroll.Network.ApiClient;
import com.libit.wingspayroll.Network.ApiServices;
import com.libit.wingspayroll.Network.StaticDataHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Camera2Activity extends AppCompatActivity {
    private int PICK_IMAGE_REQUEST = 0;
    private static final int MY_CAMERA_PERMISSION_CODE = 200;
    private static final int CAMERA_REQUEST = 1888;
    String uploadImagePhoto = "No Image";
    ImageView ivImagePhoto;
    ProgressDialog pd;
    ApiServices mService;
    //    FusedLocationProviderClient fusedLocationProviderClient;
//    private final static int REQUEST_CODE = 100;
//    private String Address1 = null;
//    double latitude_double,longitude_double;
//    String latitudeCam, longitudeCam;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);

        pd = new ProgressDialog(Camera2Activity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setTitle("Loading");
        pd.setMessage("Uploading Attendence");


        ivImagePhoto = findViewById(R.id.ivImagePhoto);


        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
            cameraIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            cameraIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
            //getLastLocation();
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
                //getLastLocation();
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


            String EmpIdd = StaticDataHelper.getStringFromPreferences(getApplicationContext(), "EmpId");
            String Code = StaticDataHelper.getStringFromPreferences(getApplicationContext(), "Code");
            String Qrcode = StaticDataHelper.getStringFromPreferences(getApplicationContext(), "Qrcode");
            String UnitId = StaticDataHelper.getStringFromPreferences(getApplicationContext(), "UnitId");
            String date = StaticDataHelper.getStringFromPreferences(getApplicationContext(), "date");
            String time = StaticDataHelper.getStringInPreferences(getApplicationContext(), "time");
            String address = StaticDataHelper.getStringInPreferences(getApplicationContext(), "address");
            String lati = StaticDataHelper.getStringInPreferences(getApplicationContext(), "latitude");
            String longi = StaticDataHelper.getStringInPreferences(getApplicationContext(), "longitude");

//            String latitude1=String.valueOf(latitude_double);
//            String longitude1=String.valueOf(longitude_double);

            SendAttendenceModel2 model = new SendAttendenceModel2();
            model.setEmpId(EmpIdd);
            model.setEmpCode(Code);
            model.setBarCodeId(Qrcode);
            model.setInLongitude(longi);
            model.setInLatitude(lati);
            model.setDate(date);
            model.setInTime(time);
            model.setAtten_image(uploadImagePhoto);
            model.setUnitId(UnitId);
            if (address.equals("null")){
                model.setInAddress(address);
            }else{
                model.setInAddress(address);
            }
            pd.dismiss();
            Toast.makeText(getApplicationContext(), "" + address, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pd.show();
                    AttendeceData(model);
                }
            }, 2000);

        }
    }


    private void AttendeceData(SendAttendenceModel2 model) {
        mService = ApiClient.getClient().create(ApiServices.class);
        Call<ResponseBody> userCall = mService.MarkAttendece2(model);
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
                        StaticDataHelper.setBooleanInPreferences(getApplicationContext(), "islogin", false);
                        startActivity(new Intent(Camera2Activity.this, LoginActivity.class));
                        Camera2Activity.this.finishAffinity();
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

    //----------------------------Location code----------------------------------------

//    public void onRequestPermissionsResult(@NonNull String[] permissions, int requestCode, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch (requestCode) {
//            case 100: {
//                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED)
//                {
//                    Log.i("MYTAG", "Permission has been denied by user");
//                    return;
//
//                } else {
//
//                }
//            }
//        }
//    }
//
//    private void getLastLocation() {
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
//
//            fusedLocationProviderClient.getLastLocation()
//                    .addOnSuccessListener(new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            if(location !=null){
//                                Geocoder geocoder = new Geocoder(CameraActivity.this, Locale.getDefault());
//                                List<Address>addresses = null;
//                                try {
//                                    addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
//                                    Address1 = "Address : - "+addresses.get(0).getAddressLine(0);
//                                    latitude_double = addresses.get(0).getLatitude();
//                                    longitude_double = addresses.get(0).getLongitude();
//                                    Toast.makeText(CameraActivity.this, Address1 + latitude_double +longitude_double, Toast.LENGTH_SHORT).show();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        }
//                    });
//
//        }else {
//            askPermission();
//        }
//    }
//
//    private void askPermission() {
//
//        ActivityCompat.requestPermissions(CameraActivity.this,new String[]
//                {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
//        try {
//            int LOCATION_MODE = Settings.Secure.getInt(CameraActivity.this.getContentResolver(), Settings.Secure.LOCATION_MODE);
//            if (LOCATION_MODE == 0 || LOCATION_MODE == 1) {
////                showSettingsAlert();
//                Toast.makeText(CameraActivity.this, "Please on your Gps Loction ", Toast.LENGTH_LONG).show();
//            } else {
//                getLastLocation();
//            }
//        } catch (Settings.SettingNotFoundException e) {
//            e.printStackTrace();
//            Toast.makeText(CameraActivity.this, "Please on your Gps Loction ", Toast.LENGTH_LONG).show();
//        }
//    }

}
