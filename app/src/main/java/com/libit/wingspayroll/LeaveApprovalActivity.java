package com.libit.wingspayroll;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.libit.wingspayroll.Adapter.LeaveApprovalAdapter;
import com.libit.wingspayroll.Adapter.LeaveListAdapter;
import com.libit.wingspayroll.Model.LeaveApprovalModel;
import com.libit.wingspayroll.Model.LeaveListModel;
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

public class LeaveApprovalActivity extends AppCompatActivity {
    ImageView backbtn;
    TextView nametxt;
    RecyclerView recyclerview,recyclerviewApprovel;
    ApiServices mService;
    List<LeaveApprovalModel> services;
    List<LeaveListModel> services2;
    LeaveApprovalAdapter mAdapter;
    LeaveListAdapter mAdapter2;
    ProgressDialog prgr;
    LinearLayout nodatafound;
    Button gobackbtn;
    Button approved,pending;
    LinearLayout layout_pending,layout_Approve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_approval);
        services = new ArrayList<>();
        services2 = new ArrayList<>();
        prgr = new ProgressDialog(LeaveApprovalActivity.this);
        prgr.setTitle("Get Leave");
        prgr.setMessage("Loading....");
        prgr.setCancelable(false);
        prgr.show();
        gobackbtn = findViewById(R.id.gobackbtn);
        nodatafound = findViewById(R.id.nodatafound);
        recyclerview = findViewById(R.id.recyclerview);
        approved=findViewById(R.id.btn_approved);
        pending=findViewById(R.id.btn_pending);
        recyclerviewApprovel=findViewById(R.id.recyclerviewApprovel);
        layout_pending=findViewById(R.id.layout_pending);
        layout_Approve=findViewById(R.id.layout_Approve);
        backbtn = findViewById(R.id.backbtn);
        nametxt = findViewById(R.id.nametxt);
        nametxt.setText("Leave approval");
        recyclerviewApprovel.setVisibility(View.GONE);
        layout_pending.setVisibility(View.VISIBLE);

        String UserId = StaticDataHelper.getStringFromPreferences(LeaveApprovalActivity.this, "EmpId");
        getallleave();

        approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerview.setVisibility(View.GONE);
                layout_pending.setVisibility(View.GONE);
                layout_Approve.setVisibility(View.VISIBLE);
                recyclerviewApprovel.setVisibility(View.VISIBLE);
                prgr.show();
                getapproved();
            }
        });

        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerviewApprovel.setVisibility(View.GONE);
                layout_Approve.setVisibility(View.GONE);
                layout_pending.setVisibility(View.VISIBLE);
                recyclerview.setVisibility(View.VISIBLE);
                prgr.show();
                getallleave();
            }
        });

        gobackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



    private void getallleave() {
        mService = ApiClient.getClient().create(ApiServices.class);
        Call<ResponseBody> userCall = mService.getallleave();
        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody user = response.body();
                try {
                    String data = user.string();
                    JSONObject responsejobj = new JSONObject(data);
                    String stauts = responsejobj.getString("Status_Code");
                    String message = responsejobj.getString("Message");
                    services.clear();

                    if (stauts.equalsIgnoreCase("200")) {

                        JSONArray coursearray = responsejobj.getJSONArray("Data");
                        for (int i = 0; i < coursearray.length(); i++) {
                            JSONObject dataobj = coursearray.getJSONObject(i);
//                            String LeaveRequestId = dataobj.getString("LeaveRequestId");
                            String LeaveRequestId = dataobj.getString("ReqID");
                            String fdate = dataobj.getString("FDate");
                            String tdate = dataobj.getString("TDate");
                            String reason = dataobj.getString("Reason");
                            String UserName = dataobj.getString("Username");
                            String LeaveType = dataobj.getString("LeaveType");
                            String Status = dataobj.getString("Status");
                            String RequestDate = dataobj.getString("RequestDate");
                            String Days = dataobj.getString("Days");
                            String LeaveRequestType = dataobj.getString("LeaveRequestType");

                            //String selectdate = dataobj.getString("selectdate");
                            //String imagepath = dataobj.getString("imagepath");

                            LeaveApprovalModel model = new LeaveApprovalModel();
                            model.setID(LeaveRequestId);
                            model.setUserName(UserName);
                            model.setType(LeaveRequestType);
                            model.setEStatus(Status);
                            model.setFdate(fdate);
                            model.setTdate(tdate);
                            model.setReason(reason);
                            model.setRequestDate(RequestDate);
                            model.setDays(Days);
                            services.add(model);

                            recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            mAdapter = new LeaveApprovalAdapter(LeaveApprovalActivity.this,services);
                            recyclerview.setAdapter(mAdapter);
                            prgr.dismiss();
                        }
                    } else {
                        prgr.dismiss();
                        //nodatafound.setVisibility(View.VISIBLE);
                         Toast.makeText(LeaveApprovalActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    prgr.dismiss();
                    e.printStackTrace();
                } catch (IOException e) {
                    prgr.dismiss();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


            }
        });
    }

    private void getapproved() {
        mService = ApiClient.getClient().create(ApiServices.class);
        Call<ResponseBody> userCall = mService.getAppLeave();
        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody user = response.body();
                try {
                    String data = user.string();
                    JSONObject responsejobj = new JSONObject(data);
                    String stauts = responsejobj.getString("Status_Code");
                    String message = responsejobj.getString("Message");
                    services2.clear();

                    if (stauts.equalsIgnoreCase("200")) {

                        JSONArray coursearray = responsejobj.getJSONArray("Data");
                        for (int i = 0; i < coursearray.length(); i++) {
                            JSONObject dataobj = coursearray.getJSONObject(i);

                            String UserName = dataobj.getString("Username");
                            String fdate = dataobj.getString("FDate");
                            String tdate = dataobj.getString("TDate");
                            String reason = dataobj.getString("Reason");
                            String LeaveType = dataobj.getString("LeaveType");
                            String Status = dataobj.getString("Status");
                            String RequestDate = dataobj.getString("RequestDate");
                            String AName = dataobj.getString("AName");
                            String ADate = dataobj.getString("ADate");
                            String Days = dataobj.getString("Days");
                            String LeaveRequestId = dataobj.getString("ReqID");
                            String LeaveRequestType = dataobj.getString("LeaveRequestType");


                            //String selectdate = dataobj.getString("selectdate");
                            //String imagepath = dataobj.getString("imagepath");

                            LeaveListModel model = new LeaveListModel();
                            model.setID(LeaveRequestId);
                            model.setUserName(UserName);
                            model.setType(LeaveRequestType);
                            model.setEStatus(Status);
                            model.setFdate(fdate);
                            model.setTdate(tdate);
                            model.setReason(reason);
                            model.setRequestDate(RequestDate);
                            model.setAName(AName);
                            model.setADate(ADate);
                            model.setDays(Days);
                            services2.add(model);

                            recyclerviewApprovel.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            mAdapter2 = new LeaveListAdapter(LeaveApprovalActivity.this,services2);
                            recyclerviewApprovel.setAdapter(mAdapter2);
                            prgr.dismiss();
                        }
                    } else {
                        prgr.dismiss();
                        //nodatafound.setVisibility(View.VISIBLE);
                        Toast.makeText(LeaveApprovalActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    prgr.dismiss();
                    e.printStackTrace();
                } catch (IOException e) {
                    prgr.dismiss();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


            }
        });
    }

//    public void approveleave(String id,) {
//        mService = ApiClient.getClient().create(ApiInterface.class);
//        Call<ResponseBody> userCall = mService.approveleave(id);
//        userCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                ResponseBody user = response.body();
//
//                try {
//                    String data = user.string();
//
//                    JSONObject responsejobj = new JSONObject(data);
//                    String stauts = responsejobj.getString("Status_Code");
//                    String message = responsejobj.getString("Message");
//
//                    if(stauts.equalsIgnoreCase("200")){
//                        Toast.makeText(LeaveApprovalActivity.this,"Approved Successfully",Toast.LENGTH_SHORT).show();
//                        getallleave();
//
//                    }else{
//                        Toast.makeText(LeaveApprovalActivity.this, message, Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//
//            }
//        });
//
//    }

}
