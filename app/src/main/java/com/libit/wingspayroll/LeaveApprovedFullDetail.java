package com.libit.wingspayroll;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.libit.wingspayroll.Network.ApiServices;
import com.libit.wingspayroll.Network.StaticDataHelper;

public class LeaveApprovedFullDetail extends AppCompatActivity {

    TextView username,holidaytype,reason,dateone,tilldate,Status,NoOfDays,Approvedate,ApproveBy,Appbytv,
             todatetv,ReqDate,fromdatetv,nametxt,Appdatetv;
    LinearLayout todatelv,noofdayLV;
    ImageView imageone,backbtn;
    ProgressDialog prgr;
    View vieww;
    ApiServices mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_approved_full_detail);

        init();

        prgr = new ProgressDialog(LeaveApprovedFullDetail.this);
        prgr.setMessage("Loading...");
//      prgr.show();

        String UserId = StaticDataHelper.getStringFromPreferences(LeaveApprovedFullDetail.this, "EmpId");

        final String id = getIntent().getStringExtra("ID");
        String UserName = getIntent().getStringExtra("UserName");
        String type = getIntent().getStringExtra("Type");
        String EStatus = getIntent().getStringExtra("EStatus");
        String RequestDate = getIntent().getStringExtra("RequestDate");
        String fdate = getIntent().getStringExtra("fdate");
        String tdate = getIntent().getStringExtra("tdate");
        String reson = getIntent().getStringExtra("reason");
        String AName = getIntent().getStringExtra("AName");
        String ADate = getIntent().getStringExtra("ADate");
        String Days = getIntent().getStringExtra("Days");

//      String imagepath = getIntent().getStringExtra("imagepath");
//        if(imagepath.equalsIgnoreCase("")){
//            imageone.setVisibility(View.GONE);
//            prgr.dismiss();
//        }else{
//            Glide.with(this).load(imagepath).into(imageone);
//            prgr.dismiss();
//        }

        username.setText(UserName);
        if (type.equalsIgnoreCase("Half day")) {
            holidaytype.setText("Half day");
            dateone.setText(fdate);
            fromdatetv.setText("Date");
            vieww.setVisibility(View.GONE);
            todatelv.setVisibility(View.GONE);
            noofdayLV.setVisibility(View.GONE);
        } else {
            holidaytype.setText("Full day");
            dateone.setText(fdate);
            tilldate.setText(tdate);
            fromdatetv.setText("From date");
            todatetv.setText("To date");
            NoOfDays.setText(Days);
        }

        if (EStatus.equalsIgnoreCase("Approved")) {
            Appdatetv.setText("Approved Date");
            Approvedate.setText(ADate);
            Appbytv.setText("Approved By");
            ApproveBy.setText(AName);
        } else {
            Appdatetv.setText("Disapproved Date");
            Approvedate.setText(ADate);
            Appbytv.setText("Disapproved By");
            ApproveBy.setText(AName);
        }

        reason.setText(reson);
        ReqDate.setText(RequestDate);
        Status.setText(EStatus);
        nametxt.setText("Leave approval Full Detail");

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void onBackPressed() {
        startActivity(new Intent(LeaveApprovedFullDetail.this,LeaveApprovalActivity.class));
        finish();
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
        Appdatetv=findViewById(R.id.Appdatetv);
        noofdayLV= findViewById(R.id.noofdayLV);
        imageone = findViewById(R.id.imageone);
        backbtn = findViewById(R.id.backbtn);
        nametxt = findViewById(R.id.nametxt);
        Status=findViewById(R.id.txt_Status);
        NoOfDays=findViewById(R.id.txt_NoOfDays);
        Approvedate=findViewById(R.id.Approvedate);
        ApproveBy=findViewById(R.id.ApproveBy);
        Appbytv=findViewById(R.id.Appbytv);
        ReqDate=findViewById(R.id.ReqDate);
        vieww = findViewById(R.id.vieww);
    }
}