package com.example.dimot_bekalot.entryActivities;
/**
 * This activity will lead the user to patient register
 * or to institute register
 */
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dimot_bekalot.R;

public class Register_Main_Activity extends AppCompatActivity {

    private Button main_register_patient_button;
    private Button main_register_institute_button;

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