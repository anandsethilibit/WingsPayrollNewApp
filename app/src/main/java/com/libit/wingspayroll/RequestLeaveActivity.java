package com.libit.wingspayroll;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.libit.wingspayroll.Model.RequestModel;
import com.libit.wingspayroll.Network.ApiClient;
import com.libit.wingspayroll.Network.ApiServices;
import com.libit.wingspayroll.Network.StaticDataHelper;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestLeaveActivity extends AppCompatActivity {
    LinearLayout LvforFullday, Lvforhalfdy, halfbuttonLv, uploadimageLv;
    String type = "Select";
    EditText selectdate, Fromdate, todate, reasonforleave;
    EditText Numberofdays;
    Button buttonSubmit;
    Calendar c;
    int mYear, mMonth, mDay;
    ImageView backbtn;
    RequestModel model;
    List<RequestModel> services;
    ApiServices mService;
    TextView nametxt,leavebalance;
    ImageView detail;
    TextView buttonChoosePhoto;
    private int PICK_IMAGE_REQUEST = 0;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    ImageView ivImagePhoto;
    String uploadImagePhoto="No Image";
    String opiningbalace;
    ProgressBar progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_leave);

        ivImagePhoto = findViewById(R.id.ivImagePhoto);
        uploadimageLv = findViewById(R.id.uploadimageLv);
        halfbuttonLv = findViewById(R.id.halfbuttonLv);
        //leavebalance = findViewById(R.id.leavebalance);
        Lvforhalfdy = findViewById(R.id.Lvforhalfdy);
        LvforFullday = findViewById(R.id.LvforFullday);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        selectdate = findViewById(R.id.selectdate);
        reasonforleave = findViewById(R.id.reasonforleave);
        Numberofdays=findViewById(R.id.edt_Numberofdays);
        progressbar =findViewById(R.id.progressbar);
        progressbar.setVisibility(View.GONE);
        nametxt = findViewById(R.id.nametxt);
        nametxt.setText("Request Leave");
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        detail = findViewById(R.id.detail);
        detail.setVisibility(View.VISIBLE);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RequestDetailActivity.class));
            }
        });


        String TodayDate = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());

        String id = StaticDataHelper.getStringFromPreferences(RequestLeaveActivity.this,"EmpId");
        //getopeaning(id);

        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.IntervalSpinner);
        spinner.setItems("Select", "Half Day", "Full Day");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, item + " Selected ", Snackbar.LENGTH_LONG).show();
                type = item;

                if (type.equalsIgnoreCase("Select")) {
                    Lvforhalfdy.setVisibility(View.GONE);
                    LvforFullday.setVisibility(View.GONE);
                    reasonforleave.setVisibility(View.GONE);
                    halfbuttonLv.setVisibility(View.GONE);
                    uploadimageLv.setVisibility(View.GONE);
                }if (type.equalsIgnoreCase("Half Day")) {
                    Lvforhalfdy.setVisibility(View.VISIBLE);
                    LvforFullday.setVisibility(View.GONE);
                    reasonforleave.setVisibility(View.VISIBLE);
                    halfbuttonLv.setVisibility(View.VISIBLE);
                    uploadimageLv.setVisibility(View.GONE);
                }if (type.equalsIgnoreCase("Full Day")) {
                    LvforFullday.setVisibility(View.VISIBLE);
                    Lvforhalfdy.setVisibility(View.GONE);
                    reasonforleave.setVisibility(View.VISIBLE);
                    halfbuttonLv.setVisibility(View.GONE);
                    uploadimageLv.setVisibility(View.GONE);
                }

            }
        });

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date tomorrow = calendar.getTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String todayAsString = dateFormat.format(today);
        String dayAfterTomorrow = dateFormat.format(tomorrow);

        System.out.println(todayAsString);
        System.out.println(dayAfterTomorrow);

        selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog datePickerDialog = new DatePickerDialog(RequestLeaveActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                c.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
                                String strDate = format.format(c.getTime());
                                selectdate.setText(strDate);
                            }
                        }, mYear, mMonth, mDay);
                // set maximum date to be selected as today
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });


        Fromdate = findViewById(R.id.Fromdate);
        Fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RequestLeaveActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                c.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
                                String strDate = format.format(c.getTime());
                                Fromdate.setText(strDate);
                            }
                        }, mYear, mMonth, mDay);
                // set maximum date to be selected as today
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        todate = findViewById(R.id.todate);
        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RequestLeaveActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                c.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
                                String strDate = format.format(c.getTime());
                                todate.setText(strDate);

                            }
                        }, mYear, mMonth, mDay);
                // set maximum date to be selected as today
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();

            }
        });

        buttonChoosePhoto = findViewById(R.id.buttonChoosePhoto);
        buttonChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialogImage();
            }
        });


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                if (type.equalsIgnoreCase("Half day")) {
                    if (TextUtils.isEmpty(selectdate.getText().toString()) || TextUtils.isEmpty(reasonforleave.getText().toString())) {
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(RequestLeaveActivity.this, "Please fill all credentials", Toast.LENGTH_SHORT).show();
                    } else {
                        String Id = StaticDataHelper.getStringFromPreferences(RequestLeaveActivity.this, "EmpId");
                        String Status = "Half day";
                        String SelectDate = selectdate.getText().toString();
                        String FDate = selectdate.getText().toString();  //" ";
                        String TDate = selectdate.getText().toString();  //" ";
                        String reason = reasonforleave.getText().toString();

                        model = new RequestModel();
                        model.setUsername(Id);
                        model.setLeaveRequestType(Status);
                        //model.setSelectDate(SelectDate);
                        model.setFDate(FDate);
                        model.setTDate(TDate);
                        model.setReason(reason);
                        //model.setUID(Id);
                        //model.setImagepath(uploadImagePhoto);
                        requestleave(model);
                    }

                } else if (type.equalsIgnoreCase("Full day")) {
                    if (TextUtils.isEmpty(Fromdate.getText().toString()) || TextUtils.isEmpty(todate.getText().toString())
                            || TextUtils.isEmpty(reasonforleave.getText().toString()) ) {
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(RequestLeaveActivity.this, "Please fill all credentials", Toast.LENGTH_SHORT).show();
                    } else if(TextUtils.isEmpty(Numberofdays.getText().toString().trim())){
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(RequestLeaveActivity.this, "Please Enter Number of Days", Toast.LENGTH_SHORT).show();
                    }else {
                        String Id = StaticDataHelper.getStringFromPreferences(RequestLeaveActivity.this, "EmpId");
                        String Status = "Full day";
                        String SelectDate = " ";
                        String FDate = Fromdate.getText().toString();
                        String TDate = todate.getText().toString();
                        String reason = reasonforleave.getText().toString();
                        String Days =Numberofdays.getText().toString().trim();

                        model = new RequestModel();
                        model.setUsername(Id);
                        model.setLeaveRequestType(Status);
                        model.setFDate(FDate);
                        model.setTDate(TDate);
                        model.setReason(reason);
                        model.setDays(Days);
                        //model.setUID(Id);
                        //model.setImagepath(uploadImagePhoto);
                        requestleave(model);
                    }
                }
            }
        });
    }

    private void requestleave(RequestModel model) {
        mService = ApiClient.getClient().create(ApiServices.class);
        Call<ResponseBody> userCall = mService.Leaverequest(model);
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
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Requested Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finishAffinity();

                    } else {
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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

    private void showPictureDialogImage() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
//        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Gallery",
                "Camera"};

        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallaryImage();
                                break;
                            case 1:
                                takePhotoFromCameraImage();
                                break;
                        }
                    }
                });

        pictureDialog.show();
    }

    public void choosePhotoFromGallaryImage() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, PICK_IMAGE_REQUEST);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void takePhotoFromCameraImage() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, 1);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    ivImagePhoto.setVisibility(View.VISIBLE);
                    ivImagePhoto.setImageURI(selectedImage);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        bitmap = ImageResizer.reduceBitmapSize(bitmap, 307200);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        uploadImagePhoto = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    } catch (Exception ee) {
                        Toast.makeText(getApplicationContext(), "error_3" + ee, Toast.LENGTH_SHORT).show();
                        ee.printStackTrace();
                    }
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {

                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    photo = ImageResizer.reduceBitmapSize(photo, 307200);
                    ivImagePhoto.setVisibility(View.VISIBLE);
                    ivImagePhoto.setImageBitmap(photo);

                    Log.d("Camera Image-->", "onActivityResult: " + photo);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    uploadImagePhoto = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                }
                break;
            //--------------------------------------------------------------------------
        }
    }


//    public void getopeaning(String userId) {
//        mService = ApiClient.getClient().create(ApiServices.class);
//        Call<ResponseBody> userCall = mService.getopeaningbalance(userId);
//        userCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                ResponseBody user = response.body();
//                try {
//                    String data = user.string();
//
//                    JSONObject responsejobj = new JSONObject(data);
//                    String stauts = responsejobj.getString("Status_Code");
//                    String message = responsejobj.getString("Message");
//
//                    if (stauts.equalsIgnoreCase("200")) {
//                        JSONArray coursearray = responsejobj.getJSONArray("data");
//                        for (int i = 0; i < coursearray.length(); i++) {
//                            JSONObject dataobj = coursearray.getJSONObject(i);
//
//                            opiningbalace = dataobj.getString("opiningbalace");
//                        }
//                        leavebalance.setText(" My Leave Balance :-"+opiningbalace);
//                    }else{
//                        // Toast.makeText(RequestDetail.this, message, Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//    }

}