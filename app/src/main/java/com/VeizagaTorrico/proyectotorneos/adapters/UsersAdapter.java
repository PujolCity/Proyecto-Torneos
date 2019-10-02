package com.VeizagaTorrico.proyectotorneos.adapters;

import com.VeizagaTorrico.proyectotorneos.ConstantURL;
import com.VeizagaTorrico.proyectotorneos.services.UserSrv;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersAdapter {

    public UserSrv connectionEnable(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantURL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(UserSrv.class);
    }
}
