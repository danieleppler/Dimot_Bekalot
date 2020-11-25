package com.example.dimot_bekalot.entryActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.tools.validationTools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register_Institute_Activity extends AppCompatActivity {

    EditText institute_nameInput,user_nameInput, emailInput, phone_numberInput;
    EditText passwordInput, cityInput, streetInput, home_numberInput;
    Button registerInstitute_button;
    //TextView mLoginBtn;
    FirebaseAuth fAuto;/*instance to register the institute */
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_institute);

        institute_nameInput = findViewById(R.id.user_first_name_input_register_institute);
        user_nameInput = findViewById(R.id.user_user_name_input_register_institute);
        emailInput = findViewById(R.id.user_email_input_register_institute);
        phone_numberInput = findViewById(R.id.user_phone_input_register_institute);
        passwordInput = findViewById(R.id.user_password_input_register_institute);
        cityInput = findViewById(R.id.user_living_city_input_register_institute);
        streetInput = findViewById(R.id.user_living_street_input_register_institute);
        home_numberInput = findViewById(R.id.user_house_number_input_register_institute);

        registerInstitute_button = findViewById(R.id.registr_new_institute_button);
        fAuto = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.user_progress_bar_institute);
        //*************************************************************//

        /*checking if the institute is already exist*/
        if (fAuto.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), Login_Activity.class));
            finish();
        }
        //*************************************************************//
        registerInstitute_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String instituteName = institute_nameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String phone = phone_numberInput.getText().toString().trim();

                /*checking if the inputs is valid inputs*/
                if (!validationTools.isInstituteNamesIsValid(instituteName, institute_nameInput)) {
                    return;
                }
                if (!validationTools.isAllCostumersNeedfulInputIsValid(email, password, phone, emailInput,
                        passwordInput, phone_numberInput)) {
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                /**all the needful details are enters, can move to register
                 *the user in fireBase
                 */

                /*On Complete Listener will return trie or false is the user add or not
                by email ans password*/
                fAuto.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register_Institute_Activity.this, "החשבון נוצר בהצלחה", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Register_Institute_Activity.this, Main_Activity.class));
                        } else {
                            Toast.makeText(Register_Institute_Activity.this, "לא יכול להוסיך למסד נתונים" + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}