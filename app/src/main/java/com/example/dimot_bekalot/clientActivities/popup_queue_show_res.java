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
import com.example.dimot_bekalot.dataObjects.Costumer_Details_Patient;
import com.example.dimot_bekalot.dataObjects.MyDate;
import com.example.dimot_bekalot.dataObjects.TreatmentQueue;
import com.example.dimot_bekalot.dataObjects.Update_Queues;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.StringJoiner;

public class popup_queue_show_res extends AppCompatActivity implements View.OnClickListener{

    final String TAG="popup_queue_show_Res";

    TextView queue_det;

    Intent intent;

    String chosen_queue;

    Button confirm_btn;

    Update_Queues uq;

    String type = "", nameInstitute = "", city = "", day="", month = "", year = "20",hour="",minute="";
    String client_id;

    TreatmentQueue tq;

    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_queue_show_res);

        intent=getIntent();

        tq=new TreatmentQueue();

        chosen_queue=intent.getStringExtra("chosen_queue");

        client_id=intent.getStringExtra("client_id");

        parse_treatment(tq);

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width=dm.widthPixels;
        int height=dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8),(int) (height * 0.6));

        queue_det = (TextView) findViewById(R.id.queue_show_det);
        queue_det.setText(chosen_queue);

        confirm_btn=(Button)findViewById(R.id.cancel_confirm);
        confirm_btn.setOnClickListener(this);


    }

    private void parse_treatment(TreatmentQueue tq) {
        int i = 0;
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
            month += chosen_queue.charAt(i);
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
            hour += chosen_queue.charAt(i);
            i++;
        }
        i++;
        while (i<chosen_queue.length()) {
            minute += chosen_queue.charAt(i);
            i++;
        }
        MyDate date=new MyDate(day,month,year,hour,minute);
        tq.setType(nameInstitute);
        tq.setCity(city);
        tq.setDate(date);
        tq.setNameInstitute(type);
        tq.setIdPatient(client_id);
    }

    @Override
    public void onClick(View v) {
        if(v==confirm_btn)
        {
            uq=new Update_Queues();
            uq.cancel_patient(client_id,tq,this);
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