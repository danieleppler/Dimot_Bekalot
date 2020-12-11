package com.example.dimot_bekalot.clientActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.dataObjects.MyDate;
import com.example.dimot_bekalot.dataObjects.TreatmentQueue;
import com.example.dimot_bekalot.dataObjects.Update_Queues;
import com.example.dimot_bekalot.entryActivities.Login_Activity;
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

    String type = "", nameInstitute = "", city = "", day="", year = "20";
    int hour,minute,month;

    FirebaseDatabase mDatabase;
    DatabaseReference Queues_ref,queues_src_ref,queues_inst_ref;

    TreatmentQueue tq=new TreatmentQueue();
    Update_Queues uq;

    Intent intent;

    String chosen_queue,time,date,client_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_queue_res);

        intent=getIntent();
        chosen_queue=intent.getStringExtra("chosen_queue");
        client_id=intent.getStringExtra("client_id");

        mDatabase = FirebaseDatabase.getInstance();
        Queues_ref=mDatabase.getReference().child("Queues");
        queues_src_ref=mDatabase.getReference().child("Queues_search");
        queues_inst_ref=mDatabase.getReference().child("Queues_institute");

        parse_treatment(tq);


        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width=dm.widthPixels;
        int height=dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8),(int) (height * 0.6));

        queue_det = (TextView) findViewById(R.id.queue_det);
        queue_det.setText(chosen_queue);

        confirm_btn=(Button)findViewById(R.id.make_appointment);
        confirm_btn.setOnClickListener(this);
    }

    private void parse_treatment(TreatmentQueue tq) {
        int i = 0;
        String hourT = "",minuteT = "",monthT = "";
            while (chosen_queue.charAt(i) != ' ') {
                city += chosen_queue.charAt(i);
                i++;
            }
            while (chosen_queue.charAt(i) == ' ')
                i++;
            while (chosen_queue.charAt(i) != ' ') {
                type += chosen_queue.charAt(i);
                i++;
            }
            while (chosen_queue.charAt(i) == ' ')
                i++;
            while (chosen_queue.charAt(i) != ' ') {
                nameInstitute += chosen_queue.charAt(i);
                i++;
            }
            while (chosen_queue.charAt(i) == ' ')
                i++;
            while (chosen_queue.charAt(i) != '.') {
                day += chosen_queue.charAt(i);
                i++;
            }
            i++;
            while (chosen_queue.charAt(i) != '.') {
                monthT += chosen_queue.charAt(i);
                i++;
            }
            i++;
            while (chosen_queue.charAt(i) != ' ') {
                year += chosen_queue.charAt(i);
                i++;
            }
            while (chosen_queue.charAt(i) == ' ')
                i++;

            while (chosen_queue.charAt(i) != ':') {
                hourT += chosen_queue.charAt(i);
                i++;
            }
            i++;
            while (i<chosen_queue.length()) {
                 minuteT += chosen_queue.charAt(i);
                  i++;
            }
            MyDate date=new MyDate(day,monthT,year,hourT,minuteT);
            tq.setType(type);
            tq.setCity(city);
            tq.setDate(date);
            tq.setNameInstitute(nameInstitute);
            tq.setIdPatient(client_id);
        }

    @Override
    public void onClick(View v) {
        if(v == confirm_btn)
        {

            Log.d(TAG,tq.getDate().getMonth()+" "+tq.getDate().getHour());
            uq=new Update_Queues();
            if(uq.update_new_Patient(client_id,tq))
                 Toast.makeText(getBaseContext(), "booked successfully!", Toast.LENGTH_SHORT).show();
            Intent m_intent=new Intent(context, com.example.dimot_bekalot.clientActivities.Main_Client_View.class);
            m_intent.putExtra("client_id",client_id);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startActivity(m_intent);
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
}