package com.example.dimot_bekalot.dataObjects;

import androidx.annotation.NonNull;

import com.example.dimot_bekalot.Tools.Strings_Tools;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

/**
 * This class was created to add new queues to the DB
 * and to update existing queues
 */

public class UpdatesAndAddsQueues implements Serializable {
    private String client_id, institute_id, date, hour, type, city, institute_name;
    private String number_queue = "";

    private FirebaseDatabase dataBase;
    private DatabaseReference ref_QueuesInstitute, ref_QueuesSearch, ref_Queues;
    private DatabaseReference ref_institute;
    private final String QUEUES = "Queues";
    private final String QUEUES_INSTITUTE = "Queues_institute";
    private final String QUEUES_SEARCH = "Queues_search";
    private final String INSTITUTE = "Institutes";

    private boolean isExist = false; // for init database

    public UpdatesAndAddsQueues(String id_institute, String id_patient, String date, String hour, String type, String city, String name) {
        this.institute_id = id_institute;
        this.client_id = id_patient;
        this.date = date;
        this.hour = hour;
        this.type = type;
        this.city = city;
        this.institute_name = name;
    }

    /* add_queue */
    public void add() {
        initDatabase();

        StringBuilder newDate = new StringBuilder(date);
        newDate.insert(2, ".");
        newDate.insert(5, ".");
        String newHour = Strings_Tools.createNOTCleanUserName(hour, 2, ":");
        String patientIdToDB = client_id;

        add2QueuesSearch(newDate.toString(), newHour, patientIdToDB);
        add2Queues(newDate.toString(), newHour, patientIdToDB);
        add2QueuesInstitute(patientIdToDB);
    }

    private void add2QueuesSearch(String new_date, String new_hour, String patientIdToDB) {
        number_queue = ref_QueuesSearch.push().getKey();

        ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(number_queue).child("patient_id_attending").setValue(patientIdToDB);
        ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(number_queue).child("date").setValue(new_date);
        ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(number_queue).child("institute").setValue(institute_name);
        ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(number_queue).child("time").setValue(new_hour);

        ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(number_queue).child("Waiting_list").child("patient_1").setValue("TBD");
        ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(number_queue).child("Waiting_list").child("patient_2").setValue("TBD");
        ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(number_queue).child("Waiting_list").child("patient_3").setValue("TBD");
        ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(number_queue).child("Waiting_list").child("patient_4").setValue("TBD");
        ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(number_queue).child("Waiting_list").child("patient_5").setValue("TBD");
    }

    private void add2Queues(String new_date, String new_hour, String patientIdToDB) {
        ref_Queues.child(number_queue).child("patient_id_attending").setValue(patientIdToDB);
        ref_Queues.child(number_queue).child("city").setValue(city);
        ref_Queues.child(number_queue).child("date").setValue(new_date);
        ref_Queues.child(number_queue).child("institute").setValue(institute_name);
        ref_Queues.child(number_queue).child("time").setValue(new_hour);
        ref_Queues.child(number_queue).child("treat_type").setValue(type);

        ref_Queues.child(number_queue).child("Waiting_list").child("patient_1").setValue("TBD");
        ref_Queues.child(number_queue).child("Waiting_list").child("patient_2").setValue("TBD");
        ref_Queues.child(number_queue).child("Waiting_list").child("patient_3").setValue("TBD");
        ref_Queues.child(number_queue).child("Waiting_list").child("patient_4").setValue("TBD");
        ref_Queues.child(number_queue).child("Waiting_list").child("patient_5").setValue("TBD");
    }

    private void add2QueuesInstitute(String patientIdToDB) {
        ref_QueuesInstitute.child(institute_id).child("Treat_type").child(type).child(date).child(hour).child("patient_id_attending").setValue(patientIdToDB);
        ref_QueuesInstitute.child(institute_id).child("Treat_type").child(type).child(date).child(hour).child("number_queue").setValue(number_queue);

        ref_QueuesInstitute.child(institute_id).child("Treat_type").child(type).child(date).child(hour).child("Waiting_list").child("patient_1").setValue("TBD");
        ref_QueuesInstitute.child(institute_id).child("Treat_type").child(type).child(date).child(hour).child("Waiting_list").child("patient_2").setValue("TBD");
        ref_QueuesInstitute.child(institute_id).child("Treat_type").child(type).child(date).child(hour).child("Waiting_list").child("patient_3").setValue("TBD");
        ref_QueuesInstitute.child(institute_id).child("Treat_type").child(type).child(date).child(hour).child("Waiting_list").child("patient_4").setValue("TBD");
        ref_QueuesInstitute.child(institute_id).child("Treat_type").child(type).child(date).child(hour).child("Waiting_list").child("patient_5").setValue("TBD");
    }

