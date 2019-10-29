package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.ConstantURL;
import com.VeizagaTorrico.proyectotorneos.models.Confrontation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ConfrontationSrv {

    @GET(ConstantURL.BASE_URL + "confrontations")
    Call<List<Confrontation>> getConfrontation();

}
