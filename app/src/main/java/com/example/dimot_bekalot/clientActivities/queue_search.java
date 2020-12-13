package com.example.dimot_bekalot.clientActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dimot_bekalot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class queue_search extends AppCompatActivity implements View.OnClickListener, Serializable {
    private static final String TAG = "queue_search";

    Button src_btn;

    Spinner treat_type_Spinner;
    Spinner city_Spinner;

    String treat_type=" ";
    String city=" ";
    String client_id;

    Context context=this;

    FirebaseDatabase mDatabase;
    DatabaseReference queues_DB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_search);
        client_id=this.getIntent().getStringExtra("id");

        mDatabase = FirebaseDatabase.getInstance();
        queues_DB= mDatabase.getReference().child("Queues_search").child("City");

        //need to see how to extract data from the db

        src_btn=(Button)findViewById(R.id.search_btn);
        src_btn.setOnClickListener(this);

        treat_type_Spinner = (Spinner) findViewById(R.id.treat_type_spinner);
        city_Spinner=(Spinner)findViewById(R.id.city_spinner);

        treat_type_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                treat_type=treat_type_Spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // can leave this empty
            }
        });

        city_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                city=city_Spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // can leave this empty
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v==src_btn)
        {
            if(city.equals("choose") || treat_type.equals("choose")) {
                if (treat_type.equals("choose")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "please choose a treatment type", Toast.LENGTH_SHORT);
                    toast.show();
                }
                if (city.equals("choose")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "please choose a city", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            else {
                queues_DB.addValueEventListener(new ValueEventListener()
                {
                    List<String> queues_id=new ArrayList<>();
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data   :   dataSnapshot.getChildren()) {
                            if (data.getKey().equals(city))
                                for (DataSnapshot data2:data.child("treat_type").getChildren()
                                     ) {
                                    if(data2.getKey().equals(treat_type))
                                        for (DataSnapshot data3:data2.getChildren()
                                             ) {
                                            if(data3.child("Patient_id_attending").getValue().toString().equals("TBD"))
                                                queues_id.add(data3.getKey());
                                        }
                                }
                        }
                            if(queues_id.size()==0)
                            {
                                Toast toast = Toast.makeText(getApplicationContext(), "there is no results for your preference", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else {
                                Intent intent = new Intent(context, com.example.dimot_bekalot.clientActivities.queue_src_res.class);
                                intent.putExtra("queues", (Serializable) queues_id);
                                intent.putExtra("client_id", client_id);
                                intent.putExtra("toStay", true);
                                startActivity(intent);
                            }
                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }
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