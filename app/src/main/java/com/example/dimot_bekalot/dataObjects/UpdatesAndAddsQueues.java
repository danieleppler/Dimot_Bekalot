package com.example.dimot_bekalot.dataObjects;

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
    private String id_patient, institute_id, date, hour, type, city, name_institute;
    private String numOfQueue = "";

    private FirebaseDatabase dataBase;
    private DatabaseReference ref_QueuesInstitute, ref_QueuesSearch, ref_Queues;
    private DatabaseReference ref_institute;
    private final String queues = "Queues";
    private final String queues_institute = "Queues_institute";
    private final String queues_search = "Queues_search";
    private final String institute = "Institutes";
    private boolean isExist = false;


    public UpdatesAndAddsQueues(String id_institute, String id_patient, String date, String hour, String type, String city, String name) {
        this.institute_id = id_institute;
        this.id_patient = id_patient;
        this.date = date;
        this.hour = hour;
        this.type = type;
        this.city = city;
        this.name_institute = name;
    }

    /* add_queue */
    public void add() {
        initDatabase();

        StringBuilder newDate = new StringBuilder(date);
        newDate.insert(2, ".");
        newDate.insert(5, ".");
        String newHour = Strings_Tools.createNOTCleanUserName(hour, 2, ":");
        String patientIdToDB = id_patient;

        add2QueuesSearch(newDate.toString(), newHour, patientIdToDB);
        add2Queues(newDate.toString(), newHour, patientIdToDB);
        add2QueuesInstitute(patientIdToDB);
    }

    private void add2QueuesSearch(String new_date, String new_hour, String patientIdToDB) {
        numOfQueue = ref_QueuesSearch.push().getKey();

        ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("patient_id_attending").setValue(patientIdToDB);
        ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("date").setValue(new_date);
        ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("institute").setValue(name_institute);
        ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("time").setValue(new_hour);

        ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("Waiting_list").child("patient_1").setValue("TBD");
        ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("Waiting_list").child("patient_2").setValue("TBD");
        ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("Waiting_list").child("patient_3").setValue("TBD");
        ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("Waiting_list").child("patient_4").setValue("TBD");
        ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue).child("Waiting_list").child("patient_5").setValue("TBD");
    }

    private void add2Queues(String new_date, String new_hour, String patientIdToDB) {
        ref_Queues.child(numOfQueue).child("patient_id_attending").setValue(patientIdToDB);
        ref_Queues.child(numOfQueue).child("city").setValue(city);
        ref_Queues.child(numOfQueue).child("date").setValue(new_date);
        ref_Queues.child(numOfQueue).child("institute").setValue(name_institute);
        ref_Queues.child(numOfQueue).child("time").setValue(new_hour);
        ref_Queues.child(numOfQueue).child("treat_type").setValue(type);

        ref_Queues.child(numOfQueue).child("Waiting_list").child("patient_1").setValue("TBD");
        ref_Queues.child(numOfQueue).child("Waiting_list").child("patient_2").setValue("TBD");
        ref_Queues.child(numOfQueue).child("Waiting_list").child("patient_3").setValue("TBD");
        ref_Queues.child(numOfQueue).child("Waiting_list").child("patient_4").setValue("TBD");
        ref_Queues.child(numOfQueue).child("Waiting_list").child("patient_5").setValue("TBD");
    }

    private void add2QueuesInstitute(String patientIdToDB) {
        ref_QueuesInstitute.child(institute_id).child("Treat_type").child(type).child(date).child(hour).child("patient_id_attending").setValue(patientIdToDB);
        ref_QueuesInstitute.child(institute_id).child("Treat_type").child(type).child(date).child(hour).child("number_queue").setValue(numOfQueue);

        ref_QueuesInstitute.child(institute_id).child("Treat_type").child(type).child(date).child(hour).child("Waiting_list").child("patient_1").setValue("TBD");
        ref_QueuesInstitute.child(institute_id).child("Treat_type").child(type).child(date).child(hour).child("Waiting_list").child("patient_2").setValue("TBD");
        ref_QueuesInstitute.child(institute_id).child("Treat_type").child(type).child(date).child(hour).child("Waiting_list").child("patient_3").setValue("TBD");
        ref_QueuesInstitute.child(institute_id).child("Treat_type").child(type).child(date).child(hour).child("Waiting_list").child("patient_4").setValue("TBD");
        ref_QueuesInstitute.child(institute_id).child("Treat_type").child(type).child(date).child(hour).child("Waiting_list").child("patient_5").setValue("TBD");
    }

    /*end add_queue */


    /*change_waiting_list*/

    public void changeWaitList() {
        initDatabase();
        ref_QueuesInstitute.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(isTBD("patient_1")){
                    String tbd = "TBD";
                    updateIdInQueue(tbd);
                }
                else{
                    String id_patient = snapshot.child(institute_id).child("Treat_type").child(type)
                            .child(date).child(hour).child("Waiting_list").child("patient_1").getValue().toString();
                    updateIdInQueue(id_patient);
                    updateWaitList();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void updateIdInQueue(String id_patient) {
        ref_institute.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                city = snapshot.child(institute_id).child("address").child("city_Name").getValue().toString();
                name_institute = snapshot.child(institute_id).child("institute_name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        /*update Queues*/
        ref_Queues.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder newDate = new StringBuilder(date);
                newDate.insert(2, ".");
                newDate.insert(5, ".");
                String newHour = Strings_Tools.createNOTCleanUserName(hour, 2, ":");

                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.child("date").getValue().toString().equals(newDate.toString()) &&
                            data.child("time").getValue().toString().equals(newHour)) {
                        numOfQueue = data.getKey();
                        break;
                    }
                }
                ref_Queues.child(numOfQueue).child("patient_id_attending").setValue(id_patient);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        ref_QueuesSearch.child("City").child(city)
                .child("Treat_type").child(type).child(numOfQueue).child("patient_id_attending").setValue(id_patient);
        ref_QueuesInstitute.child(institute_id).child("Treat_type")
                .child(type).child(date).child(hour).child("patient_id_attending").setValue(id_patient);
        /*update Queues*/
    }

    private void updateWaitList() {
        ref_QueuesSearch.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /* check what is inside patient_2 */
                if(isTBD("patient_2")){
                    updateCurrentPatient("patient_1", "TBD");
                }
                else { // patient_2 == number id:
                    String id_for_patient_1 = snapshot.child("City").child(city).child("Treat_type").child(type).child(numOfQueue)
                            .child("Waiting_list").child("patient_2").getValue().toString();
                    updateCurrentPatient("patient_1", id_for_patient_1);


                    /* check what is inside patient_3 */
                    if(isTBD("patient_3")){
                        updateCurrentPatient("patient_2", "TBD");
                    }
                    else { // patient_3 == number id:
                        String id_for_patient_2 = snapshot.child("City").child(city).child("Treat_type").child(type).child(numOfQueue)
                                .child("Waiting_list").child("patient_3").getValue().toString();

                        updateCurrentPatient("patient_2", id_for_patient_2);


                        /* check what is inside patient_4 */
                        if(isTBD("patient_4")){
                            updateCurrentPatient("patient_3", "TBD");
                        }
                        else { // patient_4 == number id:
                            String id_for_patient_3 = snapshot.child("City").child(city).child("Treat_type").child(type).child(numOfQueue)
                                    .child("Waiting_list").child("patient_4").getValue().toString();

                            updateCurrentPatient("patient_3", id_for_patient_3);


                            /* check what is inside patient_5 */
                            if(isTBD("patient_5")){
                                updateCurrentPatient("patient_4", "TBD");
                            }
                            else { // patient_5 == number id:
                                String id_for_patient_4 = snapshot.child("City").child(city).child("Treat_type").child(type).child(numOfQueue)
                                        .child("Waiting_list").child("patient_5").getValue().toString();

                                updateCurrentPatient("patient_4", id_for_patient_4);
                                updateCurrentPatient("patient_5", "TBD");
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private boolean isTBD(String patient) {
        final boolean[] flag = new boolean[1];
        ref_QueuesSearch.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("City").child(city).child("Treat_type").child(type).child(numOfQueue)
                        .child("Waiting_list").child(patient).getValue().toString().equals("TBD"))
                    flag[0] = true;
                else
                    flag[0] = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        return flag[0];
    }

    private void updateCurrentPatient(String patient, String id) {
        ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(numOfQueue)
                .child("Waiting_list").child(patient).setValue(id);
        ref_QueuesInstitute.child(institute_id).child("Treat_type")
                .child(type).child(date).child(hour).child("Waiting_list").child(patient).setValue(id);
        ref_Queues.child(numOfQueue).child("Waiting_list").child(patient).setValue(id);
    }

    /*end change_waiting_list*/

    private void initDatabase() {
        if (!isExist) {
            dataBase = FirebaseDatabase.getInstance();
            ref_QueuesInstitute = dataBase.getReference(queues_institute);
            ref_Queues = dataBase.getReference(queues);
            ref_QueuesSearch = dataBase.getReference(queues_search);
            ref_institute = dataBase.getReference(institute);
            isExist = true;
        }
    }
}
