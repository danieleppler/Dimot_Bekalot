package com.example.dimot_bekalot.InstituteActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dimot_bekalot.ListQueuesInstituteActivity;
import com.example.dimot_bekalot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class WatchingQueueActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private String date = "";
    private static final String QUEUE = "Queues_institute";
    private String typeOfTreatment;
    private String institute_id;

    private FirebaseDatabase dataBase;
    private DatabaseReference dbRef;

    CalendarView calendar_view;
    Context context = this;
    Spinner spinner;


    List<String> queueWithHourAndNumID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watching_queue);

        Intent intent = getIntent();
        institute_id = intent.getExtras().getString("instituteID");

        /* <Spinner> */
        spinner = (Spinner) findViewById(R.id.watching_chooseTreatmentType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this, R.array.treatment_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        /* </Spinner> */

        dataBase = FirebaseDatabase.getInstance();
        dbRef = dataBase.getReference(QUEUE);
        calendar_view = (CalendarView)findViewById(R.id.calendar);

        queueWithHourAndNumID = new ArrayList<>();
        // touch date on the screen:

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                calendar_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                        if(day >= 1 && day <= 9){ date = "0"+day; }
                        else{ date = day + ""; }
                        if(month >= 1 && month <= 9){ date += ""+"0"+month;}
                        else{ date += month + "";}
                        date += ""+ year;
                    }
                });
                for (DataSnapshot data : snapshot.child(institute_id).child("Treat_type").child(typeOfTreatment).child(date).getChildren()) {
                    String hour = data.getKey();
                    String id = data.child("Patient_id_attending").getValue().toString();
                    String allQueue = hour + "\n" + id;
                    queueWithHourAndNumID.add(allQueue);
                }
                goToList();

            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}

        }); // dbRef.addValueEventListener

    }

    /* <Spinner> */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        typeOfTreatment = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), typeOfTreatment, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
    /* </Spinner> */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}


    private void goToList(){
        Intent list_intent = new Intent(context, ListQueuesInstituteActivity.class);
        list_intent.putExtra("instituteID", institute_id);
        list_intent.putExtra("type", typeOfTreatment);
        list_intent.putExtra("queues", (Serializable)queueWithHourAndNumID);
        startActivity(list_intent);
    }
}