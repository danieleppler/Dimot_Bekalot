package com.example.dimot_bekalot.InstituteActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dimot_bekalot.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


public class UpdateQueueActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Spinner spinner;
    TextView IDInput, dateInput, timeInput;
    Button addClientToQueue;

    private static final String INSTITUTES = "Institutes";
    private FirebaseDatabase dataBase;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_queue);

        /* <Spinner> */
        spinner = (Spinner)findViewById(R.id.chooseTreatmentType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this, R.array.treatment_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        /* </Spinner> */

        IDInput = (TextView)findViewById(R.id.id_input);
        dateInput = (TextView)findViewById(R.id.date_input);
        timeInput = (TextView)findViewById(R.id.hour_input);
        addClientToQueue = (Button)findViewById(R.id.adding);

        dataBase = FirebaseDatabase.getInstance();
        dbRef = dataBase.getReference(INSTITUTES);


        addClientToQueue.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String id_institute = IDInput.getText().toString().trim();
                String date = dateInput.getText().toString().trim();
                String time = timeInput.getText().toString().trim();



            }
        });

    }

    @Override
    public void onClick(View v) {
        if(addClientToQueue == v){
//            add in database a client in queue
        }
    }

    /* <Spinner> */

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) { }


    /* </Spinner> */

}