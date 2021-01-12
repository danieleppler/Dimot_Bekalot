package com.example.dimot_bekalot.generalActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.dimot_bekalot.dataObjects.MyDate;
import com.example.dimot_bekalot.dataObjects.TreatmentQueue;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UpdateQueues extends AppCompatActivity {

    FirebaseDatabase mDatabase;
    DatabaseReference Queues_ref, queues_src_ref, queues_inst_ref, inst_ref;
    private static final String TAG = "Update_Queues";
    String inst_id = "";
    int mutexQueues=0,mutexSrc=0,mutexinst=0;
    boolean isBooked=false;

    int i=0,count1=0,count2=0;

    String client_to_notify="TBD";
    String queue_id,tempDay="",tempMonth="",time2,date2;
    DatabaseReference pt_ref;
    String waiting_cnt="";
    String chosen_queue;
    TreatmentQueue tq;
    String client_id="",type = "", nameInstitute = "", city = "", day="", year = "";
    static int mutex=0;
    private int STOP_RUN = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String flag=getIntent().getStringExtra("flag");
        tq=new TreatmentQueue();
        if (mutex==0) {
            mutex++;
            if (flag.equals("cancel"))
                cancel_patient();
            else if(flag.equals("add_to_waiting"))
                add_to_waiting_list();
            else {
                client_id=getIntent().getStringExtra("client_id");
                chosen_queue=getIntent().getStringExtra("tq");
                tq=new TreatmentQueue();
                parse_treatment(tq);
                Boolean toPrint=getIntent().getBooleanExtra("toPrint",true);
                update_new_Patient(client_id, tq, this, toPrint);
            }
        }

    }

    public void update_new_Patient(String client_id, TreatmentQueue tq, Context context,Boolean toPrint) {

        mDatabase = FirebaseDatabase.getInstance();
        queues_src_ref = mDatabase.getReference().child("Test_Queues_search");
        queues_inst_ref = mDatabase.getReference().child("Test_Queues_institute");
        inst_ref = mDatabase.getReference().child("Institutes");

        String date = tq.getDate().getDay() + "." + tq.getDate().getMonth() + "." + String.valueOf(tq.getDate().getYear()).substring(2);
        String time = tq.getDate().getHour() + ":" + tq.getDate().getMinute();
        update_new_in_queues(tq,date,time,client_id,toPrint,context);
    }

    private void update_new_in_queues(TreatmentQueue tq, String date, String time, String client_id, Boolean toPrint, Context context) {
        Queues_ref = mDatabase.getReference().child("Test_Queues");
        Queues_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()
                ) {
                    if (data.child("institute").getValue().equals(tq.getNameInstitute()) && data.child("date").getValue().equals(date)
                            && data.child("time").getValue().equals(time) && data.child("treat_type").getValue().equals(tq.getType())) {
                        Queues_ref.child(data.getKey()).child("patient_id_attending").setValue(client_id);
                        break;
                    }
                }
                update_new_in_queues_src(tq,date,time,client_id,toPrint,context);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void update_new_in_queues_src(TreatmentQueue tq,String date,String time,String client_id,Boolean toPrint,Context context) {
        queues_src_ref.child("City").child(tq.getCity()).child("Treat_type").child(tq.getType()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()
                ) {
                    if (data.child("date").getValue().equals(date) && data.child("time").getValue().equals(time) && data.child("institute").getValue().equals(tq.getNameInstitute())) {
                        queues_src_ref.child("City").child(tq.getCity()).child("Treat_type").child(tq.getType()).child(data.getKey()).child("patient_id_attending").setValue(client_id);
                        break;
                    }
                }
                update_new_in_queues_inst(tq,client_id,toPrint,context);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void update_new_in_queues_inst(TreatmentQueue tq,String client_id,Boolean toPrint,Context context) {
        inst_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()
                )
                    if (data.child("institute_name").getValue().equals(tq.getNameInstitute())) {
                        inst_id = data.getKey();
                    }
                if (!inst_id.equals("")) {
                    int count1=0,count2=0;
                    tempDay=tq.getDate().getDay();
                    tempMonth=tq.getDate().getMonth();
                    for (int i=0;i<tempDay.length();i++)
                        count1++;
                    for(int i=0;i<tempMonth.length();i++)
                        count2++;
                    if (count1==1)
                        tempDay="0"+tempDay;
                    if (count2==1)
                        tempMonth="0"+tempMonth;
                    queues_inst_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            queues_inst_ref.child(inst_id).child("Treat_type").child(tq.getType()).
                                    child(tempDay + tempMonth+ tq.getDate().getYear().substring(2))
                                    .child(tq.getDate().getHour() + tq.getDate().getMinute()).child("patient_id_attending").setValue(client_id);
                            mDatabase.getReference("Patients").child(client_id).child("ActiveQueues").child("NumberOfWaitingQueues").setValue("0");
                            if(toPrint)
                                printAndMove(client_id,"התור נקבע בהצלחה",context);
                            else printAndMove(client_id,"התור בוטל בהצלחה",context);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void cancel_patient() {
        mDatabase = FirebaseDatabase.getInstance();
        client_id=getIntent().getStringExtra("client_id");
        chosen_queue=getIntent().getStringExtra("tq");
        parse_treatment(tq);
        String date = tq.getDate().getDay() + "." + tq.getDate().getMonth() + "." + String.valueOf(tq.getDate().getYear()).substring(2);
        String time = tq.getDate().getHour() + ":" + tq.getDate().getMinute();
        Queues_ref = mDatabase.getReference().child("Queues");
        queues_cancel(client_id, tq, this , date, time);

    }

    public void queues_cancel(String client_id, TreatmentQueue tq, Context context, String date, String time) {
        tempDay=tq.getDate().getDay();
        tempMonth=tq.getDate().getMonth();
        for (int i=0;i<tempDay.length();i++)
            count1++;
        for(int i=0;i<tempMonth.length();i++)
            count2++;
        if (count1==1)
            tempDay="0"+tempDay;
        if (count2==1)
            tempMonth="0"+tempMonth;
        date2 = tempDay + tempMonth + String.valueOf(tq.getDate().getYear()).substring(2);
        time2 = tq.getDate().getHour() +tq.getDate().getMinute();
        Queues_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()
                ) {
                    if (data.child("institute").getValue().equals(tq.getNameInstitute()) && data.child("date").getValue().equals(date)
                            && data.child("time").getValue().equals(time) && data.child("treat_type").getValue().equals(tq.getType())) {
                        Queues_ref.child(data.getKey()).child("patient_id_attending").setValue("TBD");
                        queue_src_cancel(client_id, tq, context, date, time);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    public void queue_src_cancel(String client_id, TreatmentQueue tq, Context context, String date, String time) {
        queues_src_ref = mDatabase.getReference().child("Test_Queues_search");
        queues_src_ref.child("City").child(tq.getCity()).child("Treat_type").child(tq.getType()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data2 : snapshot.getChildren()
                ) {
                    if (data2.child("date").getValue().equals(date) && data2.child("time").getValue().equals(time) && data2.child("institute").getValue().equals(tq.getNameInstitute())) {
                        queues_src_ref.child("City").child(tq.getCity()).child("Treat_type").child(tq.getType()).child(data2.getKey()).child("patient_id_attending").setValue("TBD");
                        break;
                    }
                }
                queue_inst_cancel(client_id, tq, context,date,time);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void queue_inst_cancel(String client_id, TreatmentQueue tq, Context context,String date,String time) {
        queues_inst_ref = mDatabase.getReference().child("Test_Queues_institute");
        inst_ref = mDatabase.getReference().child("Institutes");
        inst_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data3 : snapshot.getChildren()
                )
                    if (data3.child("institute_name").getValue().equals(tq.getNameInstitute())) {
                        inst_id = data3.getKey();
                    }
                if (!inst_id.equals("")) {
                    queues_inst_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            queues_inst_ref.child(inst_id).child("Treat_type").child(tq.getType()).
                                    child(date2).child(time2).child("patient_id_attending").setValue("TBD");
                            Log.d("check", "cancelled in inst_ref");
                            Update_waiting_list(tq,date,time,context,client_id);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //return the first patient id on the waiting list and update the waiting list on all the tables
    private void Update_waiting_list(TreatmentQueue tq,String date,String time,Context context,String client_id) {
        String[] waiting_list={"TBD","TBD","TBD","TBD","TBD"};
        Queues_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data:snapshot.getChildren()
                ) {
                    if (data.child("institute").getValue().equals(tq.getNameInstitute())
                            && data.child("treat_type").getValue().equals(tq.getType()) &&
                            data.child("date").getValue().equals(date) && data.child("time").getValue().equals(time)) {
                        queue_id=data.getKey();
                        i = 0;
                        for (DataSnapshot data2 :
                                data.child("Waiting_list").getChildren()) {
                            if (!data2.getValue().equals("TBD"))
                                if (!isBooked) {
                                    client_to_notify = (String) data2.getValue();
                                    isBooked = true;
                                } else
                                    waiting_list[i++] = (String) data2.getValue();
                        }
                    }
                }
                pt_ref=FirebaseDatabase.getInstance().getReference("Patients").child(client_id).child("ActiveQueues").child("NumberOfWaitingQueues");
                pt_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        waiting_cnt=(String)snapshot.getValue();
                        if ((!client_to_notify.equals("TBD"))   &&   waiting_cnt.equals("0")) {
                            i = 0;
                            Queues_ref.child(queue_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (mutexQueues++ == 0) {
                                        for (DataSnapshot data2 : snapshot.child("Waiting_list").getChildren()
                                        ) {
                                            Queues_ref.child(queue_id).child("Waiting_list").child(data2.getKey()).setValue(waiting_list[i++]);
                                        }
                                    }
                                    queues_src_ref.child("City").child(tq.getCity()).child("Treat_type").child(tq.getType()).child(queue_id).child("Waiting_list").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (mutexSrc++== 0) {
                                                i = 0;
                                                for (DataSnapshot data3 : snapshot.getChildren()
                                                ) {
                                                    queues_src_ref.child("City").child(tq.getCity()).child("Treat_type").child(tq.getType()).child(queue_id).child("Waiting_list").child(data3.getKey()).setValue(waiting_list[i++]);
                                                }
                                            }
                                            queues_inst_ref.child(inst_id).child("Treat_type").child(tq.getType()).child(date2).child(time2).child("Waiting_list").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (mutexinst++ == 0) {
                                                        i = 0;
                                                        for (DataSnapshot data4 : snapshot.getChildren()
                                                        ) {
                                                            queues_inst_ref.child(inst_id).child("Treat_type").child(tq.getType()).child(date2).child(time2).child("Waiting_list").child(data4.getKey()).setValue(waiting_list[i++]);
                                                        }
                                                        update_new_Patient(client_to_notify, tq, context, false);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                        else printAndMove(client_id,"התור בוטל בהצלחה",context);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void add_to_waiting_list() {
        client_id=getIntent().getStringExtra("client_id");
        String position=getIntent().getStringExtra("position");
        Context context=this;
        chosen_queue=getIntent().getStringExtra("chosen_queue");
        parse_treatment(tq);
        mDatabase = FirebaseDatabase.getInstance();
        Queues_ref = mDatabase.getReference().child("Test_Queues");
        queues_src_ref = mDatabase.getReference().child("Test_Queues_search");
        queues_inst_ref = mDatabase.getReference().child("Test_Queues_institute");
        inst_ref = mDatabase.getReference().child("Institutes");
        mutexinst=0;
        mutexQueues=0;
        mutexSrc=0;
        int count1=0,count2=0;
        tempDay=tq.getDate().getDay();
        tempMonth=tq.getDate().getMonth();
        for (int i=0;i<tempDay.length();i++)
            count1++;
        for(int i=0;i<tempMonth.length();i++)
            count2++;
        if (count1==1)
            tempDay="0"+tempDay;
        if (count2==1)
            tempMonth="0"+tempMonth;
        String date = tempDay + "." + tempMonth + "." + String.valueOf(tq.getDate().getYear()).substring(2);
        String time = tq.getDate().getHour() + ":" + tq.getDate().getMinute();
        Queues_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(mutexQueues==0) {
                    mutexQueues++;
                    for (DataSnapshot data : snapshot.getChildren()
                    ) {
                        if (data.child("institute").getValue().equals(tq.getNameInstitute()) && data.child("date").getValue().equals(date)
                                && data.child("time").getValue().equals(time) && data.child("treat_type").getValue().equals(tq.getType())) {
                            Queues_ref.child(data.getKey()).child("Waiting_list").child(position).setValue(client_id);
                            break;
                        }
                    }
                }
                queues_src_ref.child("City").child(tq.getCity()).child("Treat_type").child(tq.getType()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(mutexSrc==0) {
                            mutexSrc++;
                            for (DataSnapshot data : snapshot.getChildren()
                            ) {
                                if (data.child("date").getValue().equals(date) && data.child("time").getValue().equals(time) && data.child("institute").getValue().equals(tq.getNameInstitute())) {
                                    queues_src_ref.child("City").child(tq.getCity()).child("Treat_type").child(tq.getType()).child(data.getKey()).child("Waiting_list").child(position).setValue(client_id);
                                    break;
                                }
                            }
                        }
                        inst_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (mutexinst == 0) {
                                    mutexinst++;
                                    for (DataSnapshot data : snapshot.getChildren()
                                    ) {
                                        if (data.child("institute_name").getValue().equals(tq.getNameInstitute())) {
                                            inst_id = data.getKey();
                                        }
                                    }
                                    if (!inst_id.equals("")) {
                                        queues_inst_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                queues_inst_ref.child(inst_id).child("Treat_type").child(tq.getType()).
                                                        child(tempDay + tempMonth + tq.getDate().getYear().substring(2))
                                                        .child(tq.getDate().getHour() + tq.getDate().getMinute()).child("Waiting_list").child(position).setValue(client_id);
                                                mDatabase.getReference("Patients").child(client_id).child("ActiveQueues").child("NumberOfWaitingQueues").setValue("1");
                                                Toast.makeText(context, "נכנסת בהצלחה לרשימת ההמתנה", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(context, com.example.dimot_bekalot.clientActivities.Main_Client_View.class);
                                                intent.putExtra("client_id", client_id);
                                                context.startActivity(intent);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void printAndMove(String client_id,String msg,Context context)
    {

        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        if(STOP_RUN==0) {
            STOP_RUN=1;
            Intent BackIntent = new Intent(context, com.example.dimot_bekalot.clientActivities.Main_Client_View.class);
            BackIntent.putExtra("client_id", client_id);
            context.startActivity(BackIntent);
        }
    }

    public void parse_treatment(TreatmentQueue tq) {
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
        tq.setCity(type);
        tq.setDate(date);
        tq.setNameInstitute(city);
        tq.setIdPatient(client_id);
    }
}
