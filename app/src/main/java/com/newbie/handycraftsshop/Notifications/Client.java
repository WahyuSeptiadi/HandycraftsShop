package com.newbie.handycraftsshop.Notifications;

/**
 * Created by wahyu_septiadi on 26, April 2020.
 * Visit My GitHub --> https://github.com/WahyuSeptiadi
 */

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private static Retrofit retrofit = null;
    public static Retrofit getClient(String url){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
