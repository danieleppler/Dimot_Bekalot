package com.example.dimot_bekalot.dataObjects;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dimot_bekalot.SendNotificationPack.APIService;
import com.example.dimot_bekalot.SendNotificationPack.ApiInterface;
import com.example.dimot_bekalot.SendNotificationPack.Client;
import com.example.dimot_bekalot.SendNotificationPack.Data;
import com.example.dimot_bekalot.SendNotificationPack.MyNotification;
import com.example.dimot_bekalot.SendNotificationPack.RootModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import okhttp3.ResponseBody;
//import retrofit2.Callback;


public class Update_Queues {

    FirebaseDatabase mDatabase;
    DatabaseReference Queues_ref, queues_src_ref, queues_inst_ref, inst_ref;
    private static final String TAG = "Update_Queues";
    String inst_id = "";

    String token;

    boolean isBooked=false;

    int i=0;

    String client_to_notify="";
    Boolean toUpdate=false;
    String queue_id;

    public Update_Queues() {}

    public void update_new_Patient(String client_id, TreatmentQueue tq, Context context,Boolean toPrint) {
        mDatabase = FirebaseDatabase.getInstance();
        Queues_ref = mDatabase.getReference().child("Test_Queues");
        queues_src_ref = mDatabase.getReference().child("Test_Queues_search");
        queues_inst_ref = mDatabase.getReference().child("Test_Queues_institute");
        inst_ref = mDatabase.getReference().child("Institutes");
        String date = tq.getDate().getDay() + "." + tq.getDate().getMonth() + "." + String.valueOf(tq.getDate().getYear()).substring(2);
        String time = tq.getDate().getHour() + ":" + tq.getDate().getMinute();
        update_new_in_queues(tq,date,time,client_id,toPrint,context);
    }

