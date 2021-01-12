package com.example.dimot_bekalot.SendNotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAADniALz8:APA91bF6XPcr0VGAY8_E5aRCObc_Mk8POjGQ_2aIS6zYNWUqHybwjawPdEKnbADACgq0VW74gw6K3XntLDpSQFPcLX654bBWqci2OI8MDJNTkCrNXK3qa7F0NNTKAGyrIwsbWHntleVv" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}

