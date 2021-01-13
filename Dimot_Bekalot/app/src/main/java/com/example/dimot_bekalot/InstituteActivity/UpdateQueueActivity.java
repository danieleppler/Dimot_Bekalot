package com.example.dimot_bekalot.InstituteActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.dataObjects.UpdatesAndAddsQueues;
import com.example.dimot_bekalot.entryActivities.Main_Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UpdateQueueActivity extends AppCompatActivity implements View.OnClickListener {
    TextView ID_input, date_input, time_input, type_input;
    Button updateClientToQueue;

    private ImageButton logOut;
    Context context = this;

    int BLUE = R.color.blue; // color of button

    private static final String INSTITUTE = "Institutes";
    private FirebaseDatabase dataBase;
    private DatabaseReference ref_institute;
    private String institute_id, queue, date, type_treatment, hour_queue;
    /* for update_queue: */
    private String city, institute_name;


    private int STOP_RUN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_queue);

        Intent intent = getIntent();
        institute_id = intent.getExtras().getString("instituteID");
        type_treatment = intent.getExtras().getString("type");
        date = intent.getExtras().getString("date");
        queue = intent.getExtras().getString("queue");

        dataBase = FirebaseDatabase.getInstance();
        ref_institute = dataBase.getReference(INSTITUTE);

        ref_institute.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                city = snapshot.child(institute_id).child("address").child("city_Name").getValue().toString();
                institute_name = snapshot.child(institute_id).child("institute_name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        ID_input = (TextView) findViewById(R.id.id_input);
        date_input = (TextView) findViewById(R.id.date_input);
        time_input = (TextView) findViewById(R.id.hour_input);
        type_input = (TextView) findViewById(R.id.update_chooseTreatmentType);
        updateClientToQueue = (Button) findViewById(R.id.update);

        String dateOfQueue[] = queue.split("\n");
        hour_queue = dateOfQueue[0]; // only number of time, without ":"

        StringBuilder timeNew = new StringBuilder(hour_queue);
        timeNew.insert(2, ":");
        time_input.setText(timeNew.toString());

        StringBuilder dateNewName = new StringBuilder(date);
        dateNewName.insert(2, "/");
        dateNewName.insert(5, "/20");

        type_input.setText(type_treatment);

        date_input.setText(dateNewName.toString());

        updateClientToQueue.setOnClickListener(this);
        updateClientToQueue.setBackgroundColor(getResources().getColor(BLUE));

        logOut = (ImageButton) findViewById(R.id.logOutButton);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logOutIntent = new Intent(context, Main_Activity.class);
                startActivity(logOutIntent);
            }
        });
    } // onCreate

    @Override
    public void onClick(View v) {
        if (updateClientToQueue == v) {
            String patient_id = ID_input.getText().toString().trim();

            if (STOP_RUN == 0) {
                if (patient_id.equals("TBD")) {
                    toWaitingList(date, hour_queue, patient_id);
                    STOP_RUN = 1;
                    goBackToCalendar();
                }
                else{
                    Toast.makeText(context, "Write TBD", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void toWaitingList(String theDate, String theTime, String id_patient_input) {
        UpdatesAndAddsQueues update = new UpdatesAndAddsQueues(institute_id, id_patient_input, theDate, theTime, type_treatment, city, institute_name);
        update.changeWaitList();
    }

    private void goBackToCalendar() {
        Intent goBack_intent = new Intent(this, WatchingQueueActivity.class);
        goBack_intent.putExtra("instituteID", institute_id);
        startActivity(goBack_intent);
    }
}
