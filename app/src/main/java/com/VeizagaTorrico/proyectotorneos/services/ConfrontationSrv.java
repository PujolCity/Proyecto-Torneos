package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.ConstantURL;
import com.VeizagaTorrico.proyectotorneos.models.Confrontation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ConfrontationSrv {

    @GET(ConstantURL.BASE_URL + "confrontations/competition")
    Call<List<Confrontation>> getConfrontation(@Query("idCompetencia") int idCompetencia);

}
