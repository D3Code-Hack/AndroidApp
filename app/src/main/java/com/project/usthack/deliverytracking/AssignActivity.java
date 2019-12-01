package com.project.usthack.deliverytracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class AssignActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;

    private String item1;
    private String item2;
    private String item3;
    private EditText item11;
    private EditText item21;
    private EditText item31;

    private Button submit;

    String name = "";
    String address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign);

        Spinner spinner1 = findViewById(R.id.spinner1);

        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("Rohan");
        arrayList1.add("Vanip");
        arrayList1.add("Shewanj");
        arrayList1.add("Wanr");
        arrayList1.add("Raun");
        arrayList1.add("Wease");
        arrayList1.add("Erdf");
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList1);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tutorialsName = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + tutorialsName, Toast.LENGTH_LONG).show();
                name = tutorialsName;

            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        Spinner spinner2 = findViewById(R.id.spinner2);
        ArrayList<String> arrayList2 = new ArrayList<>();
        arrayList2.add("Flamingo Inn");
        arrayList2.add("UST Parking");
        arrayList2.add("Infosys park");
        arrayList2.add("Veli Village");
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList2);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(arrayAdapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tutorialsName = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + tutorialsName, Toast.LENGTH_LONG).show();
                address = tutorialsName;

            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item11 = findViewById(R.id.item1value);
                item21 = findViewById(R.id.item2Value);
                item31 = findViewById(R.id.item3Value);

                item1 = item11.getText().toString();
                item2 = item21.getText().toString();
                item3 = item31.getText().toString();


                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("7250996657", null, name+address+item1+item2+item3, null, null);

            }
        });


//        recyclerView = findViewById(R.id.items);
//        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);

//        String[] myDataset =new String[]{"Pen","Pencils", "Colors"};
        // specify an adapter (see also next example)
//        mAdapter = new ItemAdapter(myDataset);
//        recyclerView.setAdapter(mAdapter);

    }
}
