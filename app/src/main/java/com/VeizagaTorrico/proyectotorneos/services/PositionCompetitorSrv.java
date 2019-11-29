package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.ConstantURL;
import com.VeizagaTorrico.proyectotorneos.models.PositionCompetitor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PositionCompetitorSrv {

    @GET(ConstantURL.BASE_URL + "result/score")
    Call<List<PositionCompetitor>> getTablePositions(@Query("idCompetencia") int idCompetencia);

    @GET(ConstantURL.BASE_URL + "result/score")
    Call<List<PositionCompetitor>> getTablePositionsByGroup(@Query("idCompetencia") int idCompetencia, @Query("grupo") int grupo);

}
