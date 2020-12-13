package com.example.dimot_bekalot.entryActivities;
/**
 * This activity create a new Institute account and save it in the DB
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.dataObjects.Address;
import com.example.dimot_bekalot.dataObjects.Costumer_Details_Institute;
import com.example.dimot_bekalot.dataObjects.LockedAccount;
import com.example.dimot_bekalot.Tools.validationTools;
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

public class Register_Institute_Activity extends AppCompatActivity {

    private EditText institute_nameInput, instituteID_Input, emailInput, phone_numberInput;
    private EditText passwordInput, cityInput, streetInput, building_numberInput;

    private Button registerInstitute_button;
    private ProgressBar progressBar;

    private FirebaseDatabase dataBase;
    private DatabaseReference myDataBase;
    private FirebaseAuth fAuto;

    private static final String INSTITUTES = "Institutes";

    private Costumer_Details_Institute costumer_details_institute;
    private Address instituteAddress;
    private LockedAccount lockedAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_institute);
        /*lock the screen-rotation for this activity*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        /*******************************************/

        institute_nameInput = findViewById(R.id.user_first_name_input_register_institute);
        instituteID_Input = findViewById(R.id.user_ID_input_register_institute);
        emailInput = findViewById(R.id.user_email_input_register_institute);
        phone_numberInput = findViewById(R.id.user_phone_input_register_institute);
        passwordInput = findViewById(R.id.user_password_input_register_institute);
        cityInput = findViewById(R.id.user_living_city_input_register_institute);
        streetInput = findViewById(R.id.user_living_street_input_register_institute);
        building_numberInput = findViewById(R.id.user_house_number_input_register_institute);

        /*Buttons_connection*/
        registerInstitute_button = findViewById(R.id.registr_new_institute_button);
        progressBar = findViewById(R.id.user_progress_bar_institute);
        /*end_Buttons_connection*/

        /*FireBase_connection*/
        dataBase = FirebaseDatabase.getInstance();
        myDataBase = dataBase.getReference(INSTITUTES);
        fAuto=FirebaseAuth.getInstance();
        /*end_FireBase_connection*/
        //*************************************************************//

        registerInstitute_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String instituteName = institute_nameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String phone = phone_numberInput.getText().toString().trim();
                String instituteID = instituteID_Input.getText().toString().trim();
                String cityLiving = cityInput.getText().toString().trim();

                /*checking if the inputs is valid inputs*/
                if (!validationTools.CheckIfNumber(instituteID, instituteID_Input)) { return; }
                if (!validationTools.CheckIfNumber(phone, phone_numberInput)) { return; }
                if (!validationTools.isInstituteNamesIsValid(instituteName, instituteID, cityLiving,
                        cityInput, institute_nameInput, instituteID_Input)) { return; }
                if (!validationTools.isAllCostumersNeedfulInputIsValid(email, password, phone,
                        emailInput, passwordInput, phone_numberInput)) { return; }
                /*end_validation_checking*/

                /*checking if the user is already exist, if not added the user*/
                Query IDCheckingExistence = myDataBase.orderByChild("instituteID").equalTo(instituteID);
                IDCheckingExistence.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() > 0) {
                            Toast.makeText(Register_Institute_Activity.this, "משתמש זה כבר רשום, מספר הרישוי כבר קיים", Toast.LENGTH_LONG).show();
                            openLogin_Activity();
                        } else {
                            /*continue with other inputs after validation*/
                            String streetLiving = streetInput.getText().toString().trim();
                            String buildingNumber = building_numberInput.getText().toString().trim();
                            /*end_all_inputs*/

                            /*create a new Patient*/
                            instituteAddress = new Address(cityLiving, streetLiving, buildingNumber);
                            lockedAccount  =new LockedAccount("false","0");
                            costumer_details_institute = new Costumer_Details_Institute(email, phone,
                                    password, instituteAddress,lockedAccount, instituteName, "i:"+instituteID);

                            /**all the needful details are enters, can move to register
                             *the user in fireBase
                             */
                            registerInstitute();
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }
        });
    }

    /*Adding patient to our Firebase DataBase - not real DB*/
    private void registerInstitute() {
        fAuto.createUserWithEmailAndPassword(this.costumer_details_institute.getEmail(), this.costumer_details_institute.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            registerPatientToRealDB();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = fAuto.getCurrentUser();
                            update_Authentication(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register_Institute_Activity.this, "חשבון האי-מייל לא תקין או קיים כבר במערכת, אנה נסה להירשם שוב בבקשה", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Login_Activity.class));
                            //finish();
                        }
                    }
                });
    }


    /************private function************/
    private void update_Authentication(FirebaseUser currentInstituteUser) {
        Intent open_email_verification = new Intent(this, EMail_Verification_Activity.class);
        open_email_verification.putExtra("InstituteUser", currentInstituteUser);
        open_email_verification.putExtra("userName_ID",this.costumer_details_institute.getInstituteID());
        startActivity(open_email_verification);
    }

    /*Activate login activity*/
    private void openLogin_Activity() {
        Intent open_login = new Intent(this, Login_Activity.class);
        open_login.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
        open_login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(open_login);
    }

    /*Adding patient to our Firebase DataBase*/
    private void registerPatientToRealDB() {
        myDataBase.child(costumer_details_institute.getInstituteID()).setValue(this.costumer_details_institute);
    }
}