package com.example.dimot_bekalot.SendNotificationPack;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface ApiInterface {
    @Headers(
            {
                    "Authorization: key=AAAADniALz8:APA91bF6XPcr0VGAY8_E5aRCObc_Mk8POjGQ_2aIS6zYNWUqHybwjawPdEKnbADACgq0VW74gw6K3XntLDpSQFPcLX654bBWqci2OI8MDJNTkCrNXK3qa7F0NNTKAGyrIwsbWHntleVv",
                    "Content-Type:application/json"
            }
    )

    @POST("fcm/send")

    Call<ResponseBody> sendNotification(@Body RootModel root);
}