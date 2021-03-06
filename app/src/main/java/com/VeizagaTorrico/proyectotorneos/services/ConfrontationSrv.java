package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.Constants;
import com.VeizagaTorrico.proyectotorneos.models.Confrontation;

import com.VeizagaTorrico.proyectotorneos.models.ConfrontationsCompetition;
import com.VeizagaTorrico.proyectotorneos.models.Edition;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.offline.model.ConfrontationEdit;
import com.VeizagaTorrico.proyectotorneos.offline.model.ConfrontationOff;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ConfrontationSrv {

    @GET(Constants.BASE_URL + "confrontations/competition")
    Call<List<Confrontation>> getConfrontation(@Query("idCompetencia") int idCompetencia);

    @POST(Constants.BASE_URL + "confrontations/competition")
    Call<ConfrontationsCompetition> getConfrontations(@Query("idCompetencia") int idCompetencia, @Body Map<String,String> fecha_grupo);

    @PUT(Constants.BASE_URL + "confrontation")
    Call<MsgRequest> editEncuentro(@Body Map<String,String> encuentro);

    @GET(Constants.BASE_URL + "confrontations/competition-off")
    Call<List<ConfrontationOff>> confrontationsOffline(@Query ("idCompetencia") int idCompetencia);

    @PUT(Constants.BASE_URL + "confrontation-off")
    Call<MsgRequest> syncToServer(@Body Map<String,String> encuentros);

    @GET(Constants.BASE_URL + "confrontation/edition")
    Call<List<Edition>> getEditions(@Query("idEncuentro") int idEncuentro);
}
