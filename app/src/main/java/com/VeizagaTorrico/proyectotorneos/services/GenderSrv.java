package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.ConstantURL;
import com.VeizagaTorrico.proyectotorneos.models.Gender;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GenderSrv {

    @GET(ConstantURL.BASE_URL + "genders")
    Call<List<Gender>> getGenders();

}

