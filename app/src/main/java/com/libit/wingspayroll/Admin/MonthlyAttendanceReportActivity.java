package com.libit.wingspayroll.Admin;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import com.libit.wingspayroll.Network.ApiServices;
import com.libit.wingspayroll.Network.StaticDataHelper;
import com.libit.wingspayroll.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;



public class MonthlyAttendanceReportActivity extends AppCompatActivity{

    Button btngetMonthlyReport;
    ProgressDialog loading;
    EditText fromdateEt, todateet;
    int mYear, mMonth, mDay;
    Calendar c;
    String fromDate,toDate;
    String User;
    Spinner Userspinner;
    List<String> user_id, user_name;
    String userdata;
    ApiServices mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_attendance_report);

        btngetMonthlyReport=findViewById(R.id.getAttendanceMonthly);
        fromdateEt=findViewById(R.id.fromdatetv);
        todateet=findViewById(R.id.todatetv);
        Userspinner = findViewById(R.id.Userspinner);


        User = StaticDataHelper.getStringFromPreferences(MonthlyAttendanceReportActivity.this, "UserName");
        loading = new ProgressDialog(this);
        loading.setTitle("Get Attendance");
        loading.setMessage("Please wait...");

        //getuser();

        Userspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userdata = user_id.get(position);
                //Snackbar.make(view, " EmpId " +userdata+ " Selected ", Snackbar.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        fromdateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MonthlyAttendanceReportActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                c.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
                                String strDate = format.format(c.getTime());
                                fromdateEt.setText(strDate);
                                fromDate=strDate.toString();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        todateet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MonthlyAttendanceReportActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                c.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
                                String strDate = format.format(c.getTime());
                                todateet.setText(strDate);
                                toDate=strDate.toString();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        btngetMonthlyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(fromdateEt.getText().toString()) || TextUtils.isEmpty(todateet.getText().toString())) {
                    Toast.makeText(MonthlyAttendanceReportActivity.this, "Please select date", Toast.LENGTH_SHORT).show();
                } else {
                    //loading.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

//    private void getuser() {
//        mService = ApiClient.getClient().create(ApiServices.class);
//        Call<ResponseBody> userCall = mService.getuser();
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
//                    user_id   = new ArrayList<>();
//                    user_name = new ArrayList<>();
//
//                    if (stauts.equalsIgnoreCase("200")) {
//
//                        JSONArray coursearray = responsejobj.getJSONArray("Data");
//                        for (int i = 0; i < coursearray.length(); i++) {
//                            JSONObject dataobj = coursearray.getJSONObject(i);
//
//                            String empid = dataobj.getString("empid");
//                            String empname = dataobj.getString("empname");
//
//                            user_id.add(empid);
//                            user_name.add(empname);
//
//
//                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
//                                    android.R.layout.simple_spinner_item, user_name);
//                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            Userspinner.setAdapter(adapter);
//
//                        }
//                    } else {
//                        Toast.makeText(MonthlyAttendanceReportActivity.this, message, Toast.LENGTH_SHORT).show();
//
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

