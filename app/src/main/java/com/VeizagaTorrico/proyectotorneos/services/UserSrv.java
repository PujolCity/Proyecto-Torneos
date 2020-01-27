package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.ConstantURL;
import com.VeizagaTorrico.proyectotorneos.models.RespRegisterService;
import com.VeizagaTorrico.proyectotorneos.models.Success;
import com.VeizagaTorrico.proyectotorneos.models.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserSrv {


    @GET(ConstantURL.BASE_URL + "users")
    Call<List<User>> getUsers();

    @GET(ConstantURL.BASE_URL + "usercomp/petitioners")
    Call<List<User>> getPetitionersByCompetition(@Query ("idCompetencia") int idCompetencia);

    @POST(ConstantURL.BASE_URL + "usercomp-del")
    Call<Success> refusePetitionerUser(@Body Map<String,String> userComp);

    @POST(ConstantURL.BASE_URL + "add-participate")
    Call<Success> acceptPetitionUser(@Body Map<String,String> userComp);

    @POST( ConstantURL.BASE_URL + "user" )
    Call<RespRegisterService>register(@Body Map<String,String> user);

    @GET(ConstantURL.BASE_URL + "competitors-competition")
    Call<List<User>> getCompetidoresByCompetencia(@Query ("idCompetencia") int idCompetencia);

}
