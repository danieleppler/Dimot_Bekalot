package com.example.dimot_bekalot.InstituteActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dimot_bekalot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class WatchingQueueActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private String date;
    private static final String QUEUE = "Queues_institute";
    private String typeOfTreatment = "";

    private FirebaseDatabase dataBase;
    private DatabaseReference dbRef;

    CalendarView calendar_view;
    Context context = this;
    Spinner spinner;

    // for pop up
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private Button yes, no;
    ListView lvQueues;
    List<String> queueWithHourAndNumID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watching_queue);
        queueWithHourAndNumID = new ArrayList<>();
        Intent intent = getIntent();
        String institute_id  = intent.getExtras().getString("instituteID");
//        String type = intent.getExtras().getString("Treatment_type");

        /* <Spinner> */
        spinner = (Spinner) findViewById(R.id.watching_chooseTreatmentType);
       /* ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this, R.array.treatment_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);*/
        /* </Spinner> */

        typeOfTreatment = "MRI";
        date="190122";
        dataBase = FirebaseDatabase.getInstance();
        dbRef = dataBase.getReference(QUEUE);
        calendar_view = (CalendarView)findViewById(R.id.calendar);
        // touch date on the screen:
        lvQueues = (ListView)findViewById(R.id.lvQueues);
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
                    String t = data.getValue(String.class);
                    String id = data.child(t).child("patient_id_attending").getValue().toString();
                    String allQueue = t + "\n" + id;
                    queueWithHourAndNumID.add(allQueue);
                }
                ArrayAdapter queuesAdapter = new ArrayAdapter <String> (context,R.layout.simple_list,R.id.textView_stam, queueWithHourAndNumID);
                lvQueues.setAdapter(queuesAdapter);
                lvQueues.setClickable(true);
                lvQueues.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        yes = (Button)findViewById(R.id.button_yes);
                        no = (Button)findViewById(R.id.button_no);
                        String answer = createPopup(position);
                        if("yes" == answer){
                            open_updateActivity(institute_id);
                        }
                        else if("no" == answer){
                            goBack(institute_id, typeOfTreatment);
                        }
                    }
                });
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}

        }); // dbRef.addValueEventListener

        /* <popup> */

        /* </popup> */
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


    private String createPopup(int position){
        final String[] ans = {""};
        dialogBuilder = new AlertDialog.Builder(context);
        final View popupView = getLayoutInflater().inflate(R.layout.popup_queue_per_day, null);

        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans[0] = "yes";
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans[0] = "no";
            }
        });
        return ans[0];
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}

    private void open_updateActivity(String institute_id){
        Intent update_intent = new Intent(context,
                com.example.dimot_bekalot.InstituteActivity.UpdateQueueActivity.class);
        update_intent.putExtra("instituteID", institute_id);
        startActivity(update_intent);
    }
    private void goBack(String institute_id, String type){
        Intent goBack_intent = new Intent(context,
                com.example.dimot_bekalot.InstituteActivity.WatchingQueueActivity.class);
        goBack_intent.putExtra("instituteID", institute_id); // to go back with
        goBack_intent.putExtra("Treatment_type", type);      // same numID & type
        startActivity(goBack_intent);
    }
}