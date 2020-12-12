package com.example.dimot_bekalot.clientActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dimot_bekalot.R;

import java.util.ArrayList;
import java.util.List;

public class show_queues_res extends AppCompatActivity {

    ListView listView;

    List<String> queues;
    List<String> queues_id=new ArrayList<>();
    TextView noQueues,Queues;
    Intent intent;
    Context context=this;
    String client_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_queues_res);
        queues=new ArrayList<>();
        intent=getIntent();

        queues= (List<String>)intent.getSerializableExtra("queues");
        queues_id= (List<String>)intent.getSerializableExtra("queues_id");

        noQueues = (TextView) findViewById(R.id.noQueues);
        Queues=(TextView) findViewById(R.id.Queues);

        if(queues.size()==0)
            Queues.setVisibility(View.INVISIBLE);
        else
            noQueues.setVisibility(View.INVISIBLE);

        client_id=intent.getStringExtra("id");

        listView=(ListView) findViewById(R.id.queues);

        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.simple_list_view, R.id.textView, queues);
        listView.setAdapter(adapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPopup(position);
            }
        });
    }

    void showPopup(int position)
    {
        Intent intent = new Intent(this, com.example.dimot_bekalot.clientActivities.popup_queue_show_res.class);
        intent.putExtra("chosen_queue",queues.get(position));
        intent.putExtra("chosen_queue_id",queues_id.get(position));
        intent.putExtra("client_id",client_id);
        startActivity(intent);
    }

    CountDownTimer timer = new CountDownTimer(15 *60 * 1000, 1000) {

        public void onTick(long millisUntilFinished) {
            //Some code
        }

        public void onFinish() {
            Intent intent=new Intent(context,com.example.dimot_bekalot.entryActivities.Main_Activity.class);
            CharSequence text = "system wasn't used for 15 minutes,logging out";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            startActivity(intent);
        }
    };
}