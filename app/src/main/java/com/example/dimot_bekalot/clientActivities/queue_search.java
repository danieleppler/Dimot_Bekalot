package com.example.dimot_bekalot.clientActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.dataObjects.MyDate;
import com.example.dimot_bekalot.dataObjects.TreatmentQueue;
import com.example.dimot_bekalot.dataObjects.Update_Queues;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class queue_search extends AppCompatActivity implements View.OnClickListener, Serializable {
    private static final String TAG = "queue_search";

    Button src_btn;

    Spinner treat_type_Spinner;
    Spinner city_Spinner;

    String treat_type=" ";
    String city=" ";
    String client_id;

    Date fromDate,toDate;

    Context context=this;

    FirebaseDatabase mDatabase;
    DatabaseReference queues_DB;
    TextView toDate2,fromDate2;
    DatePickerDialog.OnDateSetListener from_dateListener,to_dateListener;
    Calendar toCalendar,fromCalendar;

    TextView queue_det;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_search);
        //calender
        toDate2= (TextView) findViewById(R.id.toDate);
        fromDate2= (TextView) findViewById(R.id.fromDate);
        fromDate2.setText("בחר");
        toDate2.setText("בחר");
        toCalendar = Calendar.getInstance();
        fromCalendar=Calendar.getInstance();
        from_dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                Log.d("picked date",year+" "+(monthOfYear+1)+" "+dayOfMonth);
                fromDate=new Date();
                fromDate.setYear(year);
                fromDate.setMonth(monthOfYear);
                fromDate.setDate(dayOfMonth);
                fromDate2.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
            }
        };

        to_dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                toDate=new Date();
                toDate.setYear(year);
                toDate.setMonth(monthOfYear);
                toDate.setDate(dayOfMonth);
                toDate2.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
            }
        };

        fromDate2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(context, from_dateListener, fromCalendar
                        .get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH),
                        fromCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        toDate2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(context, to_dateListener, toCalendar
                        .get(Calendar.YEAR), toCalendar.get(Calendar.MONTH),
                        toCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        client_id=this.getIntent().getStringExtra("id");

        mDatabase = FirebaseDatabase.getInstance();
        queues_DB= mDatabase.getReference().child("Queues_search").child("City");

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
                    Toast toast = Toast.makeText(getApplicationContext(), "אנא בחר סוג טיפול", Toast.LENGTH_SHORT);
                    toast.show();
                }
                if (city.equals("choose")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "אנא בחר עיר", Toast.LENGTH_SHORT);
                    toast.show();
                }
                if(fromDate==null&& toDate!=null)
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "אנא בחר טווח תאריך מקסימלי", Toast.LENGTH_SHORT);
                    toast.show();
                }
                if(fromDate!=null&&toDate==null)
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "אנא בחר טווח תאריך מינימלי", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            else {
                queues_DB.addValueEventListener(new ValueEventListener()
                {
                    boolean isBetweenDate=true;
                    boolean showPreferenceToast=true; //bin
                    boolean alreadyOnTheList=false;
                    List<String> queues_id=new ArrayList<>();
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data   :   dataSnapshot.getChildren()) {
                            if (data.getKey().equals(city))
                                for (DataSnapshot data2:data.child("Treat_type").getChildren()
                                     ) {
                                    if(data2.getKey().equals(treat_type))
                                        for (DataSnapshot data3:data2.getChildren()
                                             ) {
                                            String tempDate = data3.child("date").getValue().toString();
                                            int year = Integer.parseInt("20" + tempDate.substring(6, 8));
                                            int month = Integer.parseInt(tempDate.substring(3, 5)) - 1;
                                            int day = Integer.parseInt(tempDate.substring(0, 2));
                                            String hour=data3.child("time").getValue().toString().substring(0,2);
                                            String minute=data3.child("time").getValue().toString().substring(3,5);
                                            if(fromDate!=null && toDate!=null) {
                                                if ((year >= fromDate.getYear() && year <= toDate.getYear())
                                                        && (month >= fromDate.getMonth() && month <= toDate.getMonth())
                                                        && (day >= fromDate.getDate() && day <= toDate.getDate())) {
                                                    isBetweenDate = true;
                                                }
                                                   else isBetweenDate = false;
                                            }
                                            if(data3.child("patient_id_attending").getValue().toString().equals("TBD")) {
                                                if (isBetweenDate)
                                                    queues_id.add(data3.getKey());
                                            }
                                                else {
                                                if (!data3.child("patient_id_attending").getValue().toString().equals(client_id))
                                                    if (isBetweenDate)
                                                    {
                                                        for (DataSnapshot data4:data3.child("Waiting_list").getChildren()
                                                             ) {
                                                            if (data4.getKey().equals(client_id))
                                                                alreadyOnTheList=true;
                                                        }
                                                        if (!alreadyOnTheList) {
                                                            for (DataSnapshot data4 : data3.child("Waiting_list").getChildren()
                                                            ) {
                                                                if (data4.getValue().toString().equals("TBD")) {
                                                                    TreatmentQueue tq = new TreatmentQueue(new MyDate(String.valueOf(day), String.valueOf(month + 1)
                                                                            , String.valueOf(year), hour, minute), client_id, treat_type, data3.child("institute").getValue().toString()
                                                                            , city);
                                                                    Showpopup(tq, data4.getKey());
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                   }
                                                }
                                        }
                                }
                        }
                            if(queues_id.size()==0)
                            {
                                if(showPreferenceToast) { //bin
                                    Toast toast = Toast.makeText(getApplicationContext(), "אין תוצאות העונות לחיפושך", Toast.LENGTH_SHORT);
                                    toast.show();
                                } // bin
                            }
                            else {
                                Intent intent = new Intent(context, com.example.dimot_bekalot.clientActivities.queue_src_res.class);
                                intent.putExtra("queues", (Serializable) queues_id);
                                intent.putExtra("client_id", client_id);
                                intent.putExtra("check", "0");
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

    void Showpopup(TreatmentQueue tq,String position)
    {
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_waiting_list, null);
        queue_det = (TextView)view.findViewById(R.id.waiting_queue_det);
        queue_det.setText(tq.toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("לא נמצאו תורים זמינים העונים לחיפושך, האם אתה רוצה להיכנה לרשימת המתנה לתור:")
                .setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(context, com.example.dimot_bekalot.clientActivities.queue_search.class);
                        intent.putExtra("client_id", client_id);
                        startActivity(intent);
                    }
                }).setPositiveButton("כן,תכניס אותי", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Update_Queues uq=new Update_Queues();
                uq.update_waiting_list(client_id,tq,position,context);
            }
        });
        builder.create();
        builder.show();
    }
}