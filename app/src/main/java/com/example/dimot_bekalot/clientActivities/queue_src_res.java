package com.example.dimot_bekalot.clientActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.dataObjects.TreatmentQueue;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.transform.Result;

public class queue_src_res extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "queue_search_res";

    Intent intent;
    List<String> queues;
    String  client_id;
    TextView num_of_options;
    ListView listView;

    String[] results;

    List<TreatmentQueue> tq_list=new ArrayList<>();
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
        queues= (List<String>) intent.getSerializableExtra("queues");
        client_id=intent.getStringExtra("client_id");
        Log.d(TAG,client_id);

        num_of_options=(TextView)findViewById(R.id.res_num2);
        num_of_options.setText(String.valueOf(queues.size()));

        results=new String[queues.size()];

        listView=(ListView) findViewById(R.id.results);

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
                            Log.d("TAG","checked for the " + ++check + " time");
                            results[j++]=data.child("institute").getValue()+"     "+data.child("treat_type").getValue()+"     "+data.child("date").getValue()+"     "+data.child("time").getValue();
                            Log.d("TAG",data.getKey()+"  "+ "added to the arraylist");
                            break;
                        }
                    }
                }

                for(int i=0;i<queues.size();i++)
                     Log.d("TAG","result    " + results[i]);

                ArrayAdapter adapter = new ArrayAdapter <String> (context,R.layout.simple_list_view,R.id.textView, results);
                listView.setAdapter(adapter);

                listView.setClickable(true);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(TAG,"item "+ position+" has been clicked");
                        showPopup(position);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
}