package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.ConstantURL;
import com.VeizagaTorrico.proyectotorneos.models.Referee;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RefereeSrv {

    @GET(ConstantURL.BASE_URL + "referees/competition")
    Call<List<Referee>> getReferees(@Query("idCompetencia") int idCompetencia);

}
