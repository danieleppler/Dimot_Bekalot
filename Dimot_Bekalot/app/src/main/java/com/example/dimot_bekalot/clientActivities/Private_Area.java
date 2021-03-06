package com.example.dimot_bekalot.clientActivities;

     /*
    this is the private area of the patient, can navigate to the following screens:
    1.patient details editing screen
    2.showing queues that had been booked by the patient
     */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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

public class Private_Area extends AppCompatActivity implements View.OnClickListener{

    Button edit_det,show_queues;

    FirebaseDatabase mDatabase;
    DatabaseReference db_ref;

    List<String> queues;
    List<String> queues_id;

    String client_id;

    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private__area);


        client_id=getIntent().getStringExtra("id");

        edit_det=(Button)findViewById(R.id.edit_det);
        show_queues=(Button)findViewById(R.id.show_queues);

        edit_det.setOnClickListener(this);
        show_queues.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance();
        db_ref=mDatabase.getReference().child("Queues");


    }

    @Override
    public void onClick(View v) {
        if (v==edit_det)
        {
            Intent intent=new Intent(context, com.example.dimot_bekalot.clientActivities.edit_client_det.class);
            intent.putExtra("client_id",client_id);
            startActivity(intent);
        }
        if (v==show_queues)
        {
            db_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    queues=new ArrayList<>();
                    queues_id=new ArrayList<>();
                    for (DataSnapshot data:snapshot.getChildren()
                         ) { //searching for all the queues matching the patient id
                        String temp=data.child("city").getValue()+"     "+data.child("institute").getValue()+"     "+
                                data.child("treat_type").getValue()+"     "+data.child("date").getValue()
                                +"     "+data.child("time").getValue();
                        if (data.child("patient_id_attending").getValue().equals(client_id)) {
                            queues.add(temp);//adding the queues to a container
                            queues_id.add(data.getKey());
                        }
                    }
                    Intent intent=new Intent(context, com.example.dimot_bekalot.clientActivities.show_queues_res.class);
                    intent.putExtra("queues", (Serializable) queues); //passing the container in the intent
                    intent.putExtra("queues_id", (Serializable) queues_id);
                    intent.putExtra("id",client_id);
                    startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
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