package com.VeizagaTorrico.proyectotorneos.adapters;

import com.VeizagaTorrico.proyectotorneos.ConstantURL;
import com.VeizagaTorrico.proyectotorneos.services.SportsSrv;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SportsAdapter {

    public SportsSrv connectionEnable() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantURL.GET_SPORTS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(SportsSrv.class);
    }


}
