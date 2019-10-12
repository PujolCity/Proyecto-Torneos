package com.VeizagaTorrico.proyectotorneos;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAdapter {

    public Retrofit connectionEnable(){
        return new Retrofit.Builder()
                .baseUrl(ConstantURL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
