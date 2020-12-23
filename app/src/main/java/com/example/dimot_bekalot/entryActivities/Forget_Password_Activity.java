package com.example.dimot_bekalot.entryActivities;
/**
 * This activity allow to change the password of a user after
 * carefully checking that it's the right user
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dimot_bekalot.Firebase.DB_ChangePassword;
import com.example.dimot_bekalot.Firebase.DB_LockUser;
import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.dataObjects.Login_Input_Data;
import com.example.dimot_bekalot.Tools.Strings_Tools;
import com.example.dimot_bekalot.Tools.validationTools;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Forget_Password_Activity extends AppCompatActivity {

    private Button send_emailAndUserID_and_newPassword;
    private Button send_new_password;

    private EditText userName_ID_input_and_password_1;
    private EditText email_input_and_password_2;
    private TextView forgetPassword_MainText;

    private FirebaseDatabase dataBase;
    private DatabaseReference myDataBase;
    private static final String INSTITUTES = "Institutes";
    private static final String PATIENTS = "Patients";
    private static String PATIENTSorINSTITUTES = "";
    private Login_Input_Data InputToChangePassword;

    private int PERMISSION_TO_CHANGE_PASSWORD = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        userName_ID_input_and_password_1 = findViewById(R.id.forget_password_userName_ID);
        email_input_and_password_2 = findViewById(R.id.forget_password_user_email);

        /*Buttons_connection*/
        send_emailAndUserID_and_newPassword = (Button) findViewById(R.id.foget_passwoed_send_details_to_check);
        send_new_password = (Button) findViewById(R.id.forget_password_send_new_password);
        /*end_Buttons_connection*/

        /*FireBase_connection*/
        dataBase = FirebaseDatabase.getInstance();
        myDataBase = dataBase.getReference();
        /*end_FireBase_connection*/
        //*************************************************************//

        send_emailAndUserID_and_newPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName_ID = userName_ID_input_and_password_1.getText().toString().trim();
                String email = email_input_and_password_2.getText().toString().trim();

                /*checking if the inputs is valid inputs*/
                if (!validationTools.isForgetPasswordInputValid_User_email(userName_ID, email,
                        userName_ID_input_and_password_1, email_input_and_password_2)) { return; }
                if (!validationTools.CheckIfNumber(Strings_Tools.only_number_at_ID(userName_ID), userName_ID_input_and_password_1)) { return; }
                /*end_validation_checking*/

                InputToChangePassword = new Login_Input_Data(Strings_Tools.createNOTCleanUserName(userName_ID, 1, ":"), email);

                myDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        /**if the input details will match to Patient details*/
                        boolean found_User = false;
                        if (userName_ID.charAt(0) == 'p') {
                            if (snapshot.child(PATIENTS).child(Strings_Tools.createNOTCleanUserName(userName_ID, 1, ":")).exists()) {
                                PATIENTSorINSTITUTES = PATIENTS;
                                found_User = checkValidDetails(snapshot, 'p', Strings_Tools.createNOTCleanUserName(userName_ID, 1, ":"));
                            } else {
                                Toast.makeText(Forget_Password_Activity.this, "פרטים שגויים, התחל שוב בבקשה", Toast.LENGTH_LONG).show();
                                goBackToLogin_Activity();
                                return;
                            }
                        }

                        /**if the input details will match to institute details*/
                        if (userName_ID.charAt(0) == 'i') {
                            if (snapshot.child(INSTITUTES).child(Strings_Tools.createNOTCleanUserName(userName_ID, 1, ":")).exists()) {
                                PATIENTSorINSTITUTES = INSTITUTES;
                                found_User = checkValidDetails(snapshot, 'i', Strings_Tools.createNOTCleanUserName(userName_ID, 1, ":"));
                            } else {
                                Toast.makeText(Forget_Password_Activity.this, "פרטים שגויים, התחל שוב בבקשה", Toast.LENGTH_LONG).show();
                                goBackToLogin_Activity();
                                return;
                            }
                        }

                        /**if the input details is NOT match to institutes or patients details, return the user back
                         * to login Activity*/
                        if (!found_User) {
                            Toast.makeText(Forget_Password_Activity.this, "פרטים שגויים, התחל שוב בבקשה", Toast.LENGTH_LONG).show();
                            goBackToLogin_Activity();
                            return;
                        }

                        send_new_password.setVisibility(View.VISIBLE);
                        send_emailAndUserID_and_newPassword.setVisibility(View.INVISIBLE);
                        forgetPassword_MainText = (TextView) findViewById(R.id.forget_password_main_text);
                        forgetPassword_MainText.setText("זיכרו, תוכלו לשנות את הסיסמא רק אם הרפטים הקודמים שמלאתם נכונים");
                        forgetPassword_MainText.setTextSize(25);
                        userName_ID_input_and_password_1.setText("");
                        email_input_and_password_2.setText("");
                        userName_ID_input_and_password_1.setHint("הכנס סיסמא חדשה");
                        email_input_and_password_2.setHint("אימות סיסמא");
                        send_emailAndUserID_and_newPassword.setText("אשר סיסמא חדשה");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }
        });

