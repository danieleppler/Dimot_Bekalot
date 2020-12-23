package com.example.dimot_bekalot.clientActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.dataObjects.MyDate;
import com.example.dimot_bekalot.dataObjects.TreatmentQueue;
import com.example.dimot_bekalot.dataObjects.Update_Queues;

import java.io.Serializable;
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

    TreatmentQueue tq=new TreatmentQueue();
    TextView queue_det;


    String type = "", nameInstitute = "", city = "", day="", year = "20",chosen_queue;
    Update_Queues uq;

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
        chosen_queue=queues.get(position);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_popup_queue_show_res, null);
        queue_det = (TextView)view.findViewById(R.id.queue_show_det);
        queue_det.setText(chosen_queue);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("האם אתה בטוח שברצונך לבטל את התור")
                .setNegativeButton("חזור אחורה", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(context, com.example.dimot_bekalot.clientActivities.show_queues_res.class);
                        intent.putExtra("queues", (Serializable) queues);
                        intent.putExtra("queues_id", (Serializable) queues_id);
                        intent.putExtra("id",client_id);
                        startActivity(intent);
                    }
                })
                .setPositiveButton("בטל", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        parse_treatment(tq);
                        Log.d("check",tq.getDate()+""+tq.getNameInstitute()+""+tq.getNameInstitute()+""+tq.getCity()+""+tq.getIdPatient());
                        uq=new Update_Queues();
                        uq.cancel_patient(client_id,tq,context);
                    }
                });
        builder.create();
        builder.show();
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

    /*

     */
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
        tq.setType(nameInstitute);
        tq.setCity(city);
        tq.setDate(date);
        tq.setNameInstitute(type);
        tq.setIdPatient(client_id);
    }
}