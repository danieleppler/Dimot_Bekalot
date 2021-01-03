package com.example.dimot_bekalot.InstituteActivity;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.dataObjects.MyDate;
import com.example.dimot_bekalot.dataObjects.TreatmentQueue;
import com.example.dimot_bekalot.dataObjects.Update_Queues;
import com.example.dimot_bekalot.dataObjects.UpdatesAndAddsQueues;
import com.example.dimot_bekalot.entryActivities.Main_Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UpdateQueueActivity extends AppCompatActivity implements View.OnClickListener{
    TextView IDInput, dateInput, timeInput, typeInput;
    Button updateClientToQueue;

    private ImageButton logOut;
    Context context = this;

    int BLUE = R.color.blue;

    private static final String Queues = "Queues_institute", INSTITUTE = "Institutes";
    private FirebaseDatabase dataBase;
    private DatabaseReference dbRef_queue_institute;
    private DatabaseReference ref_institute;
    private String institute_id, queue, date, typeOfTreatment, theTime, client_id;
    /* for update_queue: */
    private String city, nameInstitute;
    private String day, month, year, hour, minute;


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
        client_id = intent.getExtras().getString("client_id");

        dataBase = FirebaseDatabase.getInstance();
        dbRef_queue_institute = dataBase.getReference(Queues);
        ref_institute = dataBase.getReference(INSTITUTE);

        ref_institute.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                city = snapshot.child(institute_id).child("address").child("city_Name").getValue().toString();
                nameInstitute = snapshot.child(institute_id).child("institute_name").getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        IDInput = (TextView) findViewById(R.id.id_input);
        dateInput = (TextView) findViewById(R.id.date_input);
        timeInput = (TextView) findViewById(R.id.hour_input);
        typeInput = (TextView) findViewById(R.id.update_chooseTreatmentType);
        updateClientToQueue = (Button) findViewById(R.id.update);

        String dateOfQueue[] = queue.split("\n");
        theTime = dateOfQueue[0]; // only the number of time

        hour = theTime.substring(0,2);
        minute = theTime.substring(2);

        day = date.substring(0,2);
        month = date.substring(2,4);
        year = date.substring(4);

        StringBuilder timeNew = new StringBuilder(theTime);
        timeNew.insert(2,":");
        timeInput.setText(timeNew.toString());

        StringBuilder dateNewName = new StringBuilder(date);
        dateNewName.insert(2, "/");
        dateNewName.insert(5, "/20");

        typeInput.setText(typeOfTreatment);

        dateInput.setText(dateNewName.toString());

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
            String idOfPatient = IDInput.getText().toString().trim();
            StringBuilder id_patient_input = new StringBuilder(idOfPatient);
            if(!idOfPatient.equals("TBD")){
                id_patient_input.insert(0, "p:");
            }

            dbRef_queue_institute.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (STOP_RUN == 0) {
                        Update_Queues update = new Update_Queues();
                        MyDate myDate = new MyDate(day, month, year, hour, minute);
                        TreatmentQueue tq = new TreatmentQueue(myDate, client_id, typeOfTreatment, nameInstitute, city);
                        update.cancel_patient(client_id, tq, context, idOfPatient);
/*                        UpdatesAndAddsQueues up = new UpdatesAndAddsQueues(institute_id, id_patient_input.toString(), date, theTime, typeOfTreatment);
                          up.update();
*/                        STOP_RUN = 1;
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
