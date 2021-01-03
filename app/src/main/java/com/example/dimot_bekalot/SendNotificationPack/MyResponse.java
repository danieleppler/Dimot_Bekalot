package com.example.dimot_bekalot.SendNotificationPack;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

public class MyResponse implements Result {
    public int success;

    @Override
    public Status getStatus() {
        return null;
    }
}
