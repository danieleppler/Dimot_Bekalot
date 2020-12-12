package com.example.dimot_bekalot.InstituteActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.ValueEventListener;


public class AddQueueActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Spinner spinner;
    TextView IDInput, dateInput, timeInput;
    Button addClientToQueue;
    String type;
    private static final String Queues = "Queues_institute";
    private FirebaseDatabase dataBase;
    private DatabaseReference dbRef_queue_institute;
    private DatabaseReference dbRef;
    private String institute_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_queue);

        Intent intent = getIntent();
        institute_id = intent.getExtras().getString("instituteID");

        /* <Spinner> */
        spinner = (Spinner) findViewById(R.id.chooseTreatmentType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this, R.array.treatment_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        /* </Spinner> */

        IDInput = (TextView) findViewById(R.id.id_input);
        dateInput = (TextView) findViewById(R.id.date_input);
        timeInput = (TextView) findViewById(R.id.hour_input);
        addClientToQueue = (Button) findViewById(R.id.adding);

        dataBase = FirebaseDatabase.getInstance();
        dbRef = dataBase.getReference();
        dbRef_queue_institute = dataBase.getReference(Queues);

        addClientToQueue.setOnClickListener(this);
    }

     @Override
     public void onClick(View v) {
        if (v == addClientToQueue) {
                    String id_patient_input = IDInput.getText().toString().trim();
                    String date_input = dateInput.getText().toString().trim();
                    String time_input = timeInput.getText().toString().trim();

                    String str_date[] = date_input.split("/", 3);
                    String theDate = str_date[0] + "" + str_date[1] + "" + str_date[2];

                    String str_time[] = time_input.split(":", 2);
                    String theTime = str_time[0] + "" + str_time[1];
                    Log.d("add queue", institute_id + " " + type + " " + theDate + " " + theTime);
                    dbRef_queue_institute.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            dbRef_queue_institute.child(institute_id).child("Treat_type").child(type)
                                    .child(theDate).child(theTime).getRoot();

                            if (snapshot.child(institute_id).child("Treat_type").child(type).child(theDate).exists()) {
                                // if don't exist yet=
                                if (snapshot.child(institute_id).child("Treat_type").child(type).child(theDate).child(theTime).exists()) {
                                    Toast.makeText(AddQueueActivity.this,
                                            "התור כבר תפוס לתאריך והשעה הנתונים", Toast.LENGTH_SHORT).show();
                                } else {
                                    dbRef_queue_institute.child(institute_id).child("Treat_type").child(type).child(theDate).setValue(theTime);
                                    dbRef_queue_institute.child(institute_id).child("Treat_type").child(type).child(theDate).child(theTime).setValue("Patient_id_attending", id_patient_input);

                                    Toast.makeText(AddQueueActivity.this,
                                            "התור נקבע בהצלחה!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                dbRef_queue_institute.child(institute_id).child("Treat_type").child(type).setValue(theDate);
                                dbRef_queue_institute.child(institute_id).child("Treat_type").child(type).child(theDate).setValue(theTime);
                                dbRef_queue_institute.child(institute_id).child("Treat_type").child(type).child(theDate).child(theTime).child("Patient_id_attending");
                                dbRef_queue_institute.child(institute_id).child("Treat_type").child(type).child(theDate).child(theTime).child("Patient_id_attending").setValue(id_patient_input);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }

                    }); // addValueEventListener

                }
        this.onStop();
            }
    // onCreate

    /* <Spinner> */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), type, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
    /* </Spinner> */

}