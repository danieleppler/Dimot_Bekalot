package com.example.dimot_bekalot.InstituteActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dimot_bekalot.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateQueueActivity extends AppCompatActivity implements View.OnClickListener {
    TextView typeTreatment, IDInput, dateInput, timeInput;
    Button addClient;

    private FirebaseDatabase dataBase;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_queue);

        typeTreatment = (TextView)findViewById(R.id.treatment_type_input);
        IDInput = (TextView)findViewById(R.id.id_input);
        dateInput = (TextView)findViewById(R.id.date_input);
        timeInput = (TextView)findViewById(R.id.hour_input);
        addClient = (Button)findViewById(R.id.adding);

//        dataBase = FirebaseDatabase.getInstance();
//        dbRef = dataBase.
    }

    @Override
    public void onClick(View v) {
        if(addClient == v){
//            add in database a client in queue
        }
    }
}