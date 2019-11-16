package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.ConstantURL;
import com.VeizagaTorrico.proyectotorneos.models.Ground;
import com.VeizagaTorrico.proyectotorneos.models.Success;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface GroundSrv {

    @GET(ConstantURL.BASE_URL + "grounds/competition")
    Call<List<Ground>> getGrounds(@Query("idCompetencia") int idCompetencia);

    @POST( ConstantURL.BASE_URL + "grounds" )
    Call<Success>createGround(@Body Map<String,String> predio);

    @DELETE( ConstantURL.BASE_URL + "grounds/del" )
    Call<Success>deleteGround(@QueryMap Map<String,Integer> datos);

}