    /*end add_queue */


    /* change_waiting_list */
    public void changeWaitList() {
        initDatabase();

        ref_QueuesInstitute.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                number_queue = snapshot.child(institute_id).child("Treat_type")
                        .child(type).child(date).child(hour).child("number_queue").child(number_queue).getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        ref_Queues.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String id_patient = snapshot.child(number_queue).child("Waiting_list").child("patient_1").getValue().toString();

                updateIdInQueue(id_patient);

                if(!id_patient.equals("TBD")){
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
                institute_name = snapshot.child(institute_id).child("institute_name").getValue().toString();
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
                        number_queue = data.getKey();
                        break;
                    }
                }
                ref_Queues.child(number_queue).child("patient_id_attending").setValue(id_patient);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type)
                .child(number_queue).child("patient_id_attending").setValue(id_patient);
        ref_QueuesInstitute.child(institute_id).child("Treat_type")
                .child(type).child(date).child(hour).child("patient_id_attending").setValue(id_patient);
        /*update Queues*/
    }

    /* update the waiting list */
    private void updateWaitList() {
        ref_Queues.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /* check what is inside patient_2 */
                String id_for_patient = snapshot.child(number_queue)
                        .child("Waiting_list").child("patient_2").getValue().toString();
                if(id_for_patient.equals("TBD")){
                    updateCurrentPatient("patient_1", "TBD");
                }
                else { // patient_2 == number id:
                    updateCurrentPatient("patient_1", id_for_patient);

                    /* check what is inside patient_3 */
                    id_for_patient = snapshot.child(number_queue)
                            .child("Waiting_list").child("patient_3").getValue().toString();
                    if(id_for_patient.equals("TBD")){
                        updateCurrentPatient("patient_2", "TBD");
                    }
                    else { // patient_3 == number id:
                        updateCurrentPatient("patient_2", id_for_patient);

                        /* check what is inside patient_4 */
                        id_for_patient = snapshot.child(number_queue)
                                .child("Waiting_list").child("patient_4").getValue().toString();
                        if(id_for_patient.equals("TBD")){
                            updateCurrentPatient("patient_3", "TBD");
                        }
                        else { // patient_4 == number id:
                            updateCurrentPatient("patient_3", id_for_patient);

                            /* check what is inside patient_5 */
                            id_for_patient = snapshot.child(number_queue)
                                    .child("Waiting_list").child("patient_5").getValue().toString();
                            if(id_for_patient.equals("TBD")){
                                updateCurrentPatient("patient_4", "TBD");
                            }
                            else { // patient_5 == number id:
                                updateCurrentPatient("patient_4", id_for_patient);
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

    private void updateCurrentPatient(String patient, String id) {
        ref_QueuesSearch.child("City").child(city).child("Treat_type").child(type).child(number_queue)
                .child("Waiting_list").child(patient).setValue(id);
        ref_QueuesInstitute.child(institute_id).child("Treat_type")
                .child(type).child(date).child(hour).child("Waiting_list").child(patient).setValue(id);
        ref_Queues.child(number_queue).child("Waiting_list").child(patient).setValue(id);
    }

    /*end change_waiting_list*/

    /* init DB only once */
    private void initDatabase() {
        if (!isExist) {
            dataBase = FirebaseDatabase.getInstance();
            ref_QueuesInstitute = dataBase.getReference(QUEUES_INSTITUTE);
            ref_Queues = dataBase.getReference(QUEUES);
            ref_QueuesSearch = dataBase.getReference(QUEUES_SEARCH);
            ref_institute = dataBase.getReference(INSTITUTE);
            isExist = true;
        }
    }
}