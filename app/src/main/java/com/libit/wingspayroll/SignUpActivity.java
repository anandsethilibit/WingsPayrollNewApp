package com.libit.wingspayroll;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    EditText CompanyName,ContactPerson,mobilenumber,EmailId;
    Spinner spinounitname;
    String unitname;
    String[] UnitNamelist ={"Select UnitName","Dinesh", "Dinesh", "Dinesh","Dangi"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        CompanyName=findViewById(R.id.txt_CompanyName);
        ContactPerson=findViewById(R.id.txt_ContactPerson);
        mobilenumber=findViewById(R.id.txt_mobilenumber);
        EmailId=findViewById(R.id.txt_EmailId);
        spinounitname=findViewById(R.id.spino_unitName);

        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, UnitNamelist);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinounitname.setAdapter(aa);
        spinounitname.setOnItemSelectedListener(new UnitSpinnerClass());
    }

    class UnitSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            unitname = UnitNamelist[position];
            //Toast.makeText(v.getContext(), "Your choose :" + UnitNamelist[position],Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

}