package com.libit.wingspayroll;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.libit.wingspayroll.Adapter.RequestDetailAdapter;
import com.libit.wingspayroll.Model.RequestDetailModel;
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

public class RequestDetailActivity extends AppCompatActivity {
    ImageView backbtn;
    ApiServices mService;
    List<RequestDetailModel> services;
    RecyclerView requestdetailRv;
    RequestDetailAdapter mAdapter;
    TextView nametxt;
    LinearLayout nodatafound,topbar;
    Button gobackbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);

        services =new ArrayList<>();
        String id = StaticDataHelper.getStringFromPreferences(getApplicationContext(),"EmpId");
        getmyleavereq(id);

        gobackbtn = findViewById(R.id.gobackbtn);
        topbar = findViewById(R.id.topbar);
        nodatafound = findViewById(R.id.nodatafound);
        nametxt = findViewById(R.id.nametxt);
        nametxt.setText("Request leave detail");
        backbtn = findViewById(R.id.backbtn);
        requestdetailRv = findViewById(R.id.requestdetailRv);


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        gobackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });
    }

    public void getmyleavereq(String userId) {
        mService = ApiClient.getClient().create(ApiServices.class);
        Call<ResponseBody> userCall = mService.getmyleaverequest(userId);
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
                        JSONArray coursearray = responsejobj.getJSONArray("Data");
                        for (int i = 0; i < coursearray.length(); i++) {
                            JSONObject dataobj = coursearray.getJSONObject(i);

                            String Type = dataobj.getString("LeaveRequestType");
                            String FDate = dataobj.getString("FDate");
                            String TDate = dataobj.getString("TDate");
                            String EStatus = dataobj.getString("Status");
                            String RequestDate = dataobj.getString("RequestDate");
                            String AllDate = FDate +" to "+ TDate;

                            RequestDetailModel model= new RequestDetailModel();
                            model.setType(Type);
                            model.setDate(AllDate);
                            model.setEStatus(EStatus);
                            services.add(model);

                            requestdetailRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            mAdapter = new RequestDetailAdapter(RequestDetailActivity.this, services);
                            requestdetailRv.setAdapter(mAdapter);
                        }
                    }else{
                        nodatafound.setVisibility(View.VISIBLE);
                        topbar.setVisibility(View.GONE);
                        requestdetailRv.setVisibility(View.GONE);
                        // Toast.makeText(RequestDetail.this, message, Toast.LENGTH_SHORT).show();
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
}