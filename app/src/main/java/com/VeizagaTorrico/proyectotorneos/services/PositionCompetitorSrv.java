package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.Constants;
import com.VeizagaTorrico.proyectotorneos.models.PositionCompetitor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PositionCompetitorSrv {

    @GET(Constants.BASE_URL + "result/score")
    Call<List<PositionCompetitor>> getTablePositions(@Query("idCompetencia") int idCompetencia);

    @GET(Constants.BASE_URL + "result/score")
    Call<List<PositionCompetitor>> getTablePositionsByGroup(@Query("idCompetencia") int idCompetencia, @Query("grupo") int grupo);

}
