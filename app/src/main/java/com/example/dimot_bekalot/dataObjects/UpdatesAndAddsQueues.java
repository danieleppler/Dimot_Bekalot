package com.example.dimot_bekalot.dataObjects;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdatesAndAddsQueues {
    String id_patient, id_institute, date, hour, type;
    private String city = "", numOfQueue = "", nameInstitute = "";

    private FirebaseDatabase dataBase;
    private DatabaseReference ref_QueuesInstitute, ref_QueuesSearch, ref_Queues;
    private DatabaseReference ref_institute;
    private final String queues = "Queues";
    private final String queues_institute = "Queues_institute";
    private final String queues_search = "Queues_search";
    private final String institute = "Institutes";


    public UpdatesAndAddsQueues(String id_institute, String id_patient ,String date ,String hour, String type){
        this.id_institute = id_institute;
        this.id_patient = id_patient;
        this.date = date;
        this.hour = hour;
        this.type = type;
    }

    public void update(){
        dataBase = FirebaseDatabase.getInstance();
        ref_QueuesInstitute = dataBase.getReference(queues_institute);
        ref_Queues = dataBase.getReference(queues);
        ref_QueuesSearch = dataBase.getReference(queues_search);
        ref_institute = dataBase.getReference(institute);

        ref_institute.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                city = snapshot.child(id_institute).child("address").child("city_Name").getValue().toString();
                nameInstitute = snapshot.child(id_institute).child("institute_name").getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        ref_QueuesSearch.child("City").child(city)
                .child("treat_type").child(type).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder newDate = new StringBuilder(date);
                newDate.insert(2, ".");
                newDate.insert(5, ".");

                StringBuilder newHour = new StringBuilder(hour);
                newHour.insert(2, ":");

                for(DataSnapshot data : snapshot.getChildren()){
                    if(data.child("date").getValue().toString().equals(newDate.toString()) &&
                            data.child("time").getValue().toString().equals(newHour.toString())){
                        numOfQueue = data.getKey().toString();
                        break;
                    }
                }
                ref_QueuesSearch.child(numOfQueue).child("Patient_id_attending").setValue(id_patient);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        ref_QueuesInstitute.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ref_QueuesInstitute.child(id_institute).child("Treat_type")
                            .child(type).child(date).child(hour)
                            .child("Patient_id_attending").setValue(id_patient);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        ref_Queues.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref_Queues.child(numOfQueue).child("Patient_id_attending").setValue(id_patient);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void add(){
        dataBase = FirebaseDatabase.getInstance();
        ref_QueuesInstitute = dataBase.getReference(queues_institute);
        ref_Queues = dataBase.getReference(queues);
        ref_QueuesSearch = dataBase.getReference(queues_search);
        ref_institute = dataBase.getReference(institute);

        ref_institute.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 city = snapshot.child(id_institute).child("address").child("city_Name").getValue().toString();
                 nameInstitute = snapshot.child(id_institute).child("institute_name").getValue().toString();
             }
             @Override
             public void onCancelled(@NonNull DatabaseError error) {}
        });
            ref_QueuesSearch.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    StringBuilder newDate = new StringBuilder(date);
                    newDate.insert(2, ".");
                    newDate.insert(5, ".");

                    StringBuilder newHour = new StringBuilder(hour);
                    newHour.insert(2, ":");

                    numOfQueue = ref_QueuesSearch.push().getKey().toString();
                    Log.d("number => ", numOfQueue);

                    ref_QueuesSearch.setValue("City");
                    ref_QueuesSearch.child("City").setValue(city);
                    ref_QueuesSearch.child("City").child(city).setValue("treat_type");
                    ref_QueuesSearch.child("City").child(city).child("treat_type").setValue(type);
                    ref_QueuesSearch.child("City").child(city).child("treat_type").child(type).setValue(numOfQueue);


                    ref_QueuesSearch.child("City").child(city).child("treat_type").child(type).child(numOfQueue).setValue("Patient_id_attending");
                    ref_QueuesSearch.child("City").child(city).child("treat_type").child(type).child(numOfQueue).setValue("date");
                    ref_QueuesSearch.child("City").child(city).child("treat_type").child(type).child(numOfQueue).setValue("institute");
                    ref_QueuesSearch.child("City").child(city).child("treat_type").child(type).child(numOfQueue).setValue("time");

                    ref_QueuesSearch.child("City").child(city).child("treat_type").child(type)
                            .child(numOfQueue).child("Patient_id_attending").setValue(id_patient);
                    ref_QueuesSearch.child("City").child(city).child("treat_type").child(type)
                            .child(numOfQueue).child("date").setValue(newDate.toString());
                    ref_QueuesSearch.child("City").child(city).child("treat_type").child(type)
                            .child(numOfQueue).child("institute").setValue(nameInstitute);
                    ref_QueuesSearch.child("City").child(city).child("treat_type").child(type)
                            .child(numOfQueue).child("time").setValue(newHour.toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        ref_QueuesInstitute.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref_QueuesInstitute.setValue(id_institute);
                ref_QueuesInstitute.child(id_institute).setValue("Treat_type");
                ref_QueuesInstitute.child(id_institute).child("Treat_type").setValue(type);
                ref_QueuesInstitute.child(id_institute).child("Treat_type").child(type).setValue(date);
                ref_QueuesInstitute.child(id_institute).child("Treat_type").child(type).child(date).setValue(hour);

                ref_QueuesInstitute.child(id_institute).child("Treat_type")
                        .child(type).child(date).child(hour).setValue("Patient_id_attending");
                ref_QueuesInstitute.child(id_institute).child("Treat_type")
                        .child(type).child(date).child(hour).child("Patient_id_attending").setValue(id_patient);

                ref_QueuesInstitute.child(id_institute).child("Treat_type")
                        .child(type).child(date).child(hour).setValue("number_queue");
                ref_QueuesInstitute.child(id_institute).child("Treat_type")
                        .child(type).child(date).child(hour).child("number_queue").setValue(numOfQueue);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        ref_Queues.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ref_Queues.setValue(numOfQueue);
                ref_Queues.child(numOfQueue).setValue("Patient_id_attending");
                ref_Queues.child(numOfQueue).setValue("city");
                ref_Queues.child(numOfQueue).setValue("date");
                ref_Queues.child(numOfQueue).setValue("institute");
                ref_Queues.child(numOfQueue).setValue("time");
                ref_Queues.child(numOfQueue).setValue("treat_type");

                StringBuilder newDate = new StringBuilder(date);
                newDate.insert(2, ".");
                newDate.insert(5, ".");

                StringBuilder newHour = new StringBuilder(hour);
                newHour.insert(2, ":");

                ref_Queues.child(numOfQueue).child("Patient_id_attending").setValue(id_patient);
                ref_Queues.child(numOfQueue).child("city").setValue(id_patient);
                ref_Queues.child(numOfQueue).child("date").setValue(newDate.toString());
                ref_Queues.child(numOfQueue).child("institute").setValue(nameInstitute);
                ref_Queues.child(numOfQueue).child("time").setValue(newHour.toString());
                ref_Queues.child(numOfQueue).child("treat_type").setValue(type);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

}
