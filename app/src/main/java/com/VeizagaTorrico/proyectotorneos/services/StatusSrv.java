package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.Constants;
import com.VeizagaTorrico.proyectotorneos.models.Gender;
import com.VeizagaTorrico.proyectotorneos.models.RespEstado;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface StatusSrv {

    @GET(Constants.BASE_URL + "competition/status")
    Call<List<Gender>> getStatus();

}

