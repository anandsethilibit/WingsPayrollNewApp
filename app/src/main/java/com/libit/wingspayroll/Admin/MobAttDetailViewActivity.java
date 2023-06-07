package com.libit.wingspayroll.Admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.libit.wingspayroll.R;


public class MobAttDetailViewActivity extends AppCompatActivity {
    ImageView backbtn;
    TextView nametxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mob_att_detail_view);

        nametxt = findViewById(R.id.nametxt);
        backbtn = findViewById(R.id.backbtn);
        nametxt.setText("Detail View");
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}