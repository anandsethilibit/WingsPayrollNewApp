package com.libit.wingspayroll.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.libit.wingspayroll.Network.ApiClient;
import com.libit.wingspayroll.Network.ApiServices;
import com.libit.wingspayroll.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MobAttDetailViewActivity extends AppCompatActivity {
    ImageView backbtn;
    TextView nametxt;
    TextView Name,Date,InTime,OutTime,Address;
    CircleImageView AttImage;
    Button btnApprove,btnDisapprove;
    ApiServices mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mob_att_detail_view);


        Name = findViewById(R.id.txt_Name);
        Date = findViewById(R.id.txt_Date);
        InTime = findViewById(R.id.txt_InTime);
        OutTime =findViewById(R.id.txt_OutTime);
        Address = findViewById(R.id.txt_Address);
        AttImage =findViewById(R.id.img_AttImage);
        btnApprove =findViewById(R.id.btn_Approve);
        btnDisapprove =findViewById(R.id.btn_Disapprove);
        nametxt = findViewById(R.id.nametxt);
        backbtn = findViewById(R.id.backbtn);
        nametxt.setText("Attendance Detail View");
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        final Integer MID = Integer.valueOf(getIntent().getStringExtra("MID"));
        String MEmpName = getIntent().getStringExtra("MEmpName");
        String MDate = getIntent().getStringExtra("MDate");
        String MInTime = getIntent().getStringExtra("MInTime");
        String MOutTime = getIntent().getStringExtra("MOutTime");
        String MAddress = getIntent().getStringExtra("MAddress");
        String MImage = getIntent().getStringExtra("MImage");

        Name.setText(MEmpName);
        Date.setText(MDate);
        InTime.setText(MInTime);
        OutTime.setText(MOutTime);
        Address.setText(MAddress);

        Glide.with(getApplicationContext())
                .load(MImage)
                .placeholder(R.drawable.demoimage)
                .error(R.drawable.demoimage)
                .fitCenter()
                .into(AttImage);


        AttImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MobAttDetailViewActivity.this, MobileAtteImageViewActivity.class);
                intent.putExtra("Image", MImage);
                startActivity(intent);
            }
        });

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = "Approved";
                MobAttendanceStatus(MID,status);
            }
        });

        btnDisapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = "DisApproved";
                MobAttendanceStatus(MID,status);
            }
        });
    }

    private void MobAttendanceStatus(Integer id,String status){
        mService= ApiClient.getClient().create(ApiServices.class);
        Call<ResponseBody> userCall = mService.MAttendanceStatus(id,status);
        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody user = response.body();
                try {
                    String data = user.string();
                    JSONObject responsejobj = new JSONObject(data);
                    String status = responsejobj.getString("Status_Code");
                    String Message = responsejobj.getString("Message");

                    if (status.equalsIgnoreCase("200")) {
                        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(MobAttDetailViewActivity.this,MobileAttendanceActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();
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