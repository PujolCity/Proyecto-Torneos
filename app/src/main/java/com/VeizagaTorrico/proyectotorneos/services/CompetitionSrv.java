package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.ConstantURL;
import com.VeizagaTorrico.proyectotorneos.models.Success;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CompetitionSrv {

    @FormUrlEncoded
    @POST( ConstantURL.BASE_URL + "existcompetition" )
    Call<Success>comprobar(@Field("competencia") String competencia);

}
