package com.example.dimot_bekalot.entryActivities;
/**
 *
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.dataObjects.Login_Input_Data;
import com.example.dimot_bekalot.tools.validationTools;

public class Login_Activity extends AppCompatActivity {

    private Button registerButton;
    private Button submitLoginButton;
    private Button forgetButton;

    private String UserName;
    private String password;
    private String email;

    private EditText UsernameInput;
    private EditText passwordInput;
    private EditText emailInput;

    private Login_Input_Data loginCostumerInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UsernameInput = (EditText) findViewById(R.id.user_userName_input_login);
        passwordInput = (EditText) findViewById(R.id.user_password_input_login);
        emailInput = (EditText) findViewById(R.id.user_email_input_login);

        /*connecting the Buttons from view*/
        submitLoginButton = (Button) findViewById(R.id.LOGIN_Button);
        forgetButton = (Button)findViewById(R.id.forget_login_button);
        registerButton = (Button) findViewById(R.id.register_button_login);

        //*************************************************************//

        submitLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserName = UsernameInput.getText().toString().trim();
                password = passwordInput.getText().toString().trim();
                email = emailInput.getText().toString().trim();

                /*checking if the inputs is valid inputs*/
                if (!validationTools.isLoginInputValid(UserName, password, UsernameInput, passwordInput)) { return; }
                if (!validationTools.CheckIfNumber(only_number_at_ID(UserName), UsernameInput)) { return; }
                /*end_validation_checking*/

                loginCostumerInput = new Login_Input_Data(createNOTCleanUserName(UserName), password, email);
                open_login_Verification_Activity();
            }
        });
        /*end_Submit_Login_button*/

        /*Register button will start to work*/
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainRegister_Activity();
            }
        });
        /*end_Register_button*/

        /*Forgot Password button will start to work*/
        forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_Forgot_Password_Activity();
            }

        });
        /*end_Forgot_Password_button*/

    }

    /************private function************/
    /*add to the string that contains the user name , the ":"*/
    private String createNOTCleanUserName(String toUserName) {
        StringBuilder newUserName = new StringBuilder(toUserName);
        newUserName.insert(1,":");
        return newUserName.toString();
    }

    /*clean the string and remain only the ID with 9 digits*/
    private String only_number_at_ID(String UserName){
        StringBuffer clean_ID = new StringBuffer(UserName);
        clean_ID.deleteCharAt(0);
        return clean_ID.toString();
    }

    /*Activate forgot password activity*/
    private void open_Forgot_Password_Activity() {
        Intent open_forget_register = new Intent(this, Forget_Password_Activity.class);
        startActivity(open_forget_register);
    }

    /*Activate register activity*/
    private void openMainRegister_Activity() {
        Intent open_main_register = new Intent(this, Register_Main_Activity.class);
        startActivity(open_main_register);
    }

    /*Activate verification activity*/
    private void open_login_Verification_Activity() {
        Intent open_verification = new Intent(this, Login_Verification_Activity.class);
        open_verification.putExtra("Login_Input_Data", loginCostumerInput);
        startActivity(open_verification);
    }
}