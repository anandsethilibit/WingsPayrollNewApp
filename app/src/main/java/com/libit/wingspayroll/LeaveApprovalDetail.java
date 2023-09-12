package com.libit.wingspayroll;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.libit.wingspayroll.Model.LeaveAppModel;
import com.libit.wingspayroll.Network.ApiClient;
import com.libit.wingspayroll.Network.ApiServices;
import com.libit.wingspayroll.Network.StaticDataHelper;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveApprovalDetail extends AppCompatActivity {
    TextView username,holidaytype,reason,dateone,tilldate,todatetv,fromdatetv,nametxt,NoOfDays;
    LinearLayout todatelv,noofdayLV;
    ImageView imageone,backbtn;
    ProgressDialog prgr;
    View vieww;
    ImageButton approvebtn,disapprovebtn;
    ApiServices mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_approval_detail);
        init();
        prgr = new ProgressDialog(LeaveApprovalDetail.this);
        prgr.setMessage("Loading...");
//      prgr.show();

        String UserId = StaticDataHelper.getStringFromPreferences(LeaveApprovalDetail.this, "EmpId");

        final String id = getIntent().getStringExtra("ID");
        String UserName = getIntent().getStringExtra("UserName");
        String type = getIntent().getStringExtra("Type");
//      String Date = getIntent().getStringExtra("Date");
        String EStatus = getIntent().getStringExtra("EStatus");
        String selectdate = getIntent().getStringExtra("selectdate");
        String fdate = getIntent().getStringExtra("fdate");
        String tdate = getIntent().getStringExtra("tdate");
        String reson = getIntent().getStringExtra("reson");
        String Days = getIntent().getStringExtra("Days");

//        String imagepath = getIntent().getStringExtra("imagepath");
//        if(imagepath.equalsIgnoreCase("")){
//            imageone.setVisibility(View.GONE);
//            prgr.dismiss();
//        }else{
//            Glide.with(this).load(imagepath).into(imageone);
//            prgr.dismiss();
//        }

        username.setText(UserName);
        if(type.equalsIgnoreCase("Half day")){
            holidaytype.setText("Half day");
            dateone.setText(fdate);
            fromdatetv.setText("Date");
            vieww.setVisibility(View.GONE);
            todatelv.setVisibility(View.GONE);
            noofdayLV.setVisibility(View.GONE);
        }else{
            holidaytype.setText("Full day");
            dateone.setText(fdate);
            tilldate.setText(tdate);
            fromdatetv.setText("From date");
            todatetv.setText("To date");
            NoOfDays.setText(Days);
        }
        reason.setText(reson);
        nametxt.setText("Leave approval detail");


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        approvebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeaveAppModel model = new LeaveAppModel();
                model.setAID(UserId);
                approveleave(id,model);
            }
        });

        disapprovebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeaveAppModel model = new LeaveAppModel();
                model.setAID(UserId);
                disapprovefun(id,model);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LeaveApprovalDetail.this,LeaveApprovalActivity.class));
        finish();
    }

    public void approveleave(String id, LeaveAppModel model) {
        prgr.show();
        mService = ApiClient.getClient().create(ApiServices.class);
        Call<ResponseBody> userCall = mService.approveleave(id,model);
        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody user = response.body();
                try {
                    String data = user.string();
                    JSONObject responsejobj = new JSONObject(data);
                    String stauts = responsejobj.getString("Status_Code");
                    String message = responsejobj.getString("Message");

                    if(stauts.equalsIgnoreCase("200")){
                        prgr.dismiss();
                        Toast.makeText(LeaveApprovalDetail.this,"Approved Successfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LeaveApprovalDetail.this,LeaveApprovalActivity.class));
                        finish();

                    }else{
                        prgr.dismiss();
                        //     Toast.makeText(LeaveApprovalDetail.this, message, Toast.LENGTH_SHORT).show();
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

    public void disapprovefun(String id,LeaveAppModel model) {
        prgr.show();
        mService = ApiClient.getClient().create(ApiServices.class);
        Call<ResponseBody> userCall = mService.disapprove(id,model);
        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody user = response.body();
                try {
                    String data = user.string();
                    JSONObject responsejobj = new JSONObject(data);
                    String stauts = responsejobj.getString("Status_Code");
                    String message = responsejobj.getString("Message");

                    if(stauts.equalsIgnoreCase("200")){
                        prgr.dismiss();
                        Toast.makeText(LeaveApprovalDetail.this,"Disapproved Successfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LeaveApprovalDetail.this,LeaveApprovalActivity.class));
                        finish();
                    }else{
                        prgr.dismiss();
                        //Toast.makeText(LeaveApprovalDetail.this, message, Toast.LENGTH_SHORT).show();
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

    private void init() {
        username = findViewById(R.id.username);
        holidaytype = findViewById(R.id.holidaytype);
        reason = findViewById(R.id.reason);
        dateone = findViewById(R.id.dateone);
        tilldate = findViewById(R.id.tilldate);
        fromdatetv = findViewById(R.id.fromdatetv);
        todatetv = findViewById(R.id.todatetv);
        todatelv = findViewById(R.id.todatelv);
        noofdayLV=findViewById(R.id.noofdayLV);
        NoOfDays=findViewById(R.id.txt_NoOfDays);
        imageone = findViewById(R.id.imageone);
        backbtn = findViewById(R.id.backbtn);
        nametxt = findViewById(R.id.nametxt);
        vieww = findViewById(R.id.vieww);
        approvebtn = findViewById(R.id.approvebtn);
        disapprovebtn = findViewById(R.id.disapprovebtn);
    }

}