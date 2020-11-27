package com.example.dimot_bekalot.entryActivities;
/**
 *
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.dataObjects.Login_Input_Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Verification_Activity extends AppCompatActivity {

    private Intent retrieveFromLogin;

    private FirebaseDatabase dataBase;
    private DatabaseReference myDataBasePatients;
    private DatabaseReference myDataBaseInstitutes;
    private static final String INSTITUTES = "Institutes";
    private static final String PATIENTS = "Patient";
    private static String INSTITUTEorPATIENT = "NOT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        /*retrieve the login input from Login_Activity in intent*/
        retrieveFromLogin = getIntent();
        Login_Input_Data inputUser = (Login_Input_Data) retrieveFromLogin.getSerializableExtra("Login_Input_Data");

        /*FireBase_connection*/
        dataBase = FirebaseDatabase.getInstance();
        myDataBasePatients = dataBase.getReference().getRoot().child(PATIENTS);
        myDataBaseInstitutes = dataBase.getReference().getRoot().child(INSTITUTES);
        /*end_FireBase_connection*/
        //*************************************************************//

        /**
         * checking if the user is Institute by ID
         */
        Query IDCheckingExistenceInstitutes = myDataBaseInstitutes.orderByChild("id").equalTo(inputUser.getID());
        IDCheckingExistenceInstitutes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {INSTITUTEorPATIENT = INSTITUTES; }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        /**
         * checking if the user is Patient by ID
         */
        Query IDCheckingExistencePatients = myDataBasePatients.orderByChild("id").equalTo(inputUser.getID());
        IDCheckingExistencePatients.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {INSTITUTEorPATIENT = PATIENTS; }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        /*The login input user is not in the DB, back to login again*/
        if(INSTITUTEorPATIENT.equals("NOT")){
            Toast.makeText(Verification_Activity.this, "הנתונים לא תקינים, אנסה נסה שנית" ,Toast.LENGTH_LONG).show();
            goBackToLogin_Activity();
        }

        if(INSTITUTEorPATIENT.equals(PATIENTS)){
           
        }else if(INSTITUTEorPATIENT.equals(INSTITUTES)){

        }
    }

    /************private function that will activate the activities************/
    /*Activate login activity*/
    private void goBackToLogin_Activity() {
        Intent open_login = new Intent(this,Login_Activity.class);
        startActivity(open_login);
    }

    /*Activate Patient Menu activity*/
    private void openPatientMenu_Activity(String Patient_ID) {
        Intent open_patient_menu = new Intent(this,Login_Activity.class);
        //Intent open_institute_menu = new Intent(this,PatientMain.class);
        //open_institute_menu.putExtra("Patient_ID",Patient_ID);
        startActivity(open_patient_menu);
    }

    /*Activate Institute Menu activity*/
    private void openInstituteMenu_Activity(String Institute_ID) {
        Intent open_institute_menu = new Intent(this,Login_Activity.class);
        //Intent open_institute_menu = new Intent(this,InstituteMain.class);
        //open_institute_menu.putExtra("Institute_ID",Institute_ID);
        startActivity(open_institute_menu);
    }

}