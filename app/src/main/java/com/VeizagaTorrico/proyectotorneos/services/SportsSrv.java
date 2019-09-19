package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.ConstantURL;
import com.VeizagaTorrico.proyectotorneos.models.Sport;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface SportsSrv {

    @GET(ConstantURL.BASE_URL + "sports")
    Call<List<Sport>> getSports();

}
