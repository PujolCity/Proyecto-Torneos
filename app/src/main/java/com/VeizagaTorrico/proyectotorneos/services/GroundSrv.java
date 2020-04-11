package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.Constants;
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

    @GET(Constants.BASE_URL + "grounds")
    Call<List<Ground>> getGrounds();

    @POST( Constants.BASE_URL + "grounds" )
    Call<Success>createGround(@Body Map<String,String> predio);

    @DELETE( Constants.BASE_URL + "grounds/del" )
    Call<Success>deleteGround(@QueryMap Map<String,Integer> datos);

    @GET(Constants.BASE_URL + "grounds/competition")
    Call<List<Ground>> getPrediosAsignados(@Query("id") int idCompetencia);

    @POST( Constants.BASE_URL + "set-ground" )
    Call<Success>asignarPredioCompetencia(@Body Map<String,String> predio);

    @DELETE( Constants.BASE_URL + "del-groundCompetition" )
    Call<Success>deletePredioCompetencia(@QueryMap Map<String,String> datos);

    @GET(Constants.BASE_URL + "grounds/name")
    Call<List<Ground>> findLikeName(@Query("nombre") String nombre);

}