    private void update_new_in_queues(TreatmentQueue tq, String date, String time, String client_id, Boolean toPrint, Context context) {
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
        queues_src_ref.child("City").child(tq.getCity()).child("Treat_type").child(tq.getType()).addValueEventListener(new ValueEventListener() {
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
        inst_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()
                )
                    if (data.child("institute_name").getValue().equals(tq.getNameInstitute())) {
                        inst_id = data.getKey();
                    }
                if (!inst_id.equals("")) {
                    int count1=0,count2=0;
                    String tempDay=tq.getDate().getDay();
                    String tempMonth=tq.getDate().getMonth();
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
                    queues_inst_ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            queues_inst_ref.child(inst_id).child("Treat_type").child(tq.getType()).
                                    child(tq.getDate().getDay() + tq.getDate().getMonth() + tq.getDate().getYear().substring(2))
                                    .child(tq.getDate().getHour() + tq.getDate().getMinute()).child("patient_id_attending").setValue(client_id);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                if(toPrint)
                   printAndMove(client_id,"התור נקבע בהצלחה",context);
                else printAndMove(client_id,"התור בוטל בהצלחה",context);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void cancel_patient(String client_id, TreatmentQueue tq, Context context, String toChange) {
        mDatabase = FirebaseDatabase.getInstance();

        String date = tq.getDate().getDay() + "." + tq.getDate().getMonth() + "." + String.valueOf(tq.getDate().getYear()).substring(2);
        String time = tq.getDate().getHour() + ":" + tq.getDate().getMinute();
        Queues_ref = mDatabase.getReference().child("Queues");
        queues_cancel(client_id, tq, context, date, time, toChange);

    }

    private void queues_cancel(String client_id, TreatmentQueue tq, Context context, String date, String time, String toChange) {
        Queues_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()
                ) {
                    if (data.child("institute").getValue().equals(tq.getNameInstitute()) && data.child("date").getValue().equals(date)
                            && data.child("time").getValue().equals(time) && data.child("treat_type").getValue().equals(tq.getType())) {
                        Queues_ref.child(data.getKey()).child("patient_id_attending").setValue(toChange);
                        Log.d("check", "cancelled in queues");
                        queue_src_cancel(client_id, tq, context, date, time, toChange);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void queue_src_cancel(String client_id, TreatmentQueue tq, Context context, String date, String time, String toChange) {
        queues_src_ref = mDatabase.getReference().child("Test_Queues_search");
        queues_src_ref.child("City").child(tq.getCity()).child("Treat_type").child(tq.getType()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data2 : snapshot.getChildren()
                ) {
                    if (data2.child("date").getValue().equals(date) && data2.child("time").getValue().equals(time) && data2.child("institute").getValue().equals(tq.getNameInstitute())) {
                        queues_src_ref.child("City").child(tq.getCity()).child("Treat_type").child(tq.getType()).child(data2.getKey()).child("patient_id_attending").setValue(toChange);
                        Log.d("check", "cancelled in queue_src");
                        queue_inst_cancel(client_id, tq, context, date, time, toChange);
                        break;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void queue_inst_cancel(String client_id, TreatmentQueue tq, Context context, String date, String time, String toChange) {
        queues_inst_ref = mDatabase.getReference().child("Test_Queues_institute");
        inst_ref = mDatabase.getReference().child("Institutes");
        inst_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data3 : snapshot.getChildren()
                )
                    if (data3.child("institute_name").getValue().equals(tq.getNameInstitute())) {
                        inst_id = data3.getKey();
                    }
                if (inst_id != "") {
                    int count1=0,count2=0;
                    String tempDay=tq.getDate().getDay();
                    String tempMonth=tq.getDate().getMonth();
                    for (int i=0;i<tempDay.length();i++)
                        count1++;
                    for(int i=0;i<tempMonth.length();i++)
                        count2++;
                    if (count1==1)
                        tempDay="0"+tempDay;
                    if (count2==1)
                        tempMonth="0"+tempMonth;
                    String date2 = tempDay + tempMonth + String.valueOf(tq.getDate().getYear()).substring(2);
                    String time2 = tq.getDate().getHour() +tq.getDate().getMinute();
                    queues_inst_ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            queues_inst_ref.child(inst_id).child("Treat_type").child(tq.getType()).
                                    child(date2).child(time2).child("patient_id_attending").setValue(toChange);
                            Log.d("check", "cancelled in inst_ref");
                            Update_waiting_list(tq, date, time, context, client_id);
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

        Queues_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data:snapshot.getChildren()
                     ) {
                    Log.d(TAG, data.getValue().toString());
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
                if (!client_to_notify.equals("TBD")) {
                    toUpdate=true;
                    i = 0;
                    Queues_ref.child(queue_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot data2 : snapshot.child("Waiting_list").getChildren()
                            ) {
                                Queues_ref.child(queue_id).child("Waiting_list").child(data2.getKey()).setValue(waiting_list[i++]);
                            }
                            i=0;
                            queues_src_ref.child("City").child(tq.getCity()).child("Treat_type").child(tq.getType()).child(queue_id).child("Waiting_list").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot data3:snapshot.getChildren()
                                         ) {
                                        queues_src_ref.child("City").child(tq.getCity()).child("Treat_type").child(tq.getType()).child(queue_id).child("Waiting_list").child(data3.getKey()).setValue(waiting_list[i++]);
                                    }
                                    i=0;
                                    int count1=0,count2=0;
                                    String tempDay=tq.getDate().getDay();
                                    String tempMonth=tq.getDate().getMonth();
                                    for (int i=0;i<tempDay.length();i++)
                                        count1++;
                                    for(int i=0;i<tempMonth.length();i++)
                                        count2++;
                                    if (count1==1)
                                        tempDay="0"+tempDay;
                                    if (count2==1)
                                        tempMonth="0"+tempMonth;
                                    String date2 = tempDay + "." + tempMonth + "." + String.valueOf(tq.getDate().getYear()).substring(2);
                                    String time2 = tq.getDate().getHour() + ":" + tq.getDate().getMinute();
                                    queues_inst_ref.child(inst_id).child("Treat_type").child(tq.getType()).child(date2).child(time2).child("Waiting_list").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot data4:snapshot.getChildren()
                                                 ) {
                                                queues_inst_ref.child(inst_id).child("Treat_type").child(tq.getType()).child(date2).child(time2).child("Waiting_list").child(data4.getKey()).setValue(waiting_list[i++]);
                                            }
                                            if(toUpdate)
                                                update_new_Patient(client_to_notify, tq, context, false);
                                            else
                                                printAndMove(client_id,"התור בוטל בהצלחה",context);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void add_to_waiting_list(String client_id, TreatmentQueue tq, String position, Context context) {
        mDatabase = FirebaseDatabase.getInstance();
        Queues_ref = mDatabase.getReference().child("Test_Queues");
        queues_src_ref = mDatabase.getReference().child("Test_Queues_search");
        queues_inst_ref = mDatabase.getReference().child("Test_Queues_institute");
        inst_ref = mDatabase.getReference().child("Institutes");
        int count1=0,count2=0;
        String tempDay=tq.getDate().getDay();
        String tempMonth=tq.getDate().getMonth();
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
                for (DataSnapshot data : snapshot.getChildren()
                ) {
                    if (data.child("institute").getValue().equals(tq.getNameInstitute()) && data.child("date").getValue().equals(date)
                            && data.child("time").getValue().equals(time) && data.child("treat_type").getValue().equals(tq.getType())) {
                        Queues_ref.child(data.getKey()).child("Waiting_list").child(position).setValue(client_id);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        queues_src_ref.child("City").child(tq.getCity()).child("Treat_type").child(tq.getType()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()
                ) {
                    if (data.child("date").getValue().equals(date) && data.child("time").getValue().equals(time) && data.child("institute").getValue().equals(tq.getNameInstitute())) {
                        queues_src_ref.child("City").child(tq.getCity()).child("Treat_type").child(tq.getType()).child(data.getKey()).child("Waiting_list").child(position).setValue(client_id);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        inst_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()
                )
                    if (data.child("institute_name").getValue().equals(tq.getNameInstitute())) {
                        inst_id = data.getKey();
                    }
                if (inst_id != "") {
                    queues_inst_ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            queues_inst_ref.child(inst_id).child("Treat_type").child(tq.getType()).
                                    child(tq.getDate().getDay() + tq.getDate().getMonth() + tq.getDate().getYear().substring(2))
                                    .child(tq.getDate().getHour() + tq.getDate().getMinute()).child("Waiting_list").child(position).setValue(client_id);
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

        Toast.makeText(context, "נכנסת בהצלחה לרשימת ההמתנה", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(context, com.example.dimot_bekalot.clientActivities.Main_Client_View.class);
        intent.putExtra("client_id", client_id);
        context.startActivity(intent);
    }

    private void printAndMove(String client_id,String msg,Context context)
    {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(context, com.example.dimot_bekalot.clientActivities.Main_Client_View.class);
        intent.putExtra("client_id", client_id);
        context.startActivity(intent);
    }
}
