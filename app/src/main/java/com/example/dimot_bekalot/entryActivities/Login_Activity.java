package com.example.dimot_bekalot.entryActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dimot_bekalot.R;

public class Login_Activity extends AppCompatActivity {

    private Button registerButton;
    private Button submitLoginButton;

    private String email;
    private long password;

    private EditText emailInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = (EditText) findViewById(R.id.user_email_input);
        passwordInput = (EditText) findViewById(R.id.user_password_input);

        /*Submit Login Button will start to work*/
        submitLoginButton = (Button) findViewById(R.id.LOGIN_Button);
        submitLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailInput.getText() == null || passwordInput.getText() == null) {
                    submitAgainToast();
                }
//                email = emailInput.getText().toString();
//                password = Long.valueOf(passwordInput.getText().toString());

            }
        });
        /*end_Submit_Login_button*/

        /*Register button will start to work*/
        registerButton = (Button) findViewById(R.id.register_button);
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

    private void submitAgainToast() {
        Toast.makeText(this, "נתונים שגויים, נסה שנית בבקשה", Toast.LENGTH_SHORT).show();
    }
}
