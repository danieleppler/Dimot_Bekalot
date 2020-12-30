package com.example.dimot_bekalot.dataObjects;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dimot_bekalot.Tools.Strings_Tools;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class UpdatesAndAddsQueues implements Serializable {
    private String id_patient, id_institute, date, hour, type;
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

        String patientIdToDB = id_patient;

        ref_institute.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                city = snapshot.child(id_institute).child("address").child("city_Name").getValue().toString();
                nameInstitute = snapshot.child(id_institute).child("institute_name").getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        ref_Queues.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder newDate = new StringBuilder(date);
                newDate.insert(2, ".");
                newDate.insert(5, ".");
                String newHour = Strings_Tools.createNOTCleanUserName(hour, 2, ":");

                for(DataSnapshot data : snapshot.getChildren()){
                    if( data.child("date").getValue().toString().equals(newDate.toString()) &&
                            data.child("time").getValue().toString().equals(newHour) ){
                        numOfQueue = data.getKey();
                        break;
                    }
                }
                ref_Queues.child(numOfQueue).child("patient_id_attending").setValue(patientIdToDB);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        ref_QueuesSearch.child("City").child(city)
                .child("treat_type").child(type).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref_QueuesSearch.child(numOfQueue).child("patient_id_attending").setValue(patientIdToDB);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        ref_QueuesInstitute.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ref_QueuesInstitute.child(id_institute).child("Treat_type")
                            .child(type).child(date).child(hour)
                            .child("patient_id_attending").setValue(patientIdToDB);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }


    public void add(int number){
        dataBase = FirebaseDatabase.getInstance();
        ref_QueuesInstitute = dataBase.getReference(queues_institute);
        ref_Queues = dataBase.getReference(queues);
        ref_QueuesSearch = dataBase.getReference(queues_search);
        ref_institute = dataBase.getReference(institute);

        StringBuilder newDate = new StringBuilder(date);
        newDate.insert(2, ".");
        newDate.insert(5, ".");
        String newHour = Strings_Tools.createNOTCleanUserName(hour, 2, ":");
        String[] _numberOfQueue = new String[1];
        String patientIdToDB = id_patient;

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
                numOfQueue = ref_QueuesSearch.push().getKey();
                _numberOfQueue[0] = numOfQueue;

                if (!snapshot.child("City").exists()) {
                    ref_QueuesSearch.setValue("City");
                }
                if (!snapshot.child("City").child(city).exists()){
                    ref_QueuesSearch.child("City").setValue(city);
                }
                if(!snapshot.child("City").child(city).child("Treat_type").exists()) {
                    ref_QueuesSearch.child("City").child(city).setValue("Treat_type");
                }
                if(!snapshot.child("City").child(city).child("Treat_type").child(type).exists()) {
                    ref_QueuesSearch.child("City").child(city).child("Treat_type").setValue(type);
                }

                ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).setValue(numOfQueue);

                ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).setValue("patient_id_attending");
                ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).setValue("date");
                ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).setValue("institute");
                ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).setValue("time");
                ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).setValue("Waiting_list");

                ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("patient_id_attending").setValue(patientIdToDB);
                ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("date").setValue(newDate.toString());
                ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("institute").setValue(nameInstitute);
                ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("time").setValue(newHour);

                ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("Waiting_list").setValue("patient_1");
                ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("Waiting_list").setValue("patient_2");
                ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("Waiting_list").setValue("patient_3");
                ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("Waiting_list").setValue("patient_4");
                ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("Waiting_list").setValue("patient_5");
                ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("Waiting_list").child("patient_1").setValue("TBD");
                ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("Waiting_list").child("patient_2").setValue("TBD");
                ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("Waiting_list").child("patient_3").setValue("TBD");
                ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("Waiting_list").child("patient_4").setValue("TBD");
                ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("Waiting_list").child("patient_5").setValue("TBD");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        ref_Queues.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref_Queues.setValue(numOfQueue);

                ref_Queues.child(numOfQueue).setValue("patient_id_attending");
                ref_Queues.child(numOfQueue).setValue("city");
                ref_Queues.child(numOfQueue).setValue("date");
                ref_Queues.child(numOfQueue).setValue("institute");
                ref_Queues.child(numOfQueue).setValue("time");
                ref_Queues.child(numOfQueue).setValue("treat_type");
                ref_Queues.child(numOfQueue).setValue("Waiting_list");

                ref_Queues.child(numOfQueue).child("patient_id_attending").setValue(patientIdToDB);
                ref_Queues.child(numOfQueue).child("city").setValue(city);
                ref_Queues.child(numOfQueue).child("date").setValue(newDate.toString());
                ref_Queues.child(numOfQueue).child("institute").setValue(nameInstitute);
                ref_Queues.child(numOfQueue).child("time").setValue(newHour);
                ref_Queues.child(numOfQueue).child("treat_type").setValue(type);

                ref_Queues.child(numOfQueue).child("Waiting_list").setValue("patient_1");
                ref_Queues.child(numOfQueue).child("Waiting_list").setValue("patient_2");
                ref_Queues.child(numOfQueue).child("Waiting_list").setValue("patient_3");
                ref_Queues.child(numOfQueue).child("Waiting_list").setValue("patient_4");
                ref_Queues.child(numOfQueue).child("Waiting_list").setValue("patient_5");
                ref_Queues.child(numOfQueue).child("Waiting_list").child("patient_1").setValue("TBD");
                ref_Queues.child(numOfQueue).child("Waiting_list").child("patient_2").setValue("TBD");
                ref_Queues.child(numOfQueue).child("Waiting_list").child("patient_3").setValue("TBD");
                ref_Queues.child(numOfQueue).child("Waiting_list").child("patient_4").setValue("TBD");
                ref_Queues.child(numOfQueue).child("Waiting_list").child("patient_5").setValue("TBD");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        ref_QueuesInstitute.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(1 == number){
                    ref_QueuesInstitute.setValue(id_institute);
                    ref_QueuesInstitute.child(id_institute).setValue("Treat_type");
                    ref_QueuesInstitute.child(id_institute).child("Treat_type").setValue(type);
                    ref_QueuesInstitute.child(id_institute).child("Treat_type").child(type).setValue(date);
                }
                if(2 == number){
                    ref_QueuesInstitute.child(id_institute).child("Treat_type").setValue(type);
                    ref_QueuesInstitute.child(id_institute).child("Treat_type").child(type).setValue(date);
                }
                if(3 == number) {
                    ref_QueuesInstitute.child(id_institute).child("Treat_type").child(type).setValue(date);
                }

                ref_QueuesInstitute.child(id_institute).child("Treat_type").child(type).child(date).setValue(hour);

                ref_QueuesInstitute.child(id_institute).child("Treat_type").child(type).child(date).child(hour).setValue("patient_id_attending");
                ref_QueuesInstitute.child(id_institute).child("Treat_type").child(type).child(date).child(hour).setValue("number_queue");
                ref_QueuesInstitute.child(id_institute).child("Treat_type").child(type).child(date).child(hour).setValue("Waiting_list");

                ref_QueuesInstitute.child(id_institute).child("Treat_type").child(type).child(date).child(hour).child("patient_id_attending").setValue(patientIdToDB);

                ref_QueuesInstitute.child(id_institute).child("Treat_type").child(type).child(date).child(hour).child("number_queue").setValue(_numberOfQueue[0]);

                ref_QueuesInstitute.child(id_institute).child("Treat_type").child(type).child(date).child(hour).child("Waiting_list").setValue("patient_1");
                ref_QueuesInstitute.child(id_institute).child("Treat_type").child(type).child(date).child(hour).child("Waiting_list").setValue("patient_2");
                ref_QueuesInstitute.child(id_institute).child("Treat_type").child(type).child(date).child(hour).child("Waiting_list").setValue("patient_3");
                ref_QueuesInstitute.child(id_institute).child("Treat_type").child(type).child(date).child(hour).child("Waiting_list").setValue("patient_4");
                ref_QueuesInstitute.child(id_institute).child("Treat_type").child(type).child(date).child(hour).child("Waiting_list").setValue("patient_5");

                ref_QueuesInstitute.child(id_institute).child("Treat_type").child(type).child(date).child(hour).child("Waiting_list").child("patient_1").setValue("TBD");
                ref_QueuesInstitute.child(id_institute).child("Treat_type").child(type).child(date).child(hour).child("Waiting_list").child("patient_2").setValue("TBD");
                ref_QueuesInstitute.child(id_institute).child("Treat_type").child(type).child(date).child(hour).child("Waiting_list").child("patient_3").setValue("TBD");
                ref_QueuesInstitute.child(id_institute).child("Treat_type").child(type).child(date).child(hour).child("Waiting_list").child("patient_4").setValue("TBD");
                ref_QueuesInstitute.child(id_institute).child("Treat_type").child(type).child(date).child(hour).child("Waiting_list").child("patient_5").setValue("TBD");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    public void addID() {
        add(1);
    }

    public void addType() {
        add(2);
    }

    public void addDate() {
        add(3);
    }

    public void addTime() {
        add(4);
    }

}
