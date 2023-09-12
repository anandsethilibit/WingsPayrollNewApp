package com.libit.wingspayroll.Admin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.libit.wingspayroll.MainActivity;
import com.libit.wingspayroll.Network.ApiClient;
import com.libit.wingspayroll.Network.ApiServices;
import com.libit.wingspayroll.Network.StaticDataHelper;
import com.libit.wingspayroll.R;
import com.libit.wingspayroll.SignUpActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminLoginActivity extends AppCompatActivity {
    TextView signup;
    Button loginButton;
    ProgressDialog loading;
    ProgressBar progress_bar;
    TextInputEditText Username, userpassword;
    Switch sw;
    String who = "Admin";
    ApiServices mService;
    //TextView gradienttv;
    //TextView inputText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        loading = new ProgressDialog(this);
        Username = findViewById(R.id.username);
        userpassword=findViewById(R.id.userpassword);
        loginButton = findViewById(R.id.loginButton);
        signup=findViewById(R.id.txt_signup);


//        inputText = findViewById(R.id.inputText);
//     +
//     gradienttv = findViewById(R.id.gradienttv);
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
                if (TextUtils.isEmpty(Username.getText().toString().trim())||TextUtils.isEmpty(userpassword.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Please Enter Username", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                } else {
                    String username = Username.getText().toString();
                    String Password = userpassword.getText().toString();
                    Adminlogin(username,Password);

//                    StaticDataHelper.setStringInPreferences(AdminLoginActivity.this, "Name", "Abcd");
//                    StaticDataHelper.setStringInPreferences(AdminLoginActivity.this, "Usertype", "Admin");
//                    startActivity(new Intent(AdminLoginActivity.this, MainActivity.class));
//                    finish();
//                    loading.dismiss();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLoginActivity.this, SignUpActivity.class));
                finish();
                loading.dismiss();
            }
        });
    }


    private void Adminlogin(String username,String password) {
        mService = ApiClient.getClient().create(ApiServices.class);
        Call<ResponseBody> userCall = mService.loginadmin(username,password);
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
                        JSONObject jobj = responsejobj.getJSONObject("user");
                        String EmpId = jobj.getString("UserId");
                        String DisplayName = jobj.getString("DisplayName");
                        String UserLogId = jobj.getString("UserLogId");
                        String UserStatus = jobj.getString("UserStatus");

                        if(UserStatus.equals("Active")) {
                            StaticDataHelper.setStringInPreferences(AdminLoginActivity.this, "EmpId", EmpId);
                            StaticDataHelper.setStringInPreferences(AdminLoginActivity.this, "Name", DisplayName);
                            StaticDataHelper.setStringInPreferences(AdminLoginActivity.this, "UserStatus", UserStatus);
                            StaticDataHelper.setStringInPreferences(AdminLoginActivity.this, "UserLogId", UserLogId);
                            StaticDataHelper.setStringInPreferences(AdminLoginActivity.this, "Usertype", "Admin");
                            StaticDataHelper.setBooleanInPreferences(getApplicationContext(), "isAdminlogin", true);
                            startActivity(new Intent(AdminLoginActivity.this, MainActivity.class));
                            finish();
                            loading.dismiss();
                        }else {
                            Toast.makeText(AdminLoginActivity.this, "User DeActive", Toast.LENGTH_SHORT).show();
                        }
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
}
