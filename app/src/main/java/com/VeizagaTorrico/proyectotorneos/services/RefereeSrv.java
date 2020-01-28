package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.Constants;
import com.VeizagaTorrico.proyectotorneos.models.Referee;
import com.VeizagaTorrico.proyectotorneos.models.Success;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RefereeSrv {

    @GET(Constants.BASE_URL + "referees/competition")
    Call<List<Referee>> getReferees(@Query("idCompetencia") int idCompetencia);

    @POST( Constants.BASE_URL + "judge" )
    Call<Success>createReferee(@Body Map<String,String> juez);

    @DELETE( Constants.BASE_URL + "judge/del" )
    Call<Success>deleteReferee(@Query ("idJuez") int idJuez);

}
