package com.example.dimot_bekalot.entryActivities;
/**
 * This activity create a new Patient account and save it in the DB
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.dataObjects.Address;
import com.example.dimot_bekalot.dataObjects.Costumer_Details_Patient;
import com.example.dimot_bekalot.tools.validationTools;
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

public class Register_Patient_Activity extends AppCompatActivity {
    private EditText first_nameInput, last_nameInput, ageInput;
    private EditText patientID_Input, emailInput, phone_numberInput;
    private EditText passwordInput, cityInput, streetInput, home_numberInput;

    private Button registerPatient_button;
    private ProgressBar progressBar;

    private FirebaseDatabase dataBase;
    private DatabaseReference myDataBase;
    private static final String PATIENTS = "Patients";
    private Costumer_Details_Patient costumer_details_patient;
    private Address patientAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);

        first_nameInput = findViewById(R.id.user_first_name_input_register_patient);
        last_nameInput = findViewById(R.id.user_last_name_input_register_patient);
        ageInput = findViewById(R.id.user_age_input_register_patient);
        patientID_Input = findViewById(R.id.user_ID_input_register_patient);
        emailInput = findViewById(R.id.user_email_input_register_patient);
        phone_numberInput = findViewById(R.id.user_phone_input_register_patient);
        passwordInput = findViewById(R.id.user_password_input_register_patient);
        cityInput = findViewById(R.id.user_living_city_input_register_patient);
        streetInput = findViewById(R.id.user_living_street_input_register_patient);
        home_numberInput = findViewById(R.id.user_house_number_input_register_patient);

        /*Buttons_connection*/
        registerPatient_button = findViewById(R.id.registr_new_patient_button);
        progressBar = findViewById(R.id.progres_bar_patient);
        /*end_Buttons_connection*/

        /*FireBase_connection*/
        dataBase = FirebaseDatabase.getInstance();
        myDataBase = dataBase.getReference(PATIENTS);
        /*end_FireBase_connection*/
        //*************************************************************//

        registerPatient_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = first_nameInput.getText().toString().trim();
                String lastName = last_nameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String phone = phone_numberInput.getText().toString().trim();
                String patientID = patientID_Input.getText().toString().trim();

                /*checking if the inputs is valid inputs*/
                if (!validationTools.isPatientNamesIsValid(firstName, lastName, patientID,
                        first_nameInput, last_nameInput, patientID_Input)) {
                    return;
                }
                if (!validationTools.isAllCostumersNeedfulInputIsValid(email, password, phone, emailInput,
                        passwordInput, phone_numberInput)) {
                    return;
                }
                /*end_validation_checking*/

                /*checking if the user is already exist, if not added the user*/
                Query IDCheckingExistence = myDataBase.orderByChild("id").equalTo(patientID);
                IDCheckingExistence.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() > 0) {
                            Toast.makeText(Register_Patient_Activity.this, "משתמש זה כבר רשום, הת.ז קיים", Toast.LENGTH_LONG).show();
                        } else {
                            /*continue with other inputs after validation*/
                            String age = ageInput.getText().toString().trim();
                            String cityLiving = cityInput.getText().toString().trim();
                            String streetLiving = streetInput.getText().toString().trim();
                            String houseNumber = home_numberInput.getText().toString().trim();
                            /*end_all_inputs*/

                            /*create a new Patient*/
                            patientAddress = new Address(cityLiving, streetLiving, houseNumber);
                            costumer_details_patient = new Costumer_Details_Patient(email, phone, password,
                                    patientAddress, firstName, lastName, age, patientID);

                            /**all the needful details are enters, can move to register
                             *the user in fireBase
                             */
                            registerPatient(costumer_details_patient);
                            progressBar.setVisibility(View.VISIBLE);
                            openPatientMenu_Activity(costumer_details_patient.getID());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }

    /**
     * Adding patient to our Firebase DataBase
     *
     * @param costumer_details_patient
     */
    private void registerPatient(Costumer_Details_Patient costumer_details_patient) {
        myDataBase.child(costumer_details_patient.getID()).setValue(costumer_details_patient);
    }

    /*Activate Patient Menu activity*/
    private void openPatientMenu_Activity(String Patient_ID) {
        Intent open_patient_menu = new Intent(this, Login_Activity.class);
        //Intent open_institute_menu = new Intent(this,PatientMain.class);
        //open_institute_menu.putExtra("Patient_ID",Patient_ID);
        startActivity(open_patient_menu);
    }
}