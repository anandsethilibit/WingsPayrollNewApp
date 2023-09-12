package com.libit.wingspayroll;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.libit.wingspayroll.Adapter.MonthlyAttendanceAdapter;
import com.libit.wingspayroll.Model.MonthlyAttendanceModel;
import com.libit.wingspayroll.Network.ApiClient;
import com.libit.wingspayroll.Network.ApiServices;
import com.libit.wingspayroll.Network.StaticDataHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonthlyAttendanceReport2Activity extends AppCompatActivity {
    ImageView backbtn;
    TextView nametxt;
    Button btngetMonthlyReport;
    ProgressDialog loading;
    String User,UserType,UserId;
    Spinner Userspinner;
    List<String> user_id, user_name;
    String userdata;
    ApiServices mService;
    TextView username;
    MonthlyAttendanceAdapter mAdapter;
    List<MonthlyAttendanceModel> services;
    ProgressBar prgruser;
    RecyclerView monthlyrecyclerview;

    int MonthNumber;
    String Month;
    String Year;
    String[] monthlist = {"Select Month","January", "February", "March","April","May","June","July","August","September","October","November","December"};
    String[] yearlist =  {"Select Year","2021","2022", "2023", "2024","2025","2026","2027","2028","2029","2030"};



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_attendance_report2);


        btngetMonthlyReport=findViewById(R.id.getAttendanceMonthly);
        Userspinner = findViewById(R.id.Userspinner);
        monthlyrecyclerview=findViewById(R.id.recview);
        prgruser=findViewById(R.id.prgruser);
        prgruser.setVisibility(View.GONE);
        username=findViewById(R.id.txtusername);
        backbtn = findViewById(R.id.backbtn);
        nametxt = findViewById(R.id.nametxt);
        nametxt.setText("Monthly Attendance");
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loading = new ProgressDialog(this);
        loading.setTitle("Get Attendance");
        loading.setMessage("Please wait...");

        User = StaticDataHelper.getStringFromPreferences(MonthlyAttendanceReport2Activity.this, "Name");
        UserType = StaticDataHelper.getStringFromPreferences(MonthlyAttendanceReport2Activity.this, "Usertype");
        UserId = StaticDataHelper.getStringFromPreferences(MonthlyAttendanceReport2Activity.this, "EmpId");

        if(UserType.equalsIgnoreCase("Admin")){
            getuser();
            Userspinner.setVisibility(View.VISIBLE);
            username.setVisibility(View.GONE);

        }else {
            Userspinner.setVisibility(View.GONE);
            username.setVisibility(View.VISIBLE);
            username.setText(User);
            userdata=UserId;
        }



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



        Spinner monthspin = (Spinner) findViewById(R.id.Monthspinner);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, monthlist);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthspin.setAdapter(aa);
        monthspin.setOnItemSelectedListener(new MonthlySpinnerClass());


        Spinner yearspin = (Spinner) findViewById(R.id.Yearspinner);
        ArrayAdapter<String> bb = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, yearlist);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearspin.setAdapter(bb);
        yearspin.setOnItemSelectedListener(new YearlySpinnerClass());



        btngetMonthlyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(Month) || TextUtils.isEmpty(Year)) {
                    Toast.makeText(MonthlyAttendanceReport2Activity.this, "Please select Month and Year", Toast.LENGTH_SHORT).show();
                } else {
                    if(Month.equalsIgnoreCase("Select Month")){
                        Toast.makeText(MonthlyAttendanceReport2Activity.this, "Please select Month ", Toast.LENGTH_SHORT).show();
                    }else if(Year.equalsIgnoreCase("Select Year")){
                        Toast.makeText(MonthlyAttendanceReport2Activity.this, "Please select Year ", Toast.LENGTH_SHORT).show();
                    }else {
                        loading.show();
                        getMonthlyAttendance(Month,Year,userdata,MonthNumber);
                    }
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getuser() {
        prgruser.setVisibility(View.VISIBLE);
        mService = ApiClient.getClient().create(ApiServices.class);
        Call<ResponseBody> userCall = mService.getEmployee();
        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody user = response.body();
                try {
                    String data = user.string();

                    JSONObject responsejobj = new JSONObject(data);
                    String stauts = responsejobj.getString("Status_Code");
                    String message = responsejobj.getString("Message");

                    user_id   = new ArrayList<>();
                    user_name = new ArrayList<>();

                    if (stauts.equalsIgnoreCase("200")) {

                        JSONArray coursearray = responsejobj.getJSONArray("Data");
                        for (int i = 0; i < coursearray.length(); i++) {
                            JSONObject dataobj = coursearray.getJSONObject(i);

                            String empid = dataobj.getString("EmployeeId");
                            String empname = dataobj.getString("EmpName");

                            user_id.add(empid);
                            user_name.add(empname);


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                    android.R.layout.simple_spinner_item, user_name);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Userspinner.setAdapter(adapter);
                        }
                        prgruser.setVisibility(View.GONE);
                    } else {
                        prgruser.setVisibility(View.GONE);
                        Toast.makeText(MonthlyAttendanceReport2Activity.this, message, Toast.LENGTH_SHORT).show();
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

    private void getMonthlyAttendance(String Month,String Year,String empid,Integer mon) {
        mService = ApiClient.getClient().create(ApiServices.class);
        Call<ResponseBody> userCall = mService.MonthlyAttendance(Month,Year,empid,mon);
        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody user = response.body();
                try {
                    String data = user.string();
                    JSONObject responsejobj = new JSONObject(data);
                    String stauts = responsejobj.getString("Status_Code");
                    String message = responsejobj.getString("Message");
                    services = new ArrayList<>();
                    services.clear();
                    if (stauts.equalsIgnoreCase("200")) {
                        JSONArray coursearray = responsejobj.getJSONArray("Data");
                        for (int i = 0; i < coursearray.length(); i++) {
                            JSONObject dataobj = coursearray.getJSONObject(i);

                            String EMPNAME = dataobj.getString("EMPNAME");
                            String THUMBNO = dataobj.getString("THUMBNO");
                            String userid = dataobj.getString("userid");
                            String Date = dataobj.getString("Date");
                            String Day = dataobj.getString("Day");
                            String Intime = dataobj.getString("Intime");
                            String OutTime = dataobj.getString("OutTime");
                            String Duration = dataobj.getString("Duration");
//                            String LateIn = dataobj.getString("LateIn");
//                            String LateOut = dataobj.getString("LateOut");
//                            String Ot = dataobj.getString("Ot");
                            String Status = dataobj.getString("Status");

                            MonthlyAttendanceModel model = new MonthlyAttendanceModel();
                            model.setEMPNAME(EMPNAME);
                            model.setTHUMBNO(THUMBNO);
                            model.setUserid(userid);
                            model.setDate(Date);
                            model.setDay(Day);
                            model.setIntime(Intime);
                            model.setOutTime(OutTime);
                            model.setDuration(Duration);
//                            model.setLateIn(LateIn);
//                            model.setLateOut(LateOut);
//                            model.setOverTime(Ot);
                            model.setStatus(Status);
                            services.add(model);

                            monthlyrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            mAdapter = new MonthlyAttendanceAdapter(MonthlyAttendanceReport2Activity.this,services);
                            monthlyrecyclerview.setAdapter(mAdapter);
                            loading.dismiss();
                        }
                    } else {
                        loading.dismiss();
                        Toast.makeText(MonthlyAttendanceReport2Activity.this, message, Toast.LENGTH_SHORT).show();
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

    class MonthlySpinnerClass implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            Month = monthlist[position];
            MonthNumber = position;
            //Toast.makeText(v.getContext(), "Your choose :" + monthlist[position],Toast.LENGTH_SHORT).show();
            //Toast.makeText(v.getContext(), "Your choose :" + MonthNumber, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    class YearlySpinnerClass implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            Year=yearlist[position];
            //Toast.makeText(v.getContext(), "Your choose :" + yearlist[position],Toast.LENGTH_SHORT).show();
            //Toast.makeText(v.getContext(), "Your choose :" + Year,Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}