/**
 * will change the password tot the user, if PERMISSION_TO_CHANGE_PASSWORD is equal to 1
 */
        send_new_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PERMISSION_TO_CHANGE_PASSWORD == 1) {

                    String newPassword_1 = userName_ID_input_and_password_1.getText().toString().trim();
                    String newPassword_2 = email_input_and_password_2.getText().toString().trim();

                    /*checking if the inputs is valid inputs*/
                    if (!validationTools.isForgetPasswordInputValid(newPassword_1, userName_ID_input_and_password_1)) { return; }
                    if (!validationTools.isForgetPasswordInputValid(newPassword_2, email_input_and_password_2)) { return; }
                    /*end_validation_checking*/

                    if (!newPassword_1.equals(newPassword_2)) {
                        Toast.makeText(Forget_Password_Activity.this, "אינכם יכולים להמשיך, הסיסמאות חייבות להיות זהות", Toast.LENGTH_LONG).show();
                        email_input_and_password_2.setError("שדה זה הוא חובה");
                        return;
                    } else {
                        updatePASSWORD(newPassword_2);
                    }
                } else {
                    Toast.makeText(Forget_Password_Activity.this, "פרטי ההזדהות הראשונים היו שגויים, התחל שוב בבקשה", Toast.LENGTH_LONG).show();
                    goBackToLogin_Activity();
                    return;
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent open_login = new Intent(this, Main_Activity.class);
        open_login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(open_login);
    }

    /************private function************/

    /*write the new password of the user in real DB and Unlock the user*/
    private void updatePASSWORD(String newValidPassword) {
        DB_ChangePassword.changePasswordAtAuthentication(PATIENTSorINSTITUTES, this.InputToChangePassword , newValidPassword);
        /*wait until the Firebase Authentication will update the password*/
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) { e.printStackTrace(); }
        DB_ChangePassword.changePasswordAtRealDB(PATIENTSorINSTITUTES, this.InputToChangePassword, newValidPassword);
        DB_LockUser.Unlock_user(this.InputToChangePassword.getID(),PATIENTSorINSTITUTES);
        Toast.makeText(Forget_Password_Activity.this, "הסיסמא שונתה, מועבר לעמוד ההתחברות", Toast.LENGTH_LONG).show();
        goBackToLogin_Activity();
        return;
    }

    /**
     * check if the input user details is correct
     * @param snapshot
     * @param PATIENTorINSTITUTE
     * @param userName_ID
     * @return
     */
    private boolean checkValidDetails(DataSnapshot snapshot, char PATIENTorINSTITUTE, String userName_ID) {
        String realUserName_ID = "";
        if (PATIENTorINSTITUTE == 'p') {
            realUserName_ID = snapshot.child(PATIENTSorINSTITUTES).child(userName_ID).child("patientID").getValue().toString();
        } else {
            realUserName_ID = snapshot.child(INSTITUTES).child(userName_ID).child("instituteID").getValue().toString();
        }

        String realEmail = snapshot.child(PATIENTSorINSTITUTES).child(userName_ID).child("email").getValue().toString();
        Login_Input_Data backFromDB_LOGINdata = new Login_Input_Data(realUserName_ID, realEmail);

        if (InputToChangePassword.equals(backFromDB_LOGINdata)) {
            PERMISSION_TO_CHANGE_PASSWORD = 1;
            return true;
        }
        return false;
    }

    /*Activate login activity*/
    private void goBackToLogin_Activity() {
        Intent open_login = new Intent(this, Login_Activity.class);
        startActivity(open_login);
    }
}