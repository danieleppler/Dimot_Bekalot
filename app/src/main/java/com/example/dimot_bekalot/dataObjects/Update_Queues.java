package com.example.dimot_bekalot.dataObjects;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dimot_bekalot.SendNotificationPack.APIService;
import com.example.dimot_bekalot.SendNotificationPack.Client;
import com.example.dimot_bekalot.SendNotificationPack.Data;
import com.example.dimot_bekalot.SendNotificationPack.MyResponse;
import com.example.dimot_bekalot.SendNotificationPack.NotificationSender;
import com.google.android.gms.common.api.Response;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;


public class Update_Queues {

    FirebaseDatabase mDatabase;
    DatabaseReference Queues_ref, queues_src_ref, queues_inst_ref, inst_ref;
    private static final String TAG = "Update_Queues";
    String inst_id="";
    String token;
    APIService apiService;

    public Update_Queues()
    {
    }

    public  void update_new_Patient(String client_id, TreatmentQueue tq, Context context)
    {
        mDatabase=FirebaseDatabase.getInstance();
        Queues_ref=mDatabase.getReference().child("Queues");
        queues_src_ref=mDatabase.getReference().child("Queues_search");
        queues_inst_ref=mDatabase.getReference().child("Queues_institute");
        inst_ref=mDatabase.getReference().child("Institutes");
        String date=tq.getDate().getDay()+"."+tq.getDate().getMonth()+"."+String.valueOf(tq.getDate().getYear()).substring(2);
        String time=tq.getDate().getHour()+":"+tq.getDate().getMinute();
        Queues_ref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data:snapshot.getChildren()
                ) {
                    if (data.child("institute").getValue().equals(tq.getNameInstitute())&&data.child("date").getValue().equals(date)
                    &&data.child("time").getValue().equals(time)&&data.child("treat_type").getValue().equals(tq.getType())) {
                        Queues_ref.child(data.getKey()).child("patient_id_attending").setValue(client_id);
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
                for (DataSnapshot data:snapshot.getChildren()
                ) {
                    if (data.child("date").getValue().equals(date) && data.child("time").getValue().equals(time) && data.child("institute").getValue().equals(tq.getNameInstitute()))  {
                        queues_src_ref.child("City").child(tq.getCity()).child("Treat_type").child(tq.getType()).child(data.getKey()).child("patient_id_attending").setValue(client_id);
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
                for (DataSnapshot data:snapshot.getChildren()
                )
                    if (data.child("institute_name").getValue().equals(tq.getNameInstitute())) {
                        inst_id =data.getKey();
                    }
                if(inst_id!="")
                {
                    queues_inst_ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            queues_inst_ref.child(inst_id).child("Treat_type").child(tq.getType()).
                                    child(tq.getDate().getDay()+tq.getDate().getMonth()+tq.getDate().getYear().substring(2))
                                    .child(tq.getDate().getHour()+tq.getDate().getMinute()).child("patient_id_attending").setValue(client_id);
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
        Toast.makeText( context, "booked successfully", Toast.LENGTH_LONG).show();
        Intent intent=new Intent(context,com.example.dimot_bekalot.clientActivities.Main_Client_View.class);
        intent.putExtra("client_id",client_id);
        context.startActivity(intent);
    }

    public  void cancel_patient(String client_id,TreatmentQueue tq,Context context)
    {
        mDatabase=FirebaseDatabase.getInstance();
        Queues_ref=mDatabase.getReference().child("Queues");
        queues_src_ref=mDatabase.getReference().child("Queues_search");
        queues_inst_ref=mDatabase.getReference().child("Queues_institute");
        inst_ref=mDatabase.getReference().child("Institutes");
        String date=tq.getDate().getDay()+"."+tq.getDate().getMonth()+"."+String.valueOf(tq.getDate().getYear()).substring(2);
        String time=tq.getDate().getHour()+":"+tq.getDate().getMinute();

        Queues_ref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data:snapshot.getChildren()
                ) {
                    if (data.child("institute").getValue().equals(tq.getNameInstitute())&&data.child("date").getValue().equals(date)
                            &&data.child("time").getValue().equals(time)&&data.child("treat_type").getValue().equals(tq.getType())) {
                        Queues_ref.child(data.getKey()).child("patient_id_attending").setValue("TBD");
                        Log.d("check","booked in queues");
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
                for (DataSnapshot data:snapshot.getChildren()
                ) {
                    if (data.child("date").getValue().equals(date) && data.child("time").getValue().equals(time) && data.child("institute").getValue().equals(tq.getNameInstitute())) {
                        queues_src_ref.child("City").child(tq.getCity()).child("Treat_type").child(tq.getType()).child(data.getKey()).child("patient_id_attending").setValue("TBD");
                        Log.d("check","booked in queue_src");
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
                for (DataSnapshot data:snapshot.getChildren()
                )
                    if (data.child("institute_name").getValue().equals(tq.getNameInstitute())) {
                        inst_id =data.getKey();
                    }
                if(inst_id!="") {
                    queues_inst_ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            queues_inst_ref.child(inst_id).child("Treat_type").child(tq.getType()).
                                    child(tq.getDate().getDay() + tq.getDate().getMonth() + tq.getDate().getYear().substring(2))
                                    .child(tq.getDate().getHour() + tq.getDate().getMinute()).child("patient_id_attending").setValue("TBD");
                            Log.d("check","booked in inst_ref");
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
        String sendNotficTo="p:121212121";//debug

        FirebaseDatabase.getInstance().getReference().child("Patients").child(sendNotficTo).child("token").
                addValueEventListener(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                                              token= (String) snapshot.child("token").getValue();
                                              Log.d(TAG,token);
                                              apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
                                              Data data=new Data("title","hello");
                                              NotificationSender sender = new NotificationSender(data,token );
                                              apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
                                                  @Override
                                                  public void onResponse(Call<MyResponse> call, retrofit2.Response<MyResponse> response) {
                                                      if (response.code() == 200) {
                                                          if (response.body().success != 1) {
                                                              Toast.makeText(context, "Failed ", Toast.LENGTH_LONG);
                                                          }
                                                      }
                                                  }
                                                  @Override
                                                  public void onFailure(Call<MyResponse> call, Throwable t) {

                                                  }
                                              });
                                          }

                                          @Override
                                          public void onCancelled(@NonNull DatabaseError error) {

                                          }
                                      });

        /*
         need to find the first in the list on real time

         */

        //need to update list
        Toast.makeText( context, "התור בוטל בהצלחה", Toast.LENGTH_LONG).show();
        Intent intent=new Intent(context,com.example.dimot_bekalot.clientActivities.Main_Client_View.class);
        intent.putExtra("client_id",client_id);
        context.startActivity(intent);
    }

    public void update_waiting_list(String client_id,TreatmentQueue tq,String position,Context context)
    {
        mDatabase=FirebaseDatabase.getInstance();
        Queues_ref=mDatabase.getReference().child("Queues");
        queues_src_ref=mDatabase.getReference().child("Queues_search");
        queues_inst_ref=mDatabase.getReference().child("Queues_institute");
        inst_ref=mDatabase.getReference().child("Institutes");
        String date="0"+tq.getDate().getDay()+"."+"0"+tq.getDate().getMonth()+"."+String.valueOf(tq.getDate().getYear()).substring(2);
        String time=tq.getDate().getHour()+":"+tq.getDate().getMinute();
        Queues_ref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data:snapshot.getChildren()
                ) {
                    if (data.child("institute").getValue().equals(tq.getNameInstitute())&&data.child("date").getValue().equals(date)
                            &&data.child("time").getValue().equals(time)&&data.child("treat_type").getValue().equals(tq.getType())) {
                        Queues_ref.child(data.getKey()).child("waiting_list").child(position).setValue(client_id);
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
                for (DataSnapshot data:snapshot.getChildren()
                ) {
                    if (data.child("date").getValue().equals(date) && data.child("time").getValue().equals(time) && data.child("institute").getValue().equals(tq.getNameInstitute()))  {
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
                for (DataSnapshot data:snapshot.getChildren()
                )
                    if (data.child("institute_name").getValue().equals(tq.getNameInstitute())) {
                        inst_id =data.getKey();
                    }
                if(inst_id!="")
                {
                    queues_inst_ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            queues_inst_ref.child(inst_id).child("Treat_type").child(tq.getType()).
                                    child(tq.getDate().getDay()+tq.getDate().getMonth()+tq.getDate().getYear().substring(2))
                                    .child(tq.getDate().getHour()+tq.getDate().getMinute()).child("Waiting_list").child(position).setValue(client_id);
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
        Toast.makeText( context, "נכנסת בהצלחה לרשימת ההמתנה", Toast.LENGTH_LONG).show();
        Intent intent=new Intent(context,com.example.dimot_bekalot.clientActivities.Main_Client_View.class);
        intent.putExtra("client_id",client_id);
        context.startActivity(intent);
    }
}
