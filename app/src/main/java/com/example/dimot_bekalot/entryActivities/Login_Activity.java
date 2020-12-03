package com.example.dimot_bekalot.entryActivities;
/**
 *
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.dataObjects.Login_Input_Data;
import com.example.dimot_bekalot.tools.validationTools;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login_Activity extends AppCompatActivity {

    private Button registerButton;
    private Button submitLoginButton;

    private String ID;
    private String password;

    private EditText IDinput;
    private EditText passwordInput;

    private FirebaseDatabase dataBase;
    private DatabaseReference myDataBasePatients;
    private DatabaseReference myDataBaseInstitutes;
    private FirebaseAuth fAuto;

    private Login_Input_Data loginCostumer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        IDinput = (EditText) findViewById(R.id.user_ID_input_login);
        passwordInput = (EditText) findViewById(R.id.user_password_input_login);

        /*Submit Login Button will connecting from view*/
        submitLoginButton = (Button) findViewById(R.id.LOGIN_Button);

        //*************************************************************//

        submitLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = IDinput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                /*checking if the inputs is valid inputs*/
                if (!validationTools.isLoginInputValid(ID, password, IDinput, passwordInput)) {
                    return;
                }
                /*end_validation_checking*/

                loginCostumer = new Login_Input_Data(ID, password);
                openVerification_Activity();
            }
        });
        /*end_Submit_Login_button*/

        /*Register button will start to work*/
        registerButton = (Button) findViewById(R.id.register_button_login);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainRegister_Activity();
            }
        });
        /*end_Register_button*/
    }

    /************private function that will activate the activities************/
    /*Activate register activity*/
    private void openMainRegister_Activity() {
        Intent open_main_register = new Intent(this, Register_Main_Activity.class);
        startActivity(open_main_register);
    }

    /*Activate verification activity*/
    private void openVerification_Activity() {
        Intent open_verification = new Intent(this, Verification_Activity.class);
        open_verification.putExtra("Login_Input_Data", loginCostumer);
        startActivity(open_verification);
    }
}