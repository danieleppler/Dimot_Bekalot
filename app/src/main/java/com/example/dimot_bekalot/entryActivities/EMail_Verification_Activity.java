package com.example.dimot_bekalot.entryActivities;
/**
 * This activity is only showing an explanation about that the user have to verify
 * his email address before login
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.example.dimot_bekalot.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EMail_Verification_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_mail__varification);

        TextView screenMassege = (TextView) findViewById(R.id.email_verification_text_thread);

        /*FireBase_connection*/
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        /*end_FireBase_connection*/


        /*send a verification to th new created user */
        mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Thread showText = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(6000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        screenMassege.setText("הנכם מועברים לעמוד ההתחברות, תוכלו להתחבר לחשבונכם רק במידה ואישרתם את כתובתכם");
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            /*waiting before move to login activity, so the user can read the text*/
                            try {
                                sleep(10000);
                                openLogin_Activity();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    showText.start();
                }
            }
        });
    }

    /************private function************/
    /*Activate login activity*/
    private void openLogin_Activity() {
        Intent open_login = new Intent(getApplicationContext(), Login_Activity.class);
        startActivity(open_login);
    }
}