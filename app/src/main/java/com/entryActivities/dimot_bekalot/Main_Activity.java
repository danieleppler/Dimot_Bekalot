package com.entryActivities.dimot_bekalot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main_Activity extends AppCompatActivity {

    private Button login_button;
    private Button register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Login button will start to work*/
        login_button = (Button)findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openLogin_Activity(); }
        });
        /*end_Login_button*/

        /*Register button will start to work*/
        register_button = (Button)findViewById(R.id.register_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){ openRegister_Activity(); }
        });
        /*end_Register_button*/

    }

    /************private function that will activate the activities************/
    /*Activate login activity*/
    private void openLogin_Activity() {
        Intent open_login = new Intent(this,Login_Activity.class);
        startActivity(open_login);
    }

    /*Activate register activity*/
    private void openRegister_Activity() {
        Intent open_register = new Intent(this,Register_Activity.class);
        startActivity(open_register);
    }
}