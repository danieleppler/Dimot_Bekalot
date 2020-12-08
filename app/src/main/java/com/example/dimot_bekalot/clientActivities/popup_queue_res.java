package com.example.dimot_bekalot.clientActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dimot_bekalot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class popup_queue_res extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "popup_queue_res";

    TextView queue_det;
    Button confirm_btn;
    Context context=this;

    FirebaseDatabase mDatabase;
    DatabaseReference db_ref;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_queue_res);

        mDatabase = FirebaseDatabase.getInstance();
        db_ref=mDatabase.getReference().child("Queues");


        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        intent=getIntent();
        int width=dm.widthPixels;
        int height=dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8),(int) (height * 0.6));

        queue_det = (TextView) findViewById(R.id.queue_det);
        queue_det.setText(getIntent().getStringExtra("chosen_queue"));

        confirm_btn=(Button)findViewById(R.id.make_appointment);
        confirm_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == confirm_btn)
        {
            db_ref.addListenerForSingleValueEvent(new ValueEventListener()
            {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data:snapshot.getChildren()
                         ) {
                        if (data.getKey().equals(intent.getStringExtra("queue_id"))) {
                            Log.d(TAG,"entered to queue number:"+data.getKey());
                            //set the patient id
                            db_ref.child(data.getKey()).child("Patient_id_attending").setValue(intent.getStringExtra("client_id"));
                            CharSequence text = "booked successfully!";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            break;
                        }

                    }
                    Intent m_intent=new Intent(context, com.example.dimot_bekalot.clientActivities.Main_Client_View.class);
                    m_intent.putExtra("client_id",intent.getStringExtra("queue_id"));
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startActivity(m_intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}