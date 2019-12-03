package com.project.usthack.deliverytracking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class emergency extends AppCompatActivity {
    private RadioGroup radioGroup;
    Button submit, clear;
    EditText desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        submit = (Button)findViewById(R.id.submit);
        radioGroup = (RadioGroup)findViewById(R.id.groupradio);

        // Uncheck or reset the radio buttons initially
        radioGroup.clearCheck();


        radioGroup.setOnCheckedChangeListener(
                new RadioGroup
                        .OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group,int checkedId){
                        RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
                    }
                });
        desc = (EditText)findViewById(R.id.reason);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("hi","hello");
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton)radioGroup.findViewById(selectedId);

                String str = (String)radioButton.getText();
                String str1 = desc.getText().toString();
                String sent = "Hi, your driver with details "+""+"has "+ str+"issues"+"\n"+"THe following is " +
                        "the description"+"\n"+str1;
                Toast.makeText(getApplicationContext(),sent , Toast.LENGTH_LONG).show();
                SmsManager sms=SmsManager.getDefault();
                String no = "9008327935";
                sms.sendTextMessage(no, null,sent, null,null);
            }
        });

    }
}