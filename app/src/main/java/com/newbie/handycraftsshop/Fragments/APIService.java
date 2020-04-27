package com.newbie.handycraftsshop.Fragments;

/**
 * Created by wahyu_septiadi on 26, April 2020.
 * Visit My GitHub --> https://github.com/WahyuSeptiadi
 */

import com.newbie.handycraftsshop.Notifications.MyResponse;
import com.newbie.handycraftsshop.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    // Cloud Messaging key
                    "Authorization:key=AAAAxg_43zY:APA91bE3wKJTTFqiDi5PfLob-Nk2dzTzrNnQ32qRQfDPAatVLsTZFzagKXzXFx3SorAsjIWnbbQQzclS-fZLa8qWY1ZejhckoA-dNeKZa7TtfWSg8lYtAbGKkId-HyMEx5WHgdoap7pO"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
