package com.example.dimot_bekalot.clientActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.dataObjects.TreatmentQueue;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class queue_src_res extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "queue_search_res";

    Intent intent;
    List<String> queues;
    String  client_id;
    TextView num_of_options;
    TextView queues2;
    TextView temp1;
    TextView temp2;
    ListView listView;

    String[] results;

    Context context=this;

    FirebaseDatabase mDatabase;
    DatabaseReference queues_DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_src_res);

        mDatabase = FirebaseDatabase.getInstance();
        queues_DB= mDatabase.getReference().child("Queues");

        intent=getIntent();

        client_id=intent.getStringExtra("client_id");
        boolean toStay=intent.getBooleanExtra("toStay",true);
        if (!toStay)
        {
            Intent t_intent = new Intent(context, com.example.dimot_bekalot.clientActivities.Main_Client_View.class);
            t_intent.putExtra("client_id",client_id);
            startActivity(t_intent);
        }
        queues= (List<String>) intent.getSerializableExtra("queues");


        num_of_options=(TextView)findViewById(R.id.res_num2);
        temp1=(TextView)findViewById(R.id.res_num1);
        temp2=(TextView)findViewById(R.id.res_num3);
        num_of_options.setText(String.valueOf(queues.size()));
        queues2=(TextView)findViewById(R.id.Queues2);

        results=new String[queues.size()];
        listView=   (ListView) findViewById(R.id.results);

        if(queues.size()==0) {
            num_of_options.setVisibility(View.INVISIBLE);
            temp1.setVisibility(View.INVISIBLE);
            temp2.setVisibility(View.INVISIBLE);
        }
        else {
            queues2.setVisibility(View.INVISIBLE);
            queues_DB.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int check=0;
                    int j=0;
                    for (DataSnapshot data:snapshot.getChildren()
                    ) {
                        for (int i=0;i<queues.size();i++) {
                            if (data.getKey().equals(queues.get(i))) {
                                results[j++]=data.child("city").getValue()+"     "+data.child("treat_type").getValue()+"     "+data.child("institute").getValue()+"     "+data.child("date").getValue()+"     "+data.child("time").getValue();
                                break;
                            }
                        }
                    }
                    ArrayAdapter adapter = new ArrayAdapter <String> (context,R.layout.simple_list_view,R.id.textView, results);
                    listView.setAdapter(adapter);
                    listView.setClickable(true);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            showPopup(position);
                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

    }

    @Override
    public void onClick(View v) {

    }

    void showPopup(int position)
    {
        Intent intent = new Intent(context, com.example.dimot_bekalot.clientActivities.popup_queue_res.class);
        intent.putExtra("chosen_queue",results[position]);
        intent.putExtra("client_id",client_id);
        intent.putExtra("queue_id", queues.get(position));
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