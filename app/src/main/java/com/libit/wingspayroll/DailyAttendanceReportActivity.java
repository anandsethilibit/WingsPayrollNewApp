package com.libit.wingspayroll;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.libit.wingspayroll.Adapter.DailyAttendanceAdapter;
import com.libit.wingspayroll.Model.DailyAttendanceModel;
import com.libit.wingspayroll.Network.ApiClient;
import com.libit.wingspayroll.Network.ApiServices;
import com.libit.wingspayroll.Network.StaticDataHelper;

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

public class DailyAttendanceReportActivity extends AppCompatActivity {
    ImageView backbtn;
    TextView nametxt;
    Button btn_DailyAttendance;
    ProgressDialog loading;
    EditText Selectdate;
    int mYear, mMonth, mDay;
    Calendar c;
    String date;
    RecyclerView attendanceRecycler;
    List<DailyAttendanceModel> services;
    DailyAttendanceAdapter adapter;
    ApiServices mService;
    String User;
    List<String> unit_id,unit_name;
    Integer unitdata;
    Spinner Unitspinner;
    ProgressBar prgruser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_attendance_report);
        loading = new ProgressDialog(this);
        loading.setTitle("Get Attendance");
        loading.setMessage("Please wait...");
        btn_DailyAttendance=findViewById(R.id.btn_DailyAttendance);
        Selectdate=findViewById(R.id.selectdateEt);
        attendanceRecycler=findViewById(R.id.attendanceRecycler);
        prgruser=findViewById(R.id.prgruser);
        Unitspinner=findViewById(R.id.Unitspinner);
        backbtn = findViewById(R.id.backbtn);
        nametxt = findViewById(R.id.nametxt);
        nametxt.setText("Daily Attendance");
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getUnit();

        date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        Selectdate.setText(date);
        User = StaticDataHelper.getStringFromPreferences(DailyAttendanceReportActivity.this, "EmpId");


        Unitspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unitdata = Integer.valueOf(unit_id.get(position));
                Snackbar.make(view, " EmpId " +unitdata+ " Selected ", Snackbar.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(DailyAttendanceReportActivity.this,
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

        btn_DailyAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(Selectdate.getText().toString())) {
                    Toast.makeText(DailyAttendanceReportActivity.this, "Please select date", Toast.LENGTH_SHORT).show();
                } else {
                    services = new ArrayList<>();
                    loading.show();
                    DailyAttendance(date,unitdata);
                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getUnit() {
        prgruser.setVisibility(View.VISIBLE);
        mService = ApiClient.getClient().create(ApiServices.class);
        Call<ResponseBody> userCall = mService.getUnit();
        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody user = response.body();
                try {
                    String data = user.string();

                    JSONObject responsejobj = new JSONObject(data);
                    String stauts = responsejobj.getString("Status_Code");
                    String message = responsejobj.getString("Message");

                    unit_id   = new ArrayList<>();
                    unit_name = new ArrayList<>();

                    if (stauts.equalsIgnoreCase("200")) {
                        JSONArray coursearray = responsejobj.getJSONArray("unitList");
                        for (int i = 0; i < coursearray.length(); i++) {
                            JSONObject dataobj = coursearray.getJSONObject(i);
                            String unitId = dataobj.getString("unitId");
                            String UnitName = dataobj.getString("UnitName");

                            unit_id.add(unitId);
                            unit_name.add(UnitName);

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                    android.R.layout.simple_spinner_item, unit_name);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Unitspinner.setAdapter(adapter);
                        }
                        prgruser.setVisibility(View.GONE);
                    } else {
                        prgruser.setVisibility(View.GONE);
                        Toast.makeText(DailyAttendanceReportActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    prgruser.setVisibility(View.GONE);
                    e.printStackTrace();
                } catch (IOException e) {
                    prgruser.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void DailyAttendance(String date,Integer unitId) {
        mService = ApiClient.getClient().create(ApiServices.class);
        Call<ResponseBody> userCall = mService.attendance(date,unitId);
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
                        JSONArray coursearray = responsejobj.getJSONArray("Data");
                        for (int i = 0; i < coursearray.length(); i++) {
                            JSONObject dataobj = coursearray.getJSONObject(i);
                            String Name = dataobj.getString("EMPNAME");
                            String Intime = dataobj.getString("Intime");
                            String Outtime = dataobj.getString("OutTime");

//                            double intime  =  Double.parseDouble(Intime);
//                            double outtime =  Double.parseDouble(Outtime);
//                            double TotalTime = outtime - intime;

                            DailyAttendanceModel model = new DailyAttendanceModel();
                            model.setName(Name);
                            model.setDate(date);
                            model.setIntime(Intime);
                            model.setOuttime(Outtime);
                            services.add(model);

                            attendanceRecycler.setLayoutManager(new LinearLayoutManager(DailyAttendanceReportActivity.this));
                            adapter = new DailyAttendanceAdapter(getApplicationContext(),services);
                            attendanceRecycler.setAdapter(adapter);
                            final DailyAttendanceModel finalModel = model;
                            loading.dismiss();
                        }
                        loading.dismiss();
                        //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DailyAttendanceReportActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}