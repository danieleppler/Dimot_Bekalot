package com.example.dimot_bekalot.entryActivities;
/**
 * This activity is only showing an explanation about that the user have to verify
 * his email address before login
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dimot_bekalot.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EMail_Verification_Activity extends AppCompatActivity {

    private Intent retrieveFromRegister;
    private Handler mainHandler = new Handler();

    private Button toLogin;
    private TextView screenMassege;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_mail__varification);

        screenMassege = (TextView) findViewById(R.id.email_verification_text_thread);

        /*go to Login Button will connecting from view*/
        toLogin = (Button) findViewById(R.id.email_verification_move_to_login_button);
        toLogin.setVisibility(View.INVISIBLE);

        /*FireBase_connection*/
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        /*end_FireBase_connection*/

        //*************************************************************//

        /*send a verification to the new created user */
        mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Thread showText = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(5000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        retrieveFromRegister= getIntent();
                                        String temp = creteCleanUserName(retrieveFromRegister.getStringExtra("userName_ID"));
                                        screenMassege.setText(" שם המשתמש שישמש אתכם בעת ההתחברות הוא : "+temp+" אנא זכרו אותו ורק אחר כך עברו לעמוד ההתחברות ");
                                    }
                                });

                                /**waiting for user permission before move to login activity, so the user can read the text
                                 * and remember the user name*/
                                Thread.sleep(5000);
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        toLogin.setVisibility(View.VISIBLE);
                                        toLogin.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) { openLogin_Activity(); }
                                        });
                                    }
                                });

                            } catch (InterruptedException e) { e.printStackTrace(); }
                        }
                    };
                    showText.start();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "אנא המתן מספר שניות, מוודא פרטים , אין אפשרות לחזור אחורה", Toast.LENGTH_LONG).show();
    }

    /************private function************/
    /*Activate login activity*/
    private void openLogin_Activity() {
        Intent open_login = new Intent(getApplicationContext(), Login_Activity.class);
        startActivity(open_login);
    }

    /*split the string that contains the user name , to take the user name without ":"*/
    private String creteCleanUserName(String toUserName) {
        StringBuffer clean_userName_ID = new StringBuffer(toUserName);
        clean_userName_ID.deleteCharAt(1);
        return clean_userName_ID.toString();
    }
}