package com.example.dimot_bekalot.InstituteActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.dimot_bekalot.ListQueuesInstituteActivity;
import com.example.dimot_bekalot.Tools.Strings_Tools;
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
    private DatabaseReference ref_queue;

    CalendarView calendar_view;
    Context context = this;
    Spinner spinner;

    List<String> queueWithHourAndNumID;

    // for popup
    Dialog dialog;
    private TextView queue;
    ListView lvQueues;


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
        ref_queue = dataBase.getReference(QUEUE);
        calendar_view = (CalendarView) findViewById(R.id.calendar);

        queueWithHourAndNumID = new ArrayList<>();

        // touch date on the screen:
        ref_queue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                calendar_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                        if(day >= 1 && day <= 9){ date = "0"+day; }
                        else{ date = day + ""; }
                        month++;
                        if(month >= 1 && month <= 9){ date += ""+"0"+month;}
                        else{ date += month + "";}
                        date += ""+ String.valueOf(year).substring(2);

                        if(typeOfTreatment.equals("choose"))
                            Toast.makeText(context,
                                    "נא לבחור סוג טיפול", Toast.LENGTH_SHORT).show();
                        else {
                            for (DataSnapshot data : snapshot.child(institute_id).child("Treat_type").child(typeOfTreatment).child(date).getChildren()) {
                                String hour = data.getKey();
                                String hourToViewInList = Strings_Tools.createNOTCleanUserName(hour, 2, ":");
                                String id = data.child("patient_id_attending").getValue().toString();
                                String allQueue = "שעה: " +hourToViewInList + "\n" + "מספר תעודת זהות: " + id;
                                Log.d("queue", allQueue);
                                queueWithHourAndNumID.add(allQueue);
                            }
                            CreatePopupList();
                        }
                    }
                });

            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}

        }); // dbRef.addValueEventListener

    }

    private void CreatePopupList(){
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.popup_queue_per_day);

            dialog.setTitle("Queue per day");

            lvQueues = (ListView) dialog.findViewById(R.id.lvQueues);
            List<String> tmp = new ArrayList<String>();
            for(String data : queueWithHourAndNumID){ tmp.add(data); }
            queueWithHourAndNumID.clear();

        if(0 == tmp.size())
            Toast.makeText(this, "אין תורים להצגה ביום שנבחר", Toast.LENGTH_SHORT).show();
        else{
            ArrayAdapter queuesAdapter = new ArrayAdapter <String> (context, R.layout.simple_list, R.id.textView_stam, tmp);
            lvQueues.setAdapter(queuesAdapter);
            lvQueues.setClickable(true);
            lvQueues.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    open_updateActivity(position, tmp);
                }
            });
            dialog.show();
        }
    }

    private void open_updateActivity(int position, List<String> tmp){
        Intent update_intent = new Intent(context, UpdateQueueActivity.class);
        update_intent.putExtra("instituteID", institute_id);
        update_intent.putExtra("type", typeOfTreatment);
        String q = tmp.get(position);
        String queueOutput = q.substring(0,2) + "" +q.substring(3); // erase ":" from time
        update_intent.putExtra("queue", queueOutput);
        update_intent.putExtra("date", date);
        startActivity(update_intent);
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

}
