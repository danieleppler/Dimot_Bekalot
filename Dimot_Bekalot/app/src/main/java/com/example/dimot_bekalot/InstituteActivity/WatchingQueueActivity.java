package com.example.dimot_bekalot.InstituteActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dimot_bekalot.Tools.Strings_Tools;
import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.entryActivities.Main_Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WatchingQueueActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private static final String QUEUE = "Queues_institute";
    private String type_treatment, institute_id, client_id, date = "";
    private ImageButton logOut;
    Context context = this;

    private FirebaseDatabase dataBase;
    private DatabaseReference ref_queue;

    CalendarView calendar_view;
    Spinner spinner;

//    private final int WHITE = R.color.White;

    List<String> queueWithHourAndNumID;

    // for popup
    Dialog dialog;
    ListView lvQueues;
    
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watching_queue);

//        Intent intent = getIntent();
//        institute_id = intent.getExtras().getString("instituteID");
        institute_id = "i:123451234";

        /* <Spinner> */
        spinner = (Spinner) findViewById(R.id.watching_chooseTreatmentType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this, R.array.treatment_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        /* </Spinner> */

        logOut = (ImageButton) findViewById(R.id.logOutButton);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logOutIntent = new Intent(context, Main_Activity.class);
                startActivity(logOutIntent);
            }
        });

        dataBase = FirebaseDatabase.getInstance();
        ref_queue = dataBase.getReference(QUEUE);
        calendar_view = (CalendarView) findViewById(R.id.calendar);

        queueWithHourAndNumID = new ArrayList<>();

        // touch a date on the calendar:
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

                        if(type_treatment.equals("בחר סוג טיפול"))
                            Toast.makeText(context,
                                    "נא לבחור סוג טיפול", Toast.LENGTH_SHORT).show();
                        else {
                            for (DataSnapshot data : snapshot.child(institute_id).child("Treat_type").child(type_treatment).child(date).getChildren()) {
                                String hour = data.getKey();
                                String hourToViewInList = Strings_Tools.createNOTCleanUserName(hour, 2, ":");
                                String id = data.child("patient_id_attending").getValue().toString();
                                String allQueue = "שעה: " + hourToViewInList + "\n" + "מספר תעודת זהות: " + id;
                                client_id = id;
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

    /* This list shows the queues for that day when you click on a specific day */
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
        Intent update_intent = new Intent(context, UpdateQueueActivity.class); // if we want we can update queue
        update_intent.putExtra("instituteID", institute_id);
        update_intent.putExtra("type", type_treatment);
        String q = tmp.get(position);
        String queueOutput = q.substring(5,7) + "" +q.substring(8); // take just a number for the time
        update_intent.putExtra("queue", queueOutput);
        update_intent.putExtra("date", date);
        update_intent.putExtra("client_id", client_id);
        startActivity(update_intent);
    }

    /* <Spinner> */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type_treatment = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), type_treatment, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
    /* </Spinner> */

    @Override
    public void onBackPressed(){
        Intent back_to_main = new Intent(context, InstituteMain.class);
        startActivity(back_to_main);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}

}
