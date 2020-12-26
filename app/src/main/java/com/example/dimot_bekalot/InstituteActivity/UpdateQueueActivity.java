package com.example.dimot_bekalot.InstituteActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.dataObjects.UpdatesAndAddsQueues;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateQueueActivity extends AppCompatActivity implements View.OnClickListener{
    TextView IDInput, dateInput, timeInput, typeInput;
    Button updateClientToQueue;

    private static final String Queues = "Queues_institute";
    private FirebaseDatabase dataBase;
    private DatabaseReference dbRef_queue_institute;
    private DatabaseReference dbRef;
    private String institute_id, queue, date, typeOfTreatment, theTime;

    private int STOP_RUN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_queue);

        Intent intent = getIntent();
        institute_id = intent.getExtras().getString("instituteID");
        typeOfTreatment = intent.getExtras().getString("type");
        date = intent.getExtras().getString("date");
        queue = intent.getExtras().getString("queue");

        IDInput = (TextView) findViewById(R.id.id_input);
        dateInput = (TextView) findViewById(R.id.date_input);
        timeInput = (TextView) findViewById(R.id.hour_input);
        typeInput = (TextView) findViewById(R.id.update_chooseTreatmentType);
        updateClientToQueue = (Button) findViewById(R.id.update);

        typeInput.setText(typeOfTreatment);

        String dateOfQueue[] = queue.split("\n");
        theTime = dateOfQueue[0];
        StringBuilder timeNew = new StringBuilder(theTime);
        timeNew.insert(2,":");
        timeInput.setText(timeNew.toString());

        StringBuilder dateNewName = new StringBuilder(date);
        dateNewName.insert(2, "/");
        dateNewName.insert(5, "/20");

        dateInput.setText(dateNewName.toString());

        dataBase = FirebaseDatabase.getInstance();
        dbRef = dataBase.getReference();
        dbRef_queue_institute = dataBase.getReference(Queues);

        updateClientToQueue.setOnClickListener(this);
    } // onCreate

    @Override
    public void onClick(View v) {
        if (updateClientToQueue == v) {
            String idOfPatient = IDInput.getText().toString().trim();
            StringBuilder id_patient_input = new StringBuilder(idOfPatient);
            if(!idOfPatient.equals("TBD")){
                id_patient_input.insert(0, "p:");
            }

            dbRef_queue_institute.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (STOP_RUN == 0) {
                        UpdatesAndAddsQueues up = new UpdatesAndAddsQueues(institute_id, id_patient_input.toString(), date, theTime, typeOfTreatment);
                        up.update();
                        STOP_RUN = 1;
                        goBackToCalendar();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }

            }); // addValueEventListener

        }
        this.onStop();
    }

    private void goBackToCalendar(){

        Intent goBack_intent = new Intent(this, WatchingQueueActivity.class);
        goBack_intent.putExtra("instituteID", institute_id);
        startActivity(goBack_intent);
    }
}
