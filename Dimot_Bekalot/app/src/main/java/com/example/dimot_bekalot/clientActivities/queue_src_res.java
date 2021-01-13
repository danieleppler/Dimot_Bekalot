package com.example.dimot_bekalot.clientActivities;

     /*
    this is the screen where the queue search results are showed
     */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.dataObjects.MyDate;
import com.example.dimot_bekalot.dataObjects.TreatmentQueue;
import com.example.dimot_bekalot.generalActivities.UpdateQueues;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.List;

public class queue_src_res extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "queue_search_res";

    Intent intent;
    List<String> queues;
    String  client_id;
    TextView num_of_options;

    TextView temp1;
    TextView temp2;
    ListView listView;

    String[] results;

    Context context=this;

    FirebaseDatabase mDatabase;
    DatabaseReference queues_DB;

    TreatmentQueue tq=new TreatmentQueue();
    TextView queue_det;
    String type = "", nameInstitute = "", city = "", day="", year = "20",chosen_queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_src_res);

        mDatabase = FirebaseDatabase.getInstance();
        queues_DB= mDatabase.getReference().child("Queues");

        intent=getIntent();

            client_id = intent.getStringExtra("client_id");
            boolean toStay = intent.getBooleanExtra("toStay", true);
            if (!toStay) {
                Intent t_intent = new Intent(context, com.example.dimot_bekalot.clientActivities.Main_Client_View.class);
                t_intent.putExtra("client_id", client_id);
                startActivity(t_intent);
            }
            queues = (List<String>) intent.getSerializableExtra("queues");


            num_of_options = (TextView) findViewById(R.id.res_num2);
            temp1 = (TextView) findViewById(R.id.res_num1);
            temp2 = (TextView) findViewById(R.id.res_num3);
            num_of_options.setText(String.valueOf(queues.size()));


            results = new String[queues.size()];
            listView = (ListView) findViewById(R.id.results);

                queues_DB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int check = 0;
                        int j = 0;
                        for (DataSnapshot data : snapshot.getChildren()
                        ) {
                            for (int i = 0; i < queues.size(); i++) {
                                if (data.getKey().equals(queues.get(i))) {
                                    results[j++] = data.child("city").getValue() + "     " + data.child("treat_type").getValue() + "     " + data.child("institute").getValue() + "     " + data.child("date").getValue() + "     " + data.child("time").getValue();
                                    break;
                                }
                            }
                        }
                        ArrayAdapter adapter = new ArrayAdapter<String>(context, R.layout.simple_list_view, R.id.textView, results);
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


    @Override
    public void onClick(View v) {

    }

    //when patient click on queue that he want to book, this function pops
    void showPopup(int position)
    {
        chosen_queue=results[position];
        parse_treatment(tq);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_popup_queue_res, null);
        queue_det = (TextView)view.findViewById(R.id.queue_det);
        queue_det.setText(chosen_queue);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("האם אתה בטוח שברצונך לקבוע את התור")
                .setNegativeButton("בטל", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(context, com.example.dimot_bekalot.clientActivities.queue_src_res.class);
                        intent.putExtra("queues", (Serializable) queues);
                        intent.putExtra("client_id", client_id);
                        startActivity(intent);
                    }
                })
                .setPositiveButton("קבע", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(context, UpdateQueues.class);
                        intent.putExtra("client_id",client_id);
                        intent.putExtra("tq",tq.toString());
                        intent.putExtra("flag","book");
                        intent.putExtra("toPrint",true);
                        startActivity(intent);
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

    //in this function we parsing a string that indicating queue to an Treatment_queue object
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
}