package com.libit.wingspayroll;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.libit.wingspayroll.Admin.AdminLoginActivity;


public class Splash2Activity extends AppCompatActivity {
    Button btn_emyloyee,btn_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);

        btn_emyloyee=findViewById(R.id.btn_emyloyee);
        btn_admin=findViewById(R.id.btn_admin);

        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Splash2Activity.this, AdminLoginActivity.class);
                startActivity(intent);
//              Splash2Activity.this.finish();
            }
        });

        btn_emyloyee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Splash2Activity.this, LoginActivity.class);
                startActivity(intent);
//              Splash2Activity.this.finish();
            }
        });

    }
}