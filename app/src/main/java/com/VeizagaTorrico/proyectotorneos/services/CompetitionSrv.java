package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.ConstantURL;
import com.VeizagaTorrico.proyectotorneos.models.Competition;
import com.VeizagaTorrico.proyectotorneos.models.Success;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CompetitionSrv {

    @FormUrlEncoded
    @POST( ConstantURL.BASE_URL + "existcompetition" )
    Call<Success>comprobar(@Field("competencia") String competencia);

    @GET(ConstantURL.BASE_URL + "competitions")
    Call<List<Competition>> getCompetitions();


}
