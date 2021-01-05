package com.example.dimot_bekalot.entryActivities;
/**
 * This activity checkes if the login details is match to the details at the DB
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.example.dimot_bekalot.Firebase.DB_LockUser;
import com.example.dimot_bekalot.Firebase.DB_validation;
import com.example.dimot_bekalot.InstituteActivity.InstituteMain;
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

public class Login_Verification_Activity extends AppCompatActivity {

    private Intent retrieveFromLogin;

    private FirebaseAuth emailCheck;
    private FirebaseDatabase dataBase;
    private DatabaseReference myDataBase;

    private static final String INSTITUTES = "Institutes";
    private static final String PATIENTS = "Patients";
    private static String PATIENTSorINSTITUTES = "";
    private int LOCK_ACCOUNT = 0;

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

                /**if the input details will match to Patient details*/
                if (inputUserFromLoginActivity.getID().charAt(0) == 'p') {
                    if (snapshot.child(PATIENTS).child(inputUserFromLoginActivity.getID()).exists()) {
                        PATIENTSorINSTITUTES = PATIENTS;
                        /*if the user is not locked, can move on*/
                        ValidUSER(snapshot, inputUserFromLoginActivity.getID(), inputUserFromLoginActivity, PATIENTSorINSTITUTES);
                    } else {
                        Toast.makeText(Login_Verification_Activity.this, "פרטים שגויים, התחל שוב בבקשה", Toast.LENGTH_LONG).show();
                        goBackToLogin_Activity();
                    }
                }

                /**if the input details will match to institute details*/
                else if (inputUserFromLoginActivity.getID().charAt(0) == 'i') {
                    if (snapshot.child(INSTITUTES).child(inputUserFromLoginActivity.getID()).exists()) {
                        PATIENTSorINSTITUTES = INSTITUTES;
                        /*if the user is not locked, can move on*/
                        ValidUSER(snapshot, inputUserFromLoginActivity.getID(), inputUserFromLoginActivity, PATIENTSorINSTITUTES);
                    } else {
                        Toast.makeText(Login_Verification_Activity.this, "פרטים שגויים, התחל שוב בבקשה", Toast.LENGTH_LONG).show();
                        goBackToLogin_Activity();
                    }
                }

                /**if the input details is NOT match to institutes or patients details*/
                else {
                    Toast.makeText(Login_Verification_Activity.this, "פרטים שגויים, התחל שוב בבקשה", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    //*************************************************************//

    /************private function************/
    /**
     *
     * @param SingInUserName
     * @param SingInPassword
     * @param SingInEmail
     */
    private void emailVerification(String SingInUserName, String SingInPassword, String SingInEmail, String PATIENTorINSTITUTE) {
        /*if the password and the ID is verified, we have to check the email address*/
        emailCheck.signInWithEmailAndPassword(SingInEmail, SingInPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = emailCheck.getCurrentUser();
                    if (user.isEmailVerified()) {
                        Toast.makeText(Login_Verification_Activity.this, "ברוכים הבאים !", Toast.LENGTH_LONG).show();
                        DB_LockUser.Unlock_user(SingInUserName, PATIENTSorINSTITUTES);
                        goToRightActivity(SingInUserName, PATIENTorINSTITUTE);
                    } else {
                        Toast.makeText(Login_Verification_Activity.this, "לא אישרתם את כתובת האי-מייל, אשרו כתובתכם לצורך התחברות", Toast.LENGTH_LONG).show();
                        Toast.makeText(Login_Verification_Activity.this, "הנכם חוזרים לעמוד ההתחברות", Toast.LENGTH_LONG).show();
                        goBackToLogin_Activity();
                    }
                } else {
                    Toast.makeText(Login_Verification_Activity.this, "משהו הלך לא קשורה, התחברו שוב בבקשה,כניראה האי-מייל כבר נמצא במערכת", Toast.LENGTH_LONG).show();
                    Toast.makeText(Login_Verification_Activity.this, "בבקשה נסו שנית", Toast.LENGTH_LONG).show();
                    goBackToLogin_Activity();
                }
            }
        });
    }

    /**
     * check if the input user details is correct
     * @param snapshot
     * @param userName_ID
     * @return
     */
    private Login_Input_Data checkValidDetails(DataSnapshot snapshot, String PATIENTSorINSTITUTES, String userName_ID) {
        return DB_validation.checkValidDetails(snapshot, PATIENTSorINSTITUTES, userName_ID);
    }

    /**
     *
     * @param snapshot
     * @param user_ID
     * @param inputUserFromLoginActivity
     */
    private boolean ValidUSER(DataSnapshot snapshot, String user_ID, Login_Input_Data inputUserFromLoginActivity, String PATIENTSorINSTITUTES) {
        if (!DB_LockUser.isUserLocked(snapshot, user_ID, PATIENTSorINSTITUTES)) {
            Login_Input_Data backFromDB_LOGINdata = checkValidDetails(snapshot, PATIENTSorINSTITUTES, inputUserFromLoginActivity.getID());
            if (inputUserFromLoginActivity.equals(backFromDB_LOGINdata)) {
                /*if the password and the ID is verified, we have to check the email address*/
                emailVerification(inputUserFromLoginActivity.getID(), inputUserFromLoginActivity.getPassword(), inputUserFromLoginActivity.getEmail(), PATIENTSorINSTITUTES);
                return true;
                /*else: incorrect password*/
            }
            if (LOCK_ACCOUNT == 0) {
                LOCK_ACCOUNT = 1;
                if (Integer.valueOf(DB_LockUser.get_num_of_tries_login(snapshot, user_ID, PATIENTSorINSTITUTES)) == 2) {
                    DB_LockUser.lock_user(user_ID, PATIENTSorINSTITUTES);
                    lockedUSER();
                } else {
                    DB_LockUser.upTo_3_tries(snapshot, user_ID, PATIENTSorINSTITUTES);
                    Toast.makeText(Login_Verification_Activity.this, "חלק מן פרטים שגויים, התחל שוב בבקשה", Toast.LENGTH_LONG).show();
                    goBackToLogin_Activity();
                    return false;
                }
            }
        } else { lockedUSER(); }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent open_login = new Intent(this, Main_Activity.class);
        open_login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(open_login);
    }

    /*if the user is locked, sho this Toast*/
    private void lockedUSER() {
        Toast.makeText(Login_Verification_Activity.this, "החשבון חסום, אנא הכיסנו ל'שכחתי סיסמא' והחליפו אותה", Toast.LENGTH_LONG).show();
        goBackToLogin_Activity();
        return;
    }

    /*Activate login activity*/
    private void goBackToLogin_Activity() {
        Intent open_login = new Intent(this, Login_Activity.class);
        startActivity(open_login);
    }

    /**/
    private void goToRightActivity(String ID, String PATIENTorINSTITUTE) {
        if (PATIENTorINSTITUTE.equals(PATIENTS)) {
            openPatientMenu_Activity(ID);
        } else if (PATIENTorINSTITUTE.equals(INSTITUTES)) {
            openInstituteMenu_Activity(ID);
        }
    }

    /*Activate Patient Menu activity*/
    private void openPatientMenu_Activity(String Patient_ID) {
        Intent open_patient_menu = new Intent(this, com.example.dimot_bekalot.clientActivities.Main_Client_View.class);
        open_patient_menu.putExtra("client_id", Patient_ID);
        open_patient_menu.putExtra("check","0");
        startActivity(open_patient_menu);
    }

    /*Activate Institute Menu activity*/
    private void openInstituteMenu_Activity(String Institute_ID) {
        Intent open_institute_menu = new Intent(this, InstituteMain.class);
        open_institute_menu.putExtra("instituteID", Institute_ID);
        startActivity(open_institute_menu);
    }
}