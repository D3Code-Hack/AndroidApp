package com.project.usthack.deliverytracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private LinearLayout mLayout;
    private EditText mEditText;
    private EditText name1;
    private EditText contact1;
    private EditText latitude1;
    private EditText longitude1;

    private String name;
    private String contact;
    private String latitude;
    private String longitude;

    private EditText mEditText1;
    private Button mButton;
    private Button submit;
    String path="";
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mLayout = (LinearLayout) findViewById(R.id.linearLayout);
        mEditText = (EditText) findViewById(R.id.editText);
        latitude1 = (EditText) findViewById(R.id.latitude);
        longitude1 = (EditText) findViewById(R.id.longitude);

        name1 = (EditText) findViewById(R.id.name);
        contact1 = (EditText) findViewById(R.id.contact);

        mEditText1 = (EditText) findViewById(R.id.editText1);
        mButton = (Button) findViewById(R.id.button);
        submit = (Button) findViewById(R.id.submit);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout.addView(createNewTextView(mEditText.getText().toString(),mEditText1.getText().toString()));
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();

                path = "Enterprise/"+uid+"/Name";
                ref= FirebaseDatabase.getInstance().getReference(path);
                name =name1.getText().toString();
                ref.setValue(name);

                path = "Enterprise/"+uid+"/Contact";
                ref= FirebaseDatabase.getInstance().getReference(path);
                contact =contact1.getText().toString();
                ref.setValue(contact);

                path = "Enterprise/"+uid+"/Latitude";
                ref= FirebaseDatabase.getInstance().getReference(path);
                latitude =latitude1.getText().toString();
                ref.setValue(latitude);

                path = "Enterprise/"+uid+"/Longitude";
                ref= FirebaseDatabase.getInstance().getReference(path);
                longitude =longitude1.getText().toString();
                ref.setValue(longitude);

                Intent intent = new Intent(getApplicationContext(), Admin.class);
                intent.putExtra("lat",latitude);
                intent.putExtra("log",longitude);
                startActivity(intent);
                finish();
            }
        });
        TextView textView = new TextView(this);
        textView.setText("Name");

    }


    private TextView createNewTextView(String text, String contact) {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(this);
        textView.setLayoutParams(lparams);
        textView.setText("Name: " + text + ", Contact: "+contact);
        return textView;
    }
}
