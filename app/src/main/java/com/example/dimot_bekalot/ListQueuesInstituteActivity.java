package com.example.dimot_bekalot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.dimot_bekalot.InstituteActivity.UpdateQueueActivity;
import com.example.dimot_bekalot.InstituteActivity.WatchingQueueActivity;

import java.util.List;

public class ListQueuesInstituteActivity extends AppCompatActivity {

    ListView lvQueues;
    List<String> queues;
    private String institute_id;
    private String typeOfTreatment;
    Context context = this;

    // for pop up
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private Button yes, no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_queues_institute);

        Intent intent = getIntent();
        institute_id = intent.getExtras().getString("instituteID");
        typeOfTreatment = intent.getExtras().getString("type");
        queues = (List<String>) intent.getSerializableExtra("queues");

        lvQueues = (ListView)findViewById(R.id.lvQueues);

        ArrayAdapter queuesAdapter = new ArrayAdapter <String> (context,R.layout.simple_list,R.id.textView_stam, queues);
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

    private String createPopup(int position){
        final String[] ans = {""};
        dialogBuilder = new AlertDialog.Builder(context);
        final View popupView = getLayoutInflater().inflate(R.layout.popup_text_question, null);

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

    private void open_updateActivity(String institute_id){
        Intent update_intent = new Intent(context, UpdateQueueActivity.class);
        update_intent.putExtra("instituteID", institute_id);
        startActivity(update_intent);
    }
    private void goBack(String institute_id, String type){
        Intent goBack_intent = new Intent(context, WatchingQueueActivity.class);
        goBack_intent.putExtra("instituteID", institute_id); // to go back with
        goBack_intent.putExtra("Treatment_type", type);      // same numID & type
        startActivity(goBack_intent);
    }

}