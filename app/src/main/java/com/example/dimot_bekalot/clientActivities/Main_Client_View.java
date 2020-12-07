package com.example.dimot_bekalot.clientActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.SharedMemory;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dimot_bekalot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class Main_Client_View extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Main_Client_View";

    Button queue_order, inst_list, Private_Area;
    TextView client_name;

    SharedPreferences sp;


    String client_id=" ";
    List<String> client_names=new ArrayList<>();

    FirebaseDatabase mDatabase;
    DatabaseReference db_ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_client_view);
        client_id=getIntent().getStringExtra("client_id");// real-time
        Log.d(TAG,"trying to invoke");
       // client_id = "22222222"; //debugging
        db_ref = mDatabase.getReference().child("Patients").child(client_id);


        queue_order = (Button) findViewById(R.id.queue_order);
        inst_list = (Button) findViewById(R.id.inst_list);
        Private_Area = (Button) findViewById(R.id.Private_Area);

       readData(new FireBaseCallBack() {
           @Override
           public void onCallBack(List<String> list) {

           }
       });


        queue_order.setOnClickListener(this);
        inst_list.setOnClickListener(this);
        Private_Area.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == queue_order) {
            Intent qo_intent = new Intent(this, com.example.dimot_bekalot.clientActivities.queue_search.class);
            qo_intent.putExtra("id", client_id);
            startActivity(qo_intent);
        }

        if (v == Private_Area) {
            Intent pv_intent = new Intent(this, com.example.dimot_bekalot.clientActivities.Private_Area.class);
            pv_intent.putExtra("id", client_id);
            startActivity(pv_intent);
        }

        if (v == inst_list) {
            Intent il_intent = new Intent(this, com.example.dimot_bekalot.clientActivities.inst_list.class);
            startActivity(il_intent);
        }

    }

    private void readData(FireBaseCallBack myCallback) {
        db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                client_names.add(dataSnapshot.child("first_name").getValue(String.class));
                client_names.add(dataSnapshot.child("second_name").getValue(String.class));
                client_name = (TextView) findViewById(R.id.client_name);
                client_name.setText(client_names.get(0)+" "+client_names.get(1));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });
        myCallback.onCallBack(client_names);
    }
    private interface FireBaseCallBack{
        void onCallBack(List<String> list);
    }

}