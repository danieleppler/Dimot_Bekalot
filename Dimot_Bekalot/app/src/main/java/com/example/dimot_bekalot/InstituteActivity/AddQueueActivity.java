package com.example.dimot_bekalot.InstituteActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.dataObjects.MyDate;
import com.example.dimot_bekalot.dataObjects.TreatmentQueue;
import com.example.dimot_bekalot.dataObjects.Update_Queues;
import com.example.dimot_bekalot.dataObjects.UpdatesAndAddsQueues;
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

    private ImageButton logOut;
    Context context = this;

    private String institute_id;

    private int STOP_RUN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_queue);

        Intent intent = getIntent();
        institute_id = intent.getExtras().getString("instituteID");
//        institute_id = "i:123456781"; // Debuging
        /* <Spinner> */
        spinner = (Spinner) findViewById(R.id.chooseTreatmentType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (context, R.array.treatment_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        /* </Spinner> */

        IDInput = (TextView) findViewById(R.id.id_input);
        dateInput = (TextView) findViewById(R.id.date_input);
        timeInput = (TextView) findViewById(R.id.hour_input);
        addClientToQueue = (Button) findViewById(R.id.adding);

        dataBase = FirebaseDatabase.getInstance();
        dbRef_queue_institute = dataBase.getReference(Queues);

        addClientToQueue.setOnClickListener(this);

        logOut = (ImageButton) findViewById(R.id.logOutButton);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logOutIntent = new Intent(context, com.example.dimot_bekalot.entryActivities.Main_Activity.class);
                startActivity(logOutIntent);
            }
        });

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

            dbRef_queue_institute.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (STOP_RUN == 0) {
                        if (snapshot.child(institute_id).exists()) {

                            if(!snapshot.child(institute_id).child("Treat_type").exists()){
                                dbRef_queue_institute.child(institute_id).setValue("Treat_type");
                            }
                            if (snapshot.child(institute_id).child("Treat_type").child(type).exists()) {

                                if (snapshot.child(institute_id).child("Treat_type").child(type).child(theDate).exists()) {

                                    if (snapshot.child(institute_id).child("Treat_type").child(type).child(theDate).child(theTime).exists()) {
                                        Toast.makeText(AddQueueActivity.this,
                                                "התור כבר תפוס לתאריך והשעה הנתונים, ולכן הלקוח יעבור לרשימת ההמתנה", Toast.LENGTH_SHORT).show();
                                        toWaitingList(theDate, theTime, id_patient_input);
                                    } else { // child(time) not exist
                                        STOP_RUN = 1;
                                        add_DB_time(institute_id, theDate, theTime, id_patient_input);
                                        Toast.makeText(AddQueueActivity.this,
                                                "התור נקבע בהצלחה!", Toast.LENGTH_SHORT).show();
                                    }
                                } else { // child(date) not exist
                                    STOP_RUN = 1;
                                    add_DB_Date_time(institute_id, theDate, theTime, id_patient_input);
                                    Toast.makeText(AddQueueActivity.this,
                                            "התור נקבע בהצלחה!", Toast.LENGTH_SHORT).show();
                                }
                            } else { // child(type) not exist
                                STOP_RUN = 1;
                                add_DB_Type(institute_id, theDate, theTime, id_patient_input);
                                Toast.makeText(AddQueueActivity.this,
                                        "התור נקבע בהצלחה!", Toast.LENGTH_SHORT).show();
                            }
                        }else { // child(institute_id) not exist
                            STOP_RUN = 1;
                            add_DB_ID(institute_id, theDate, theTime, id_patient_input);
                            Toast.makeText(AddQueueActivity.this,
                                    "התור נקבע בהצלחה!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }

            }); // addValueEventListener

        }
    }

    private void toWaitingList(String theDate, String theTime, String id_patient_input) {
        dbRef_queue_institute.child(institute_id).child("Treat_type").child(type)
                .child(theDate).child(theTime).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Waiting_list").child("patient_1").getValue().equals("TBD")){
                    dbRef_queue_institute.child(institute_id).child("Treat_type").child(type)
                            .child(theDate).child(theTime).child("Waiting_list").child("patient_1").setValue(id_patient_input);
                }
                else if(snapshot.child("Waiting_list").child("patient_2").getValue().equals("TBD")){
                    dbRef_queue_institute.child(institute_id).child("Treat_type").child(type)
                            .child(theDate).child(theTime).child("Waiting_list").child("patient_2").setValue(id_patient_input);
                }
                else if(snapshot.child("Waiting_list").child("patient_3").getValue().equals("TBD")){
                    dbRef_queue_institute.child(institute_id).child("Treat_type").child(type)
                            .child(theDate).child(theTime).child("Waiting_list").child("patient_3").setValue(id_patient_input);
                }
                else if(snapshot.child("Waiting_list").child("patient_4").getValue().equals("TBD")){
                    dbRef_queue_institute.child(institute_id).child("Treat_type").child(type)
                            .child(theDate).child(theTime).child("Waiting_list").child("patient_4").setValue(id_patient_input);
                }
                else if(snapshot.child("Waiting_list").child("patient_5").getValue().equals("TBD")){
                    dbRef_queue_institute.child(institute_id).child("Treat_type").child(type)
                            .child(theDate).child(theTime).child("Waiting_list").child("patient_5").setValue(id_patient_input);
                }
                else{
                    Toast.makeText(AddQueueActivity.this,
                            "רשימת ההמתנה מלאה, עמכם הסליחה", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }


    /* <Spinner> */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), type, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    /* </Spinner> */


    private void add_DB_ID(String institute_id, String theDate, String theTime, String id_patient_input){
        UpdatesAndAddsQueues a = new UpdatesAndAddsQueues(institute_id, id_patient_input, theDate, theTime, type);
        a.addID();
        goBackToNewAdd();
    }

    private void add_DB_Type(String institute_id, String theDate, String theTime, String id_patient_input){
        UpdatesAndAddsQueues a = new UpdatesAndAddsQueues(institute_id, id_patient_input, theDate, theTime, type);
        a.addType();
        goBackToNewAdd();
    }

    private void add_DB_Date_time(String institute_id, String theDate, String theTime, String id_patient_input) {
        UpdatesAndAddsQueues a = new UpdatesAndAddsQueues(institute_id, id_patient_input, theDate, theTime, type);
        a.addDate();
        goBackToNewAdd();
    }

    private void add_DB_time(String institute_id, String theDate, String theTime, String id_patient_input) {
        UpdatesAndAddsQueues a = new UpdatesAndAddsQueues(institute_id, id_patient_input, theDate, theTime, type);
        a.addTime();
        goBackToNewAdd();
    }

    private void goBackToNewAdd(){
        Intent goBackToNewAddIntent = new Intent();
        goBackToNewAddIntent.putExtra("instituteID", institute_id);
        startActivity(goBackToNewAddIntent);
    }
}