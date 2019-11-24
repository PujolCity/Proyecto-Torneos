package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.ConstantURL;
import com.VeizagaTorrico.proyectotorneos.models.Success;
import com.VeizagaTorrico.proyectotorneos.models.Turn;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface TurnSrv {

    @GET(ConstantURL.BASE_URL + "turn/competition")
    Call<List<Turn>> getTurnsByCompetition(@Query("idCompetencia") int idCompetencia);

    @POST( ConstantURL.BASE_URL + "turn" )
    Call<Success>createTurn(@Body Map<String,String> turno);

    @DELETE( ConstantURL.BASE_URL + "turn/del" )
    Call<Success>deleteTurn(@QueryMap Map<String,Integer> turno);

}
