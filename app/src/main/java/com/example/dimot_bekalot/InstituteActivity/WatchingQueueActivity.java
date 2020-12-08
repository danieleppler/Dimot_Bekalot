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

import com.example.dimot_bekalot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class WatchingQueueActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    CalendarView calendar_view;
    private String date;
    private static final String QUEUE = "Queues_institute";
    private FirebaseDatabase dataBase;
    private DatabaseReference dbRef;

    Context context = this;

    // for pop up
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private Button yes, no;
    ListView lvQueues;
    Vector<String> queueWithHourAndNumID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watching_queue);

        Intent intent = getIntent();
        String institute_id  = intent.getExtras().getString("instituteID");
        String type = intent.getExtras().getString("Treatment_type");

        // touch date on the screen:
        calendar_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                date = (day + 1) +""+ (month + 1) +""+ (year + 1);
            }
        });

        dataBase = FirebaseDatabase.getInstance();
        dbRef = dataBase.getReference(QUEUE).child(institute_id)
                .child("Treat_type").child(type).child("Date_queue").child(date).getRoot();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                queueWithHourAndNumID = new Vector<String>();

                for (DataSnapshot data : snapshot.getChildren()) {
                    String t = data.getValue(String.class);
                    String id = data.child(t).child("patient_id_attending").getValue().toString();
                    String allQueue = t + "\n" + id;
                    queueWithHourAndNumID.add(allQueue);
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}

        }); // dbRef.addValueEventListener

        // <popup>
        lvQueues = (ListView)findViewById(R.id.lvQueues);
        ArrayAdapter<String> queuesAdapter = new ArrayAdapter(context,
                R.layout.simple_list, R.id.textView_stam, queueWithHourAndNumID);
        lvQueues.setAdapter(queuesAdapter);
        lvQueues.setClickable(true);
        lvQueues.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                yes = (Button)findViewById(R.id.button_yes);
                no = (Button)findViewById(R.id.button_no);

                String answer = createPopup(position);

                if("yes" == answer){
                    Intent update_intent = new Intent(context,
                            com.example.dimot_bekalot.InstituteActivity.UpdateQueueActivity.class);
                    startActivity(update_intent);
                }
                else if("no" == answer){
                    Intent goBack_intent = new Intent(context,
                            com.example.dimot_bekalot.InstituteActivity.WatchingQueueActivity.class);
                    startActivity(goBack_intent);
                }
            }
        });

    }

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
}