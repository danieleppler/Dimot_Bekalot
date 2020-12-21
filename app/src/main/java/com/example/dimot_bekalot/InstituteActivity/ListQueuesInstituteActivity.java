package com.example.dimot_bekalot.InstituteActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.dimot_bekalot.InstituteActivity.UpdateQueueActivity;
import com.example.dimot_bekalot.InstituteActivity.WatchingQueueActivity;
import com.example.dimot_bekalot.R;

import java.util.List;

public class ListQueuesInstituteActivity extends AppCompatActivity {

    ListView lvQueues;
    List<String> queues;
    private String institute_id, date;
    private String typeOfTreatment;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_queues_institute);

        Intent intent = getIntent();
        institute_id = intent.getExtras().getString("instituteID");
        typeOfTreatment = intent.getExtras().getString("type");
        date = intent.getExtras().getString("date");
        queues = (List<String>) intent.getSerializableExtra("queues");

        lvQueues = (ListView) findViewById(R.id.lvQueues);

        ArrayAdapter queuesAdapter = new ArrayAdapter <String> (context,R.layout.simple_list,R.id.textView_stam, queues);
        lvQueues.setAdapter(queuesAdapter);
        lvQueues.setClickable(true);
        lvQueues.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                open_updateActivity(position);
            }
        });

    }

    private void open_updateActivity(int position){
        Intent update_intent = new Intent(context, UpdateQueueActivity.class);
        update_intent.putExtra("instituteID", institute_id);
        update_intent.putExtra("type", typeOfTreatment);
        String q = queues.get(position);
        update_intent.putExtra("queue", q);
        update_intent.putExtra("date", date);
        startActivity(update_intent);
    }

}