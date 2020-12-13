package com.example.dimot_bekalot.entryActivities;
/**
 * A looper to run and check inValid inptut at the login to lock the user
 * and stop the looper outside the main looper
 *
 * NOT USED YET
 */

import android.os.Handler;
import android.os.Looper;

public class BlockAccount_Lopper extends Thread {

    public Looper Block_looper = Looper.myLooper();
    public Handler Block_handler = new Handler();

    @Override
    public void run() {
        Looper.prepare();
        Looper.loop();
    }
}
