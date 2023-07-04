package com.libit.wingspayroll;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.libit.wingspayroll.Admin.AdminLoginActivity;
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

public class LoginActivity extends AppCompatActivity {
    //FloatingActionButton loginButton;
    Button loginButton;
    ProgressDialog loading;
    ProgressBar progress_bar;
    TextInputEditText mobilenumber; // userpassword;
    Switch sw;
    String who = "Admin";
    ApiServices mService;
    //TextView gradienttv;
    //TextView inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loading = new ProgressDialog(this);
        mobilenumber = findViewById(R.id.username);
        loginButton = findViewById(R.id.loginButton);


//        inputText = findViewById(R.id.inputText);
//        gradienttv = findViewById(R.id.gradienttv);
//        gradienttv.setText("LIBIT SOLUTIONS".toUpperCase());
//        TextPaint paint = gradienttv.getPaint();
//        float width = paint.measureText("Future-focused: Building a Better Life");
//        Shader textShader = new LinearGradient(0, 0, width, gradienttv.getTextSize(),
//                new int[]{
//                        Color.parseColor("#F97C3C"),
//                        Color.parseColor("#FDB54E"),
//                        Color.parseColor("#64B678"),
//                        Color.parseColor("#478AEA"),
//                        Color.parseColor("#8446CC"),
//                }, null, Shader.TileMode.CLAMP);
//        gradienttv.getPaint().setShader(textShader);
//        gradienttv.startAnimation((Animation) AnimationUtils.loadAnimation(LoginActivity.this, R.anim.myanime));


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setTitle("Login Account");
                loading.setMessage("Please wait...");
                loading.show();
                if (TextUtils.isEmpty(mobilenumber.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }else if(mobilenumber.getText().toString().trim().length() < 10){
                    Toast.makeText(getApplicationContext(), "Please Enter 10 Digit Mobile Number", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                } else {
                      String mobileno = mobilenumber.getText().toString();
                      login(mobileno);
                }
            }
        });
    }



    private void login(String mobilenumber){
        mService=ApiClient.getClient().create(ApiServices.class);
        Call<ResponseBody>userCall = mService.loginuser(mobilenumber);
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
                        JSONObject jobj = responsejobj.getJSONObject("Login");

                        String EmpId = jobj.getString("EmpId");
                        String Code = jobj.getString("Code");
                        String Name = jobj.getString("Name");
                        String DepName = jobj.getString("DepName");
                        String DesName = jobj.getString("DesName");
                        String UnitName = jobj.getString("UnitName");

                        StaticDataHelper.setStringInPreferences(LoginActivity.this, "EmpId", EmpId);
                        StaticDataHelper.setStringInPreferences(LoginActivity.this, "Code", Code);
                        StaticDataHelper.setStringInPreferences(LoginActivity.this, "Name", Name);
                        StaticDataHelper.setStringInPreferences(LoginActivity.this, "DepName", DepName);
                        StaticDataHelper.setStringInPreferences(LoginActivity.this, "DesName", DesName);
                        StaticDataHelper.setStringInPreferences(LoginActivity.this, "UnitName", UnitName);
                        StaticDataHelper.setStringInPreferences(LoginActivity.this, "Usertype", "Employee");
                        StaticDataHelper.setBooleanInPreferences(getApplicationContext(), "islogin", true);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                        loading.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();
                        loading.dismiss();
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

//    private void login(String mobileno){
//        mService= ApiClient.getClient().create(ApiServices.class);
//        Call<ResponseBody> userCall = mService.loginuser(mobileno);
//        userCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                ResponseBody user = response.body();
//                try {
//                    String data = user.string();
//                    JSONObject responsejobj = new JSONObject(data);
//                    String status = responsejobj.getString("Status_Code");
//                    String Message = responsejobj.getString("Message");
//
//                    if (status.equalsIgnoreCase("200")) {
//                        JSONObject jobj = responsejobj.getJSONObject("Login");
//
//                          String EmpId = jobj.getString("EmpId");
////                          String Code = jobj.getString("Code");
//                          String Name = jobj.getString("Name");
//                          StaticDataHelper.setStringInPreferences(LoginActivity.this, "EmpIdd", EmpId);
//                          //StaticDataHelper.setStringInPreferences(LoginActivity.this, "Code", Code);
//                        StaticDataHelper.setStringInPreferences(LoginActivity.this, "Name", Name);
//                        StaticDataHelper.setStringInPreferences(LoginActivity.this, "Usertype", "Emp");
//                        StaticDataHelper.setBooleanInPreferences(getApplicationContext(), "islogin", true);
//                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                        finish();
//                        loading.dismiss();
//                    } else {
//                        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();
//                        loading.dismiss();
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


    private void login (String username, String pass, String type) {

        if (username.equals("admin")&& pass.equals("123")&&type.equals("User")) {
            StaticDataHelper.setStringInPreferences(LoginActivity.this,"who",who);
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(i);
            StaticDataHelper.setBooleanInPreferences(LoginActivity.this,"+",true);
            loading.dismiss();
        }
        if(username.equals("Employee")&& pass.equals("123")&&type.equals("Employee")) {
            StaticDataHelper.setStringInPreferences(LoginActivity.this,"who",who);
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(i);
            StaticDataHelper.setBooleanInPreferences(LoginActivity.this,"+",true);
            loading.dismiss();
        }
        else {
            Toast.makeText(LoginActivity.this, "fill data correctly", Toast.LENGTH_SHORT).show();
            loading.dismiss();
        }
    }
}
