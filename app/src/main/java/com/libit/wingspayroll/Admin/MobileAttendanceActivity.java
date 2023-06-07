package com.libit.wingspayroll.Admin;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.libit.wingspayroll.Network.ApiServices;
import com.libit.wingspayroll.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MobileAttendanceActivity extends AppCompatActivity {
    ImageView backbtn;
    TextView nametxt;
    ProgressDialog loading;
    EditText Selectdate;
    int mYear, mMonth, mDay;
    Calendar c;
    String Date;
    Button MobileAttendance;
    RecyclerView attendanceRecycler;
    //List<DailyAttendanceModel> services;
    //DailyAttendanceAdapter adapter;
    ApiServices mService;
    String User;


    Button Viewdetails;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_attendance);

        loading = new ProgressDialog(this);
        loading.setTitle("Get Attendance");
        loading.setMessage("Please wait...");

        nametxt = findViewById(R.id.nametxt);
        backbtn = findViewById(R.id.backbtn);
        Viewdetails=findViewById(R.id.btn_Viewdetails);
        MobileAttendance=findViewById(R.id.btn_MobileAttendance);
        //attendanceRecycler=findViewById(R.id.attendanceRecycler);
        Selectdate=findViewById(R.id.selectdateEt);
        nametxt.setText("Mobile Attendance Report");
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MobileAttendanceActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                c.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
                                String strDate = format.format(c.getTime());
                                Selectdate.setText(strDate);
                                Date=strDate.toString();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        Viewdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MobileAttendanceActivity.this,MobAttDetailViewActivity.class);
                startActivity(intent);
            }
        });

        MobileAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(Selectdate.getText().toString())) {
                    Toast.makeText(MobileAttendanceActivity.this, "Please select date", Toast.LENGTH_SHORT).show();
                } else {
                    //services = new ArrayList<>();
                    loading.show();
                    //DailyAttendance(Date);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


//    private void DailyAttendance(String date) {
//        mService = ApiClient.getClient().create(ApiServices.class);
//        Call<ResponseBody> userCall = mService.attendance(date);
//        userCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                ResponseBody user = response.body();
//                try {
//                    String data = user.string();
//                    JSONObject responsejobj = new JSONObject(data);
//
//                    services.clear();
//
//                    String stauts = responsejobj.getString("Status_Code");
//                    String message = responsejobj.getString("Message");
//
//                    if (stauts.equalsIgnoreCase("200")) {
//                        JSONArray coursearray = responsejobj.getJSONArray("Data");
//
//                        for (int i = 0; i < coursearray.length(); i++) {
//                            JSONObject dataobj = coursearray.getJSONObject(i);
//                            String Name = dataobj.getString("EMPNAME");
//                            String Intime = dataobj.getString("Intime");
//                            String Outtime = dataobj.getString("OutTime");
//
////                            double intime  =  Double.parseDouble(Intime);
////                            double outtime =  Double.parseDouble(Outtime);
////                            double TotalTime = outtime - intime;
//
//
//                            DailyAttendanceModel model = new DailyAttendanceModel();
//                            model.setName(Name);
//                            model.setDate(Date);
//                            model.setIntime(Intime);
//                            model.setOuttime(Outtime);
//                            services.add(model);
//
//                            adapter = new DailyAttendanceAdapter(getApplicationContext(),services);
//                            attendanceRecycler.setAdapter(adapter);
//                            final DailyAttendanceModel finalModel = model;
//                            loading.dismiss();
//                        }
//                        loading.dismiss();
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                    }else {
//                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(MobileAttendanceActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


}