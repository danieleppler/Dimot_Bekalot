package com.example.dimot_bekalot.entryActivities;
/**
 * This activity is the START Page of the application
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.connectActivities.Call_To_Institute_Activity;

public class Main_Activity extends AppCompatActivity {

    private Button login_button;
    private Button register_button;

    private Button test;


    /*test section*/
    private Button test_connect;
    /*end test section*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Login button will start to work*/
        login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin_Activity();
            }
        });
        /*end_Login_button*/

        /*Register button will start to work*/
        register_button = (Button) findViewById(R.id.register_button_login);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainRegister_Activity();
            }
        });
        /*end_Register_button*/

        /*test section*/
        test_connect = (Button) findViewById(R.id.testButton);
        test_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConnect_Activity();
            }
        });
        /*end test section*/
    }

    /************private function************/
    private void openConnect_Activity() {
        Intent open_Connect_register = new Intent(this, Call_To_Institute_Activity.class);
        startActivity(open_Connect_register);
    }

    /*Activate login activity*/
    private void openLogin_Activity() {
        Intent open_login = new Intent(this, Login_Activity.class);
        startActivity(open_login);
    }

    /*Activate main register activity*/
    private void openMainRegister_Activity() {
        Intent open_main_register = new Intent(this, Register_Main_Activity.class);
        startActivity(open_main_register);
    }

    /*Activate main connect to support activity*/
//    private void OpenConnectSupport() {
//        Intent open_connect = new Intent(this, Connect_Activity.class);
//        startActivity(open_connect);
//    }
}