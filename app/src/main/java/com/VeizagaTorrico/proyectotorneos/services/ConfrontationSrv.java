package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.ConstantURL;
import com.VeizagaTorrico.proyectotorneos.models.Confrontation;
import com.VeizagaTorrico.proyectotorneos.models.ConfrontationFull;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ConfrontationSrv {

    @GET(ConstantURL.BASE_URL + "confrontations/competition")
    Call<List<Confrontation>> getConfrontation(@Query("idCompetencia") int idCompetencia);

    @POST(ConstantURL.BASE_URL + "confrontations/competition")
    Call<List<Confrontation>> getConfrontations(@Query("idCompetencia") int idCompetencia, @Body Map<String,String> fecha_grupo);

    @PUT(ConstantURL.BASE_URL + "confrontation")
    Call<MsgRequest> editEncuentro(@Body Map<String,String> encuentro);

}
