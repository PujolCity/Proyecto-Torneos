package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.Constants;
import com.VeizagaTorrico.proyectotorneos.models.Inscription;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface InscriptionSrv {

    @GET(Constants.BASE_URL + "inscription-competition")
    Call<Inscription> getInscripcion(@Query("idCompetencia") int idCompetencia);

    @POST(Constants.BASE_URL + "inscription")
    Call<MsgRequest> crearInscripcion(@Body Map<String,String> body);

}
