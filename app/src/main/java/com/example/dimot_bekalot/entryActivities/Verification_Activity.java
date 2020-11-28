package com.example.dimot_bekalot.entryActivities;
/**
 *
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.dataObjects.Costumer_Details_Patient;
import com.example.dimot_bekalot.dataObjects.Login_Input_Data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Verification_Activity extends AppCompatActivity {

    private Intent retrieveFromLogin;

    private FirebaseDatabase dataBase;
    private DatabaseReference myDataBase;
    private DatabaseReference myDataBaseInstitutes;

    private static final String INSTITUTES = "Institutes";
    private static final String PATIENTS = "Patients";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        /*retrieve the login input from Login_Activity in intent*/
        retrieveFromLogin = getIntent();
        Login_Input_Data inputUser = (Login_Input_Data) retrieveFromLogin.getSerializableExtra("Login_Input_Data");

        /*FireBase_connection*/
        dataBase = FirebaseDatabase.getInstance();
        myDataBase = dataBase.getReference();
        myDataBaseInstitutes = dataBase.getReference(INSTITUTES);
        /*end_FireBase_connection*/
        //*************************************************************//

        myDataBase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(PATIENTS).child(inputUser.getID()).exists()) {
                    String SingInID = snapshot.child(PATIENTS).child(inputUser.getID()).child("id").getValue().toString();
                    String SingInPassword = snapshot.child(PATIENTS).child(inputUser.getID()).child("password").getValue().toString();

                    Login_Input_Data retrieveLoginData = new Login_Input_Data(SingInID, SingInPassword);
                    if (inputUser.equals(retrieveLoginData)) {
                        Toast.makeText(Verification_Activity.this, "ברוכים הבאים !", Toast.LENGTH_LONG).show();
                        openPatientMenu_Activity(inputUser.getID());
                    } else {
                        Toast.makeText(Verification_Activity.this, "פרטים שגויים, התחל שוב", Toast.LENGTH_LONG).show();
                        goBackToLogin_Activity();
                    }
                }
                /**
                 * if the input details is match to Patient details, move to Patient Menu activity
                 */
                else if (snapshot.child(INSTITUTES).child(inputUser.getID()).exists()) {
                    String SingInID = snapshot.child(INSTITUTES).child(inputUser.getID()).child("instituteID").getValue().toString();
                    String SingInPassword = snapshot.child(INSTITUTES).child(inputUser.getID()).child("password").getValue().toString();

                    Login_Input_Data retrieveLoginData = new Login_Input_Data(SingInID, SingInPassword);
                    if (inputUser.equals(retrieveLoginData)) {
                        Toast.makeText(Verification_Activity.this, "ברוכים הבאים !", Toast.LENGTH_LONG).show();
                        openInstituteMenu_Activity(inputUser.getID());
                    }
                    /**
                     * if the input details is match to institute details, move to Institute Menu activity
                     */
                    else {
                        Toast.makeText(Verification_Activity.this, "פרטים שגויים, התחל שוב", Toast.LENGTH_LONG).show();
                        goBackToLogin_Activity();
                    }
                }
                /**
                 * if the input details is NOT match to institutes or patients details, return the user bask
                 * to login Activity
                 */
                else{
                    Toast.makeText(Verification_Activity.this, "פרטים שגויים, התחל שוב", Toast.LENGTH_LONG).show();
                    goBackToLogin_Activity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //*************************************************************//

    /************private function that will activate the activities************/
    /*Activate login activity*/
    private void goBackToLogin_Activity() {
        Intent open_login = new Intent(this, Login_Activity.class);
        startActivity(open_login);
    }

    /*Activate Patient Menu activity*/
    private void openPatientMenu_Activity(String Patient_ID) {
        Intent open_patient_menu = new Intent(this, Login_Activity.class);
        //Intent open_institute_menu = new Intent(this,PatientMain.class);
        //open_institute_menu.putExtra("Patient_ID",Patient_ID);
        startActivity(open_patient_menu);
    }

    /*Activate Institute Menu activity*/
    private void openInstituteMenu_Activity(String Institute_ID) {
        Intent open_institute_menu = new Intent(this, Login_Activity.class);
        //Intent open_institute_menu = new Intent(this,InstituteMain.class);
        //open_institute_menu.putExtra("Institute_ID",Institute_ID);
        startActivity(open_institute_menu);
    }
}