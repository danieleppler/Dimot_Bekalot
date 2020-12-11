package com.example.dimot_bekalot.dataObjects;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Update_Queues {

    FirebaseDatabase mDatabase;
    DatabaseReference Queues_ref,queues_src_ref,queues_inst_ref,inst_ref;
    private static final String TAG = "Update_Queues";
    String inst_id="";
    public Update_Queues()
    {

    }
    public  boolean update_new_Patient(String client_id,TreatmentQueue tq)
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
                    Log.d(TAG,"CHECKING IF "+data.child("institute").getValue()+" == "+(tq.getNameInstitute())+" AND "+data.child("date").getValue()+" == "+date
                            +" AND "+ data.child("time").getValue()+" == "+time+" AND "+data.child("treat_type").getValue()+" == "+tq.getType());
                    if (data.child("institute").getValue().equals(tq.getNameInstitute())&&data.child("date").getValue().equals(date)
                    &&data.child("time").getValue().equals(time)&&data.child("treat_type").getValue().equals(tq.getType())) {
                        Queues_ref.child(data.getKey()).child("Patient_id_attending").setValue(client_id);
                        Log.d("TAG","booked in Queues");
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d(TAG,"about to check kinon for "+tq.getCity()+" "+tq.getType());
        queues_src_ref.child("City").child(tq.getCity()).child("treat_type").child(tq.getType()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data:snapshot.getChildren()
                ) {
                    Log.d(TAG,"checking if "+data.child("date").getValue()+" is equal to "+date+" and if "+data.child("time")
                    .getValue()+" is queal to "+time);
                    if (data.child("date").getValue().equals(date) && data.child("time").getValue().equals(time) && data.child("institute").getValue().equals(tq.getNameInstitute()))  {
                        queues_src_ref.child("City").child(tq.getCity()).child("treat_type").child(tq.getType()).child(data.getKey()).child("Patient_id_attending").setValue(client_id);
                        Log.d("TAG","booked in Queues_search");
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
                        Log.d(TAG,"GOT INST ID! IS: "+ inst_id);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        queues_inst_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                queues_inst_ref.child(inst_id).child("Treat_type").child(tq.getType()).
                        child(tq.getDate().getDay()+tq.getDate().getMonth()+tq.getDate().getYear().substring(2))
                        .child(tq.getDate().getHour()+tq.getDate().getMinute()).child("Patient_id_attending").setValue(client_id);
                Log.d("TAG","booked in Queues_inst");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return true;
    }

    public  boolean cancel_patient(String client_id,TreatmentQueue tq)
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
                    Log.d(TAG,"CHECKING IF "+data.child("institute").getValue()+" == "+(tq.getNameInstitute())+" AND "+data.child("date").getValue()+" == "+date
                            +" AND "+ data.child("time").getValue()+" == "+time+" AND "+data.child("treat_type").getValue()+" == "+tq.getType());
                    if (data.child("institute").getValue().equals(tq.getNameInstitute())&&data.child("date").getValue().equals(date)
                            &&data.child("time").getValue().equals(time)&&data.child("treat_type").getValue().equals(tq.getType())) {
                        Queues_ref.child(data.getKey()).child("Patient_id_attending").setValue("TBD");
                        Log.d("TAG","booked in Queues");
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d(TAG,"about to check kinon for "+tq.getCity()+" "+tq.getType());
        queues_src_ref.child("City").child(tq.getCity()).child("treat_type").child(tq.getType()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data:snapshot.getChildren()
                ) {
                    if (data.child("date").getValue().equals(date) && data.child("time").getValue().equals(time) && data.child("institute").getValue().equals(tq.getNameInstitute())) {
                        queues_src_ref.child("City").child(tq.getCity()).child("treat_type").child(tq.getType()).child(data.getKey()).child("Patient_id_attending").setValue("TBD");
                        Log.d("TAG","booked in Queues_search");
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
                        Log.d(TAG,"GOT INST ID! IS: "+ inst_id);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        queues_inst_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                queues_inst_ref.child(inst_id).child("Treat_type").child(tq.getType()).
                        child(tq.getDate().getDay()+tq.getDate().getMonth()+tq.getDate().getYear().substring(2))
                        .child(tq.getDate().getHour()+tq.getDate().getMinute()).child("Patient_id_attending").setValue("TBD");
                Log.d("TAG","booked in Queues_inst");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return true;
    }
}
