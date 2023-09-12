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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.libit.wingspayroll.Adapter.MobileAttendanceAdapter;
import com.libit.wingspayroll.Model.MobileAttendanceModel;
import com.libit.wingspayroll.Network.ApiClient;
import com.libit.wingspayroll.Network.ApiServices;
import com.libit.wingspayroll.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MobileAttendanceActivity extends AppCompatActivity {
    ImageView backbtn;
    TextView nametxt;
    ProgressDialog loading;
    EditText Selectdate;
    int mYear, mMonth, mDay;
    Calendar c;
    String date;
    Button MobileAttendance;
    RecyclerView MAttendanceRecycler;
    List<MobileAttendanceModel> services;
    MobileAttendanceAdapter adapter;
    ApiServices mService;
    String User;
    Button Viewdetails;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_attendance);
        loading = new ProgressDialog(this);
        loading.setTitle("Get Data");
        loading.setMessage("Please wait...");
        loading.setCancelable(false);
        nametxt = findViewById(R.id.nametxt);
        backbtn = findViewById(R.id.backbtn);
        MobileAttendance=findViewById(R.id.btn_MobileAttendance);
        MAttendanceRecycler=findViewById(R.id.MattendanceRecycler);
        MAttendanceRecycler.setLayoutManager(new LinearLayoutManager(this));
        Selectdate=findViewById(R.id.selectdateEt);
        nametxt.setText("Mobile Attendance Report");
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        Selectdate.setText(date);

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
                                date=strDate.toString();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });



//        Viewdetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MobileAttendanceActivity.this,MobAttDetailViewActivity.class);
//                startActivity(intent);
//            }
//        });



        MobileAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(Selectdate.getText().toString())) {
                    Toast.makeText(MobileAttendanceActivity.this, "Please select date", Toast.LENGTH_SHORT).show();
                } else {
                    services = new ArrayList<>();
                    loading.show();
                    MobileAttendance(date);
                }
            }
        });
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void MobileAttendance(String date) {
        mService = ApiClient.getClient().create(ApiServices.class);
        Call<ResponseBody> userCall = mService.Mobileattendance(date);
        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody user = response.body();
                try {
                    String data = user.string();
                    JSONObject responsejobj = new JSONObject(data);
                    services.clear();
                    String stauts = responsejobj.getString("Status_Code");
                    String message = responsejobj.getString("Message");

                    if (stauts.equalsIgnoreCase("200")) {
                        JSONArray coursearray = responsejobj.getJSONArray("Data1");

                        for (int i = 0; i < coursearray.length(); i++) {
                            JSONObject dataobj = coursearray.getJSONObject(i);
                            String EmpName = dataobj.getString("EmpName");
                            String EmpCode = dataobj.getString("EmpCode");
                            String Date = dataobj.getString("Date");
                            String Id = dataobj.getString("id");
                            String Days = dataobj.getString("Days");
                            String Intime = dataobj.getString("InTime");
                            String Outtime = dataobj.getString("OutTime");
                            String Image = dataobj.getString("Image");
                            String InAddress = dataobj.getString("InAddress");

//                            double intime  =  Double.parseDouble(Intime);
//                            double outtime =  Double.parseDouble(Outtime);
//                            double TotalTime = outtime - intime;

                            MobileAttendanceModel model = new MobileAttendanceModel();
                            model.setName(EmpName);
                            model.setId(Id);
                            model.setEmpCode(EmpCode);
                            model.setDate(Date);
                            model.setDays(Days);
                            model.setInTime(Intime);
                            model.setOutTime(Outtime);
                            model.setAtten_image(Image);
                            model.setInAddress(InAddress);
                            services.add(model);

                            adapter = new MobileAttendanceAdapter(getApplicationContext(),services);
                            MAttendanceRecycler.setAdapter(adapter);
                            final MobileAttendanceModel finalModel = model;
                            loading.dismiss();
                        }
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }else {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    loading.dismiss();
                    e.printStackTrace();
                } catch (IOException e) {
                    loading.dismiss();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MobileAttendanceActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}