package com.example.dimot_bekalot.entryActivities;
/**
 *
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.dimot_bekalot.R;
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
import com.google.firebase.database.ValueEventListener;

public class login_Verification_Activity extends AppCompatActivity {

    private Intent retrieveFromLogin;

    private FirebaseAuth emailCheck;

    private FirebaseDatabase dataBase;
    private DatabaseReference myDataBase;

    private static final String INSTITUTES = "Institutes";
    private static final String PATIENTS = "Patients";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        /*retrieve the login input data from Login_Activity in the Intent*/
        retrieveFromLogin = getIntent();
        Login_Input_Data inputUserFromLoginActivity = (Login_Input_Data) retrieveFromLogin.getSerializableExtra("Login_Input_Data");

        /*FireBase_connection*/
        dataBase = FirebaseDatabase.getInstance();
        myDataBase = dataBase.getReference();
        emailCheck = FirebaseAuth.getInstance();
        /*end_FireBase_connection*/
        //*************************************************************//

        myDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /**
                 * if the input details will match to Patient details, move to Patient Menu activity
                 */
                boolean found=false;
                if (snapshot.child(PATIENTS).child(inputUserFromLoginActivity.getID()).exists()) {
                    String SingInID = snapshot.child(PATIENTS).child(inputUserFromLoginActivity.getID()).child("patientID").getValue().toString();
                    String SingInPassword = snapshot.child(PATIENTS).child(inputUserFromLoginActivity.getID()).child("password").getValue().toString();
                    String SingInEmail = snapshot.child(PATIENTS).child(inputUserFromLoginActivity.getID()).child("email").getValue().toString();
                    Login_Input_Data backFromDB_LOGINdata = new Login_Input_Data(SingInID, SingInPassword, SingInEmail);

                    if (inputUserFromLoginActivity.equals(backFromDB_LOGINdata)) {
                        found=true;
                        /*if the password and the ID is verified, we have to check the email address*/
                        emailVerification(SingInID, SingInPassword, SingInEmail,PATIENTS);
                    }
                }

                /**
                 * if the input details will match to institute details, move to Institute Menu activity
                 */
                if (snapshot.child(INSTITUTES).child(inputUserFromLoginActivity.getID()).exists()) {
                    String SingInID = snapshot.child(INSTITUTES).child(inputUserFromLoginActivity.getID()).child("instituteID").getValue().toString();
                    String SingInPassword = snapshot.child(INSTITUTES).child(inputUserFromLoginActivity.getID()).child("password").getValue().toString();
                    String SingInEmail = snapshot.child(INSTITUTES).child(inputUserFromLoginActivity.getID()).child("email").getValue().toString();
                    Login_Input_Data backFromDB_LOGINdata = new Login_Input_Data(SingInID, SingInPassword, SingInEmail);

                    if (inputUserFromLoginActivity.equals(backFromDB_LOGINdata)) {
                        found=true;
                        /*if the password and the ID is verified, we have to check the email address*/
                        emailVerification(SingInID, SingInPassword, SingInEmail,INSTITUTES);
                    }
                }
                /**
                 * if the input details is NOT match to institutes or patients details, return the user back
                 * to login Activity
                 */
                if(!found){
                    Toast.makeText(login_Verification_Activity.this, "פרטים שגויים, התחל שוב בבקשה", Toast.LENGTH_LONG).show();
                    goBackToLogin_Activity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    //*************************************************************//

    /************private function************/
    /**
     *
     * @param SingInID
     * @param SingInPassword
     * @param SingInEmail
     */
    private void emailVerification (String SingInID, String SingInPassword, String SingInEmail,String PATIENTorINSTITUTE ){
        /*if the password and the ID is verified, we have to check the email address*/
        emailCheck.signInWithEmailAndPassword(SingInEmail,SingInPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser patient = emailCheck.getCurrentUser();
                    if(patient.isEmailVerified()){
                        Toast.makeText(login_Verification_Activity.this, "ברוכים הבאים !", Toast.LENGTH_LONG).show();
                        goToRightActivity(SingInID ,PATIENTorINSTITUTE);
                    }else{
                        Toast.makeText(login_Verification_Activity.this, "לא אישרתם את כתובת האי-מייל, אשרו כתובתכם לצורך התחברות", Toast.LENGTH_LONG).show();
                        Toast.makeText(login_Verification_Activity.this, "הנכם חוזרים לעמוד ההתחברות", Toast.LENGTH_LONG).show();
                        goBackToLogin_Activity();
                    }
                }
                else {
                    Toast.makeText(login_Verification_Activity.this, "משהו הלך לא קשורה, התחברו שוב בבקשה", Toast.LENGTH_LONG).show();
                    goBackToLogin_Activity();
                }
            }
        });
    }

    /*Activate login activity*/
    private void goBackToLogin_Activity() {
        Intent open_login = new Intent(this, Login_Activity.class);
        startActivity(open_login);
    }

    /**/
    private void goToRightActivity(String ID, String PATIENTorINSTITUTE){
        if(PATIENTorINSTITUTE.equals(PATIENTS)){
            openPatientMenu_Activity(ID);
        }else if(PATIENTorINSTITUTE.equals(INSTITUTES)) {
            openInstituteMenu_Activity(ID);
        }
    }

    /*Activate Patient Menu activity*/
    private void openPatientMenu_Activity(String Patient_ID) {
        Intent open_patient_menu = new Intent(this, temp_from_login_Activity_PA.class);
//        Intent open_patient_menu = new Intent(this,Main_Client_View.class);
//        open_patient_menu.putExtra("Patient_ID",Patient_ID);
        startActivity(open_patient_menu);
    }

    /*Activate Institute Menu activity*/
    private void openInstituteMenu_Activity(String Institute_ID) {
        Intent open_institute_menu = new Intent(this, temp_from_login_Activity_IN.class);
        //Intent open_institute_menu = new Intent(this,InstituteMain.class);
        //open_institute_menu.putExtra("Institute_ID",Institute_ID);
        startActivity(open_institute_menu);
    }
}