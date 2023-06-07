package com.libit.wingspayroll;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.libit.wingspayroll.Adapter.BirthdayAndAnniverAdapter;
import com.libit.wingspayroll.Model.BirthdayAndAnniversaryModel;
import com.libit.wingspayroll.Network.ApiClient;
import com.libit.wingspayroll.Network.ApiServices;

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

public class BirthdayAndAnniverActivity extends AppCompatActivity {
    ImageView backbtn;
    TextView nametxt;
    RecyclerView recyclerview;
    List<BirthdayAndAnniversaryModel> services;
    BirthdayAndAnniverAdapter mAdapter;
    ApiServices mService;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday_and_anniver);
        loading = new ProgressDialog(this);
        loading.setTitle("Get Data");
        loading.setMessage("Please wait...");
        loading.setCancelable(false);

        recyclerview=findViewById(R.id.recyclerBirth);
        nametxt = findViewById(R.id.nametxt);
        backbtn = findViewById(R.id.backbtn);
        nametxt.setText("Show Birthday/Anniversary");
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getbirthdayAnniverList();
        loading.show();
    }

    private void getbirthdayAnniverList() {
        mService = ApiClient.getClient().create(ApiServices.class);
        Call<ResponseBody> userCall = mService.getEmployeeBirthday();
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

                            String EmployeeName = dataobj.getString("EmpName");
                            String EmployeeContact = dataobj.getString("EmpContact");
                            String DateOfBirth = dataobj.getString("DOB");
                            // String Type = dataobj.getString("Type");


                            BirthdayAndAnniversaryModel model = new BirthdayAndAnniversaryModel();
                            model.setEmployeeName(EmployeeName);
                            model.setEmployeeContact(EmployeeContact);
                            model.setDateOfBirth(DateOfBirth);
                            model.setType("Birthday");
                            services.add(model);

                            recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            mAdapter = new BirthdayAndAnniverAdapter(BirthdayAndAnniverActivity.this,services);
                            recyclerview.setAdapter(mAdapter);
                            loading.dismiss();
                        }
                    } else {
                        loading.dismiss();
                        Toast.makeText(BirthdayAndAnniverActivity.this, message, Toast.LENGTH_SHORT).show();
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