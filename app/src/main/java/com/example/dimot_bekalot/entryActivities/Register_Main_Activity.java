package com.example.dimot_bekalot.entryActivities;
/**
 * This activity will lead the user to patient register
 * or to institute register
 */
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.dimot_bekalot.R;

public class Register_Main_Activity extends AppCompatActivity {

    private Button main_register_patient_button;
    private Button main_register_institute_button;

    private ImageButton logOut;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);

        /*patient register button will start to work*/
        main_register_patient_button = (Button) findViewById(R.id.patient_register_button);
        main_register_patient_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPatientRegister_Activity();
            }
        });
        /*end_patient_register_button*/

        /*institute register button will start to work*/
        main_register_institute_button = (Button) findViewById(R.id.institute_register_button);
        main_register_institute_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInstituteRegister_Activity();
            }
        });
        /*end_institute_register_button*/

        /*Bottun_log-out*/
        logOut = (ImageButton) findViewById(R.id.logOutButton);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logOutIntent = new Intent(context, com.example.dimot_bekalot.entryActivities.Main_Activity.class);
                startActivity(logOutIntent);
            }
        });
        /*end_Bottun_log-out*/
    }

    @Override
    public void onBackPressed() {
        Intent open_login = new Intent(this, Main_Activity.class);
        open_login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(open_login);
    }

    /************private function************/
    /*Activate patient register activity*/
    private void openPatientRegister_Activity() {
        Intent open_patient_register = new Intent(this, Register_Patient_Activity.class);
        startActivity(open_patient_register);
    }

    /*Activate institute register activity*/
    private void openInstituteRegister_Activity() {
        Intent open_institute_register = new Intent(this, Register_Institute_Activity.class);
        startActivity(open_institute_register);
    }
}